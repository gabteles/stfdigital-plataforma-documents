package br.jus.stf.plataforma.documento.application.command;

import org.apache.commons.lang3.Validate;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * 
 * @author Tomas.Godoi
 *
 */
@ApiModel("Representa substituição de tag para geração de texto.")
public class SubstituicaoTagTexto {

	@ApiModelProperty("Nome da tag")
	private String nome;

	@ApiModelProperty("Valor da tag")
	private String valor;

	public SubstituicaoTagTexto() {

	}

	public SubstituicaoTagTexto(final String nome, final String valor) {
		Validate.notBlank(nome);
		Validate.notBlank(nome);

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
