package br.jus.stf.plataforma.documento.domain.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Table;

import org.apache.commons.lang3.Validate;

import br.jus.stf.core.framework.domaindrivendesign.Entity;
import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.core.shared.documento.TextoId;

@javax.persistence.Entity
@Table(name = "TEXTO", schema = "DOCUMENTO")
public class Texto implements Entity<Texto, TextoId> {

	@EmbeddedId
	private TextoId id;

	@Embedded
	private DocumentoId documento;
	
	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "SEQ_DOCUMENTO_FINAL"))
	private DocumentoId documentoFinal;

	Texto() {

	}

	public Texto(final TextoId id, final DocumentoId documento) {
		Validate.notNull(id, "texto.id.required");
		Validate.notNull(documento, "texto.documento.required");

		this.id = id;
		this.documento = documento;
	}

	@Override
	public TextoId identity() {
		return id;
	}

	public DocumentoId documento() {
		return documento;
	}

	/**
	 * Associa o documento final do texto.
	 * 
	 * @param documentoFinal
	 */
	public void associarDocumentoFinal(DocumentoId documentoFinal) {
		Validate.notNull(documentoFinal, "texto.documentoFinal.required");
		
		this.documentoFinal = documentoFinal;
	}
	
	@Override
	public boolean sameIdentityAs(Texto other) {
		return other != null && this.id.sameValueAs(other.id);
	}

}
