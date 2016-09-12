package br.jus.stf.plataforma.documento.domain.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Table;

import org.apache.commons.lang3.Validate;

import br.jus.stf.core.framework.domaindrivendesign.EntitySupport;
import br.jus.stf.core.shared.documento.TipoDocumentoId;

/**
 * Classe de domínio Tipo de Modelo.
 * 
 * @author Tomas.Godoi
 *
 */
@javax.persistence.Entity
@Table(name = "TIPO_DOCUMENTO", schema = "DOCUMENTS")
public class TipoDocumento extends EntitySupport<TipoDocumento, TipoDocumentoId> {

	@EmbeddedId
	private TipoDocumentoId id;

	@Column(name = "DSC_TIPO_DOCUMENTO", nullable = false)
	private String descricao;

	TipoDocumento() {
		// Construtor default.
	}

	/**
	 * @param id
	 * @param descricao
	 */
	public TipoDocumento(final TipoDocumentoId id, final String descricao) {
		Validate.notNull(id, "Identificador requerido.");
		Validate.notBlank(descricao, "Descrição requerida.");

		this.id = id;
		this.descricao = descricao;
	}

	/**
	 * @return
	 */
	public String descricao() {
		return descricao;
	}
	
	@Override
	public TipoDocumentoId identity() {
		return id;
	}

}
