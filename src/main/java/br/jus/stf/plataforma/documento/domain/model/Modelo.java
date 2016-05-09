package br.jus.stf.plataforma.documento.domain.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import br.jus.stf.core.framework.domaindrivendesign.Entity;
import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.core.shared.documento.ModeloId;

/**
 * Classe de domínio Modelo.
 * 
 * @author Tomas.Godoi
 *
 */
@javax.persistence.Entity
@Table(name = "MODELO", schema = "DOCUMENTO")
public class Modelo implements Entity<Modelo, ModeloId> {

	@EmbeddedId
	private ModeloId id;

	@ManyToOne
	@JoinColumn(name = "SEQ_TIPO_DOCUMENTO", nullable = false)
	private TipoModelo tipoModelo;

	@Column(name = "NOM_MODELO", nullable = false)
	private String nome;

	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "SEQ_DOCUMENTO_TEMPLATE"))
	private DocumentoId documento;

	Modelo() {

	}

	public Modelo(final ModeloId id, final TipoModelo tipoModelo, final String nome, final DocumentoId documento) {
		Validate.notNull(id, "modelo.id.required");
		Validate.notNull(nome, "modelo.tipoModelo.required");
		Validate.notBlank(nome, "modelo.nome.required");
		Validate.notNull(documento, "modelo.documento.required");

		this.id = id;
		this.nome = nome;
		this.tipoModelo = tipoModelo;
		this.documento = documento;
	}

	@Override
	public ModeloId identity() {
		return id;
	}

	public String nome() {
		return nome;
	}

	public TipoModelo tipoModelo() {
		return tipoModelo;
	}

	public DocumentoId documento() {
		return documento;
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

		Modelo other = (Modelo) obj;
		return sameIdentityAs(other);
	}

	@Override
	public boolean sameIdentityAs(Modelo other) {
		return other != null && this.id.sameValueAs(other.id);
	}

}
