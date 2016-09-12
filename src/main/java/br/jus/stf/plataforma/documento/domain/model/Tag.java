package br.jus.stf.plataforma.documento.domain.model;

import org.apache.commons.lang3.Validate;

import br.jus.stf.core.framework.domaindrivendesign.ValueObjectSupport;

/**
 * @author Tomas.Godoi
 *
 */
public class Tag extends ValueObjectSupport<Tag> {

	private String nome;

	/**
	 * @param nome
	 */
	public Tag(final String nome) {
		Validate.notBlank(nome, "Nome requerido.");
		
		this.nome = nome;
	}

	/**
	 * @return
	 */
	public String nome() {
		return nome;
	}

}
