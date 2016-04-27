package br.jus.stf.plataforma.documento.infra.configuration.fs;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import com.google.common.io.Files;

import br.jus.stf.core.framework.Profiles;
import br.jus.stf.plataforma.documento.infra.DocumentProfiles;

@Configuration
@Profile(DocumentProfiles.DOCUMENTO_FS)
public class FSConfiguration {

	@Value("${fs.docs-dir}")
	private String docsDirPath;
	
	@Autowired
	private Environment env;
	
	@PostConstruct
	public void checkDocsDir() throws IOException {
		File docsDir = new File(documentosDirPath());
		if (env.acceptsProfiles(Profiles.DEVELOPMENT)) { // No desenvolvimento, tenta criar diretório
			if (!docsDir.exists()) {
				if (!docsDir.mkdir()) throw new RuntimeException("Erro ao criar o diretório de documentos.");
			}
		} else {
			if (!docsDir.exists()) {
				throw new RuntimeException("Diretório de documentos inexistente.");
			}
		}
	}

	@Bean(name = "documentosDirPath")
	public String documentosDirPath() {
		if (!willStoreOnTempDir()) {
			String normalizedPath;
			if (docsDirPath.startsWith("~")) {
				normalizedPath = System.getProperty("user.home") + docsDirPath.substring(1);
			} else {
				normalizedPath = docsDirPath;
			}
			return normalizedPath;
		} else {
			return Files.createTempDir().getAbsolutePath();
		}
	}

	/**
	 * Armazena em diretório temporário no desenvolvimento e quando não se quer
	 * manter os dados.
	 * 
	 * @return
	 */
	private boolean willStoreOnTempDir() {
		return env.acceptsProfiles(Profiles.DEVELOPMENT) && !env.acceptsProfiles(DocumentProfiles.KEEP_DATA);
	}
	
}