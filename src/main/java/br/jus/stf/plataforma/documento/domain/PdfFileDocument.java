package br.jus.stf.plataforma.documento.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.commons.lang3.Validate;

import br.jus.stf.plataforma.documento.domain.model.Document;

public class PdfFileDocument implements Document {

	private File file;

	public PdfFileDocument(File file) {
		Validate.notNull(file);

		this.file = file;
	}

	@Override
	public InputStream stream() {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("Erro ao abrir pdf em arquivo para assinatura.", e);
		}
	}
}
