package br.jus.stf.plataforma.documento.domain.model;

import org.apache.commons.lang3.Validate;

import br.jus.stf.core.framework.domaindrivendesign.ValueObjectSupport;

/**
 * @author Tomas.Godoi
 *
 */
public class Edicao extends ValueObjectSupport<Edicao> {

	private String numero;
	private boolean ativo;
	
	/**
	 * @param numero
	 */
	public Edicao(final String numero) {
		Validate.notBlank(numero, "Número requerido.");
		
		this.numero = numero;
	}
	
	/**
	 * Marca a edição como ativa.
	 */
	public void ativar() {
		this.ativo = true;
	}
	
	/**
	 * @return
	 */
	public String numero() {
		return numero;
	}
	
	/**
	 * @return
	 */
	public boolean ativo() {
		return ativo;
	}

}
