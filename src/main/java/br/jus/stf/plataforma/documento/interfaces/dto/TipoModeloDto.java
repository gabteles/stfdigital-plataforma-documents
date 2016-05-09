package br.jus.stf.plataforma.documento.interfaces.dto;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel("Representa um tipo de modelo")
public class TipoModeloDto {

	@ApiModelProperty("O id do tipo de modelo")
	private Long id;

	@ApiModelProperty("A descrição do tipo de modelo")
	private String descricao;

	public TipoModeloDto(Long id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
