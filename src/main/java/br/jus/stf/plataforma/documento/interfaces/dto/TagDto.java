package br.jus.stf.plataforma.documento.interfaces.dto;

import org.apache.commons.lang3.Validate;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Dto para Tag
 * 
 * @author Tomas.Godoi
 *
 */
@ApiModel("Representa um dto para Tag")
public class TagDto {

	@ApiModelProperty("Nome da tag")
	private String nome;

	public TagDto(final String nome) {
		Validate.notNull(nome);

		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
