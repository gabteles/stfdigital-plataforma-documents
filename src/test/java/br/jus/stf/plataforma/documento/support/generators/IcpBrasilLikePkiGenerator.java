package br.jus.stf.plataforma.documento.support.generators;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import br.jus.stf.plataforma.documento.support.pki.CustomPki;
import br.jus.stf.plataforma.documento.support.pki.CustomPkiGenerator;

public class IcpBrasilLikePkiGenerator {

	private static final String RESOURCES_DIR = "src/main/resources";
	private static final String PKIS_DIR = "/certification/pkis/";

	/**
	 * <pki-name>
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Informe exatamente um parâmetro: <pki-name>");
			System.exit(1);
		}
		String pkiName = args[0];
		System.out.println("pki-name: " + pkiName);
		File pkiCandidateDir = new File(RESOURCES_DIR + PKIS_DIR + pkiName);
		if (pkiCandidateDir.exists()) {
			System.err.println("Pki " + pkiName + " já existe.");
			System.exit(1);
		}
		CustomPkiGenerator pkiGenerator = new CustomPkiGenerator();
		String rootCN = "Autoridade Certificadora Raiz da Plataforma STF Digital v1";
		String[] intermediate = new String[] { "AC Intermediaria 1 da Plataforma STF Digital v1",
				"AC Intermediaria 2 da Plataforma STF Digital v1" };
		CustomPki customPki = pkiGenerator.generateCustomPKI(rootCN, intermediate);
		if (pkiCandidateDir.mkdir()) {
			System.out.println(customPki);
			File privateDir = new File(pkiCandidateDir.getPath() + "/private");
			if (privateDir.mkdir()) {
				GeneratorUtil.storeOnDisk(customPki, privateDir.getPath() + "/keystore.p12");
				GeneratorUtil.storeCertificatesOnDisk(customPki, privateDir.getPath());
				createFirstSerial(privateDir.getPath() + "/nextSerial");
			} else {
				System.err.println("Erro ao criar diretório privado da pki.");
				System.exit(1);
			}
		} else {
			System.err.println("Erro ao criar diretório da pki.");
			System.exit(1);
		}
	}

	private static void createFirstSerial(String path) throws IOException {
		FileUtils.writeStringToFile(new File(path), "1");
	}

}
