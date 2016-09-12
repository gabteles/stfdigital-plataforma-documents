package br.jus.stf.plataforma.documento.domain.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Table;

import org.apache.commons.lang3.Validate;

import br.jus.stf.core.framework.domaindrivendesign.EntitySupport;
import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.core.shared.documento.TextoId;

/**
 * @author Tomas.Godoi
 *
 */
@javax.persistence.Entity
@Table(name = "TEXTO", schema = "DOCUMENTS")
public class Texto extends EntitySupport<Texto, TextoId> {

	@EmbeddedId
	private TextoId id;

	@Embedded
	private DocumentoId documento;
	
	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "SEQ_DOCUMENTO_FINAL"))
	private DocumentoId documentoFinal;

	Texto() {
		// Construtor default.
	}

	/**
	 * @param id
	 * @param documento
	 */
	public Texto(final TextoId id, final DocumentoId documento) {
		Validate.notNull(id, "Identificador requerido.");
		Validate.notNull(documento, "Documento requerido.");

		this.id = id;
		this.documento = documento;
	}

	/**
	 * @return
	 */
	public DocumentoId documento() {
		return documento;
	}

	/**
	 * @return
	 */
	public DocumentoId documentoFinal() {
		return documentoFinal;
	}
	
	/**
	 * Associa o documento final do texto.
	 * 
	 * @param documentoFinal
	 */
	public void associarDocumentoFinal(DocumentoId documentoFinal) {
		Validate.notNull(documentoFinal, "Documento final requerido.");
		
		this.documentoFinal = documentoFinal;
	}
	
	@Override
	public TextoId identity() {
		return id;
	}

}
