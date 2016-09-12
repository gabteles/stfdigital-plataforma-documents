package br.jus.stf.plataforma.documento.domain.model;

import java.util.Optional;

import org.apache.commons.lang3.Validate;

import br.jus.stf.core.framework.domaindrivendesign.ValueObjectSupport;

/**
 * 
 * @author Tomas.Godoi
 *
 */
public class SubstituicaoTag extends ValueObjectSupport<SubstituicaoTag> {

	private String nome;

	private String valor;

	/**
	 * @param nome
	 * @param valor
	 */
	public SubstituicaoTag(final String nome, final String valor) {
		Validate.notBlank(nome, "Nome requerido.");

		this.nome = nome;
		this.valor = Optional.ofNullable(valor).orElse("");
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
	public String valor() {
		return valor;
	}

}
