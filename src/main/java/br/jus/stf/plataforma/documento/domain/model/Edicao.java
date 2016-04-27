package br.jus.stf.plataforma.documento.domain.model;

import org.apache.commons.lang3.Validate;

import br.jus.stf.core.framework.domaindrivendesign.ValueObjectSupport;

public class Edicao extends ValueObjectSupport<Edicao> {

	private String numero;
	private boolean ativo;
	
	public Edicao(final String numero) {
		Validate.notBlank(numero, "edicao.numero.required");
		
		this.numero = numero;
	}
	
	public void ativar() {
		this.ativo = true;
	}
	
	public String numero() {
		return numero;
	}
	
	public boolean ativo() {
		return ativo;
	}

}
