package br.jus.stf.plataforma.documento.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.Validate;

import br.jus.stf.plataforma.documento.domain.model.Document;

public class PdfTempDocument implements Document {

	private static final String FILE_NAME_PREFFIX = "_DocToSignTemp_";
	private static final String FILE_NAME_EXTENSION = ".pdf";

	private File file;

	public PdfTempDocument(InputStream stream) {
		Validate.notNull(stream);

		file = createTempFile(stream);
	}

	private File createTempFile(InputStream stream) {
		File tempFile = null;
		try (InputStream in = stream) {
			tempFile = new File(FileUtils.getTempDirectory(), FILE_NAME_PREFFIX + Long.toString(System.currentTimeMillis()) + "_" + RandomStringUtils.randomNumeric(20) + "." + FILE_NAME_EXTENSION);
			FileUtils.copyInputStreamToFile(in, tempFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return tempFile;
	}

	public String tempId() {
		return file.getName();
	}

	public String tempPath() {
		return file.getAbsolutePath();
	}

	@Override
	public InputStream stream() {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("Erro ao abrir pdf temporário para assinatura.", e);
		}
	}

}
