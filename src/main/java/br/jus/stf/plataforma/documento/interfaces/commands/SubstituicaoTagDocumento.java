package br.jus.stf.plataforma.documento.interfaces.commands;

import org.apache.commons.lang3.Validate;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel("Representa a substituição de uma tag em documento.")
public class SubstituicaoTagDocumento {

	@ApiModelProperty("Nome da tag")
	private String nome;

	@ApiModelProperty("Valor da tag")
	private String valor;

	public SubstituicaoTagDocumento() {

	}

	public SubstituicaoTagDocumento(final String nome, final String valor) {
		Validate.notBlank(nome);
		Validate.notNull(nome);
		
		this.nome = nome;
		this.valor = valor;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
