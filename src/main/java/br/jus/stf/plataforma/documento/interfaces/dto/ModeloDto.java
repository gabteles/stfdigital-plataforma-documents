package br.jus.stf.plataforma.documento.interfaces.dto;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel("Representa um Modelo")
public class ModeloDto {

	@ApiModelProperty("O id do modelo")
	private Long id;

	@ApiModelProperty("O tipo do documento")
	private TipoDocumentoDto tipoDocumento;

	@ApiModelProperty("O nome do modelo")
	private String nome;

	@ApiModelProperty("O id do documento do modelo")
	private Long documento;

	public ModeloDto(final Long id, final TipoDocumentoDto tipoDocumento, final String nome, final Long documento) {
		this.id = id;
		this.tipoDocumento = tipoDocumento;
		this.nome = nome;
		this.documento = documento;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoDocumentoDto getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumentoDto tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
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
