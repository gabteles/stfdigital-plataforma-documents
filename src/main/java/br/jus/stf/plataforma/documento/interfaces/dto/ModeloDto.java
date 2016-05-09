package br.jus.stf.plataforma.documento.interfaces.dto;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel("Representa um Modelo")
public class ModeloDto {

	@ApiModelProperty("O id do modelo")
	private Long id;

	@ApiModelProperty("O tipo do modelo")
	private TipoModeloDto tipoModelo;

	@ApiModelProperty("O nome do modelo")
	private String nome;

	@ApiModelProperty("O id do documento do modelo")
	private Long documento;

	public ModeloDto(final Long id, final TipoModeloDto tipoModelo, final String nome, final Long documento) {
		this.id = id;
		this.tipoModelo = tipoModelo;
		this.nome = nome;
		this.documento = documento;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoModeloDto getTipoModelo() {
		return tipoModelo;
	}

	public void setTipoModelo(TipoModeloDto tipoModelo) {
		this.tipoModelo = tipoModelo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getDocumento() {
		return documento;
	}

	public void setDocumento(Long documento) {
		this.documento = documento;
	}

}
