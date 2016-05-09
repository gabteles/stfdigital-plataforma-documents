package br.jus.stf.plataforma.documento.domain.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Table;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import br.jus.stf.core.framework.domaindrivendesign.Entity;
import br.jus.stf.core.shared.documento.TipoDocumentoId;

/**
 * Classe de domínio Tipo de Modelo.
 * 
 * @author Tomas.Godoi
 *
 */
@javax.persistence.Entity
@Table(name = "TIPO_MODELO", schema = "DOCUMENTO")
public class TipoModelo implements Entity<TipoModelo, TipoDocumentoId> {

	@EmbeddedId
	private TipoDocumentoId id;

	@Column(name = "DSC_TIPO_MODELO", nullable = false)
	private String descricao;

	TipoModelo() {

	}

	public TipoModelo(final TipoDocumentoId id, final String descricao) {
		Validate.notNull(id, "tipoModelo.id.required");
		Validate.notBlank(descricao, "tipoModelo.descricao.required");

		this.id = id;
		this.descricao = descricao;
	}

	@Override
	public TipoDocumentoId identity() {
		return id;
	}

	public String descricao() {
		return descricao;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		TipoModelo other = (TipoModelo) obj;
		return sameIdentityAs(other);
	}

	@Override
	public boolean sameIdentityAs(TipoModelo other) {
		return other != null && this.id.sameValueAs(other.id);
	}

}
