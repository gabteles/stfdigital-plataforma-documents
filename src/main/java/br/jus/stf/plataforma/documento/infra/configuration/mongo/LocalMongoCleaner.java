package br.jus.stf.plataforma.documento.infra.configuration.mongo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import br.jus.stf.plataforma.documento.infra.LocalData;

/**
 * Classe utilitária para matar processos remanescentes do mongo no ambiente
 * local.
 * 
 * @author Tomas.Godoi
 *
 */
public final class LocalMongoCleaner {

	private static final Pattern pattern = Pattern.compile(".*extract.+extractmongod.*");
	private static final String osName = System.getProperty("os.name").toLowerCase();

	private LocalMongoCleaner() {

	}

	public static void killExtracted() throws IOException {
		if (isWindows()) {
			killExtractedWindows();
		} else if (isLinux() || isMacOsX()) {
			killExtractedUnix();
		}
	}

	private static boolean isWindows() {
		return osName.startsWith("windows");
	}

	private static boolean isMacOsX() {
		return osName.equals("mac os x");
	}

	private static boolean isLinux() {
		return osName.startsWith("linux");
	}
	
	private static void killExtractedWindows() {
		BufferedReader reader = null;
		try {
			String line;
			Process p = Runtime.getRuntime().exec("tasklist /fo csv /nh");
			reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = reader.readLine()) != null) {
				String[] cols = line.split(",");
				String processName = cols[0].replace("\"", "");
				if (isMongodExtractedProcess(processName)) {
					String pid = cols[1].replace("\"", "");
					Runtime.getRuntime().exec(String.format("taskkill /f /pid %s", pid));
				}
			}
			reader.close();
		} catch (IOException e) {
			throw new RuntimeException("Erro ao matar o processo do mongodb.", e);
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	private static void killExtractedUnix() {
		BufferedReader reader = null;
		try {
			String line;
			Process p = Runtime.getRuntime().exec("ps -ewwo pid,command");
			reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = reader.readLine()) != null) {
				if (isMongodExtractedProcess(line)) {
					String pid = line.trim().split(" ")[0];
					Runtime.getRuntime().exec(String.format("kill -9 %s", pid));
				}
			}
			reader.close();
		} catch (IOException e) {
			throw new RuntimeException("Erro ao matar o processo do mongodb.", e);
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}
	
	private static boolean isMongodExtractedProcess(String processName) {
		Matcher matcher = pattern.matcher(processName);
		return matcher.matches();
	}

	/**
	 * Realiza até 30 tentativas de apagar o arquivo de lock do mongo a cada 100
	 * milissegundos.
	 * 
	 */
	public static void clearLock() {
		File lockFile = new File(LocalData.instance().dataDirAbsolutePath() + "/mongodb/mongod.lock");
		int timeout = 3000;
		int waitTime = 100;
		if (lockFile.exists()) {
			while (true) {
				if (timeout <= 0)
					throw new RuntimeException("Não conseguiu limpar lock do mongo.");
				try {
					FileUtils.forceDelete(lockFile);
					break;
				} catch (IOException e) {
					try {
						timeout -= waitTime;
						Thread.sleep(waitTime);
					} catch (InterruptedException e1) {
						// Ignora
					}
				}
			}
		}
	}

}
