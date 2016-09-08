package br.jus.stf.plataforma.documento.domain.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.Validate;

import br.jus.stf.core.framework.domaindrivendesign.EntitySupport;
import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.core.shared.documento.ModeloId;

/**
 * Classe de domínio Modelo.
 * 
 * @author Tomas.Godoi
 *
 */
@javax.persistence.Entity
@Table(name = "MODELO", schema = "DOCUMENTS")
public class Modelo extends EntitySupport<Modelo, ModeloId> {

	@EmbeddedId
	private ModeloId id;

	@ManyToOne
	@JoinColumn(name = "SEQ_TIPO_DOCUMENTO", nullable = false)
	private TipoDocumento tipoDocumento;

	@Column(name = "NOM_MODELO", nullable = false)
	private String nome;

	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "SEQ_DOCUMENTO_TEMPLATE"))
	private DocumentoId template;

	Modelo() {
		// Construtor default.
	}

	/**
	 * @param id
	 * @param tipoDocumento
	 * @param nome
	 * @param template
	 */
	public Modelo(final ModeloId id, final TipoDocumento tipoDocumento, final String nome, final DocumentoId template) {
		Validate.notNull(id, "modelo.id.required");
		Validate.notNull(tipoDocumento, "modelo.tipoDocumento.required");
		Validate.notBlank(nome, "modelo.nome.required");
		Validate.notNull(template, "modelo.template.required");

		this.id = id;
		this.nome = nome;
		this.tipoDocumento = tipoDocumento;
		this.template = template;
	}

	/**
	 * @return
	 */
	public String nome() {
		return nome;
	}

	/**
	 * @return
	 */
	public TipoDocumento tipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @return
	 */
	public DocumentoId template() {
		return template;
	}

	/**
	 * @param tipoDocumento
	 * @param nome
	 */
	public void editar(final TipoDocumento tipoDocumento, final String nome) {
		Validate.notNull(tipoDocumento, "modelo.tipoDocumento.required");
		Validate.notBlank(nome, "modelo.nome.required");
		
		this.tipoDocumento = tipoDocumento;
		this.nome = nome;
	}
	
	@Override
	public ModeloId identity() {
		return id;
	}

}
