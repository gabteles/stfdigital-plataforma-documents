package br.jus.stf.plataforma.documento.domain.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.lang3.Validate;

import br.jus.stf.core.framework.domaindrivendesign.ValueObjectSupport;

/**
 * @author Lucas.Rodrigues
 *
 */
public class ConteudoDocumento extends ValueObjectSupport<ConteudoDocumento> {
	
	private byte[] bytes;
	private Long tamanho;
	
	public ConteudoDocumento(final byte[] bytes, final Long tamanho) {
		Validate.notNull(bytes, "documentodownload.bytes.required");
		Validate.notNull(tamanho, "documentodownload.tamanho.required");
		
		this.bytes = bytes;
		this.tamanho = tamanho;
	}
	
	public InputStream stream() {
		return new ByteArrayInputStream(bytes);
	}
	
	public Long tamanho() {
		return tamanho;
	}

}
