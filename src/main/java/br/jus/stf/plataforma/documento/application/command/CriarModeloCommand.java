package br.jus.stf.plataforma.documento.application.command;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Command para criação de um novo modelo.
 * 
 * @author Tomas.Godoi
 *
 */
@ApiModel(value = "Contém as informações para a criação de um novo modelo")
public class CriarModeloCommand {

	@NotNull
	@ApiModelProperty(value = "Identificador do tipo de documento", required = true)
	private Long tipoDocumento;

	@NotBlank
	@ApiModelProperty(value = "Nome do modelo", required = true)
	private String nome;

	public Long getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(Long tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
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