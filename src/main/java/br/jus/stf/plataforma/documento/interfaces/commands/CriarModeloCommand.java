package br.jus.stf.plataforma.documento.interfaces.commands;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Contém as informações para criação de um novo modelo")
public class CriarModeloCommand {

	@NotNull
	@ApiModelProperty(value = "Identificador do tipo de modelo a ser criado", required = true)
	private Long tipoModelo;

	@NotBlank
	@ApiModelProperty(value = "Nome do modelo a ser criado", required = true)
	private String nome;

	public Long getTipoModelo() {
		return tipoModelo;
	}

	public void setTipoModelo(Long tipoModelo) {
		this.tipoModelo = tipoModelo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}