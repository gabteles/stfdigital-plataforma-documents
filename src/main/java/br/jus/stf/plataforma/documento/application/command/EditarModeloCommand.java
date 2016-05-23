package br.jus.stf.plataforma.documento.application.command;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Command para edição de um modelo existente.
 * 
 * @author Tomas.Godoi
 *
 */
@ApiModel(value = "Contém as informações para a edição de um modelo existente")
public class EditarModeloCommand {

	@NotNull
	@ApiModelProperty(value = "Identificador do modelo a ser editado", required = true)
	private Long id;

	@NotNull
	@ApiModelProperty(value = "Identificador do novo tipo de documento", required = true)
	private Long tipoDocumento;

	@NotBlank
	@ApiModelProperty(value = "Novo nome do modelo", required = true)
	private String nome;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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