package br.jus.stf.plataforma.documento.domain;

import java.io.InputStream;

import br.jus.stf.plataforma.documento.domain.model.Document;

public class PdfInputStreamDocument implements Document {

	private InputStream stream;

	public PdfInputStreamDocument(InputStream stream) {
		this.stream = stream;
	}

	@Override
	public InputStream stream() {
		return stream;
	}
	
}
