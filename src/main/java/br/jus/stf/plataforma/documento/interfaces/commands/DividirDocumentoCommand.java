package br.jus.stf.plataforma.documento.interfaces.commands;

import javax.validation.constraints.NotNull;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Command para divisão de um documento
 * 
 * @author Tomas.Godoi
 *
 */
@ApiModel("Contém as informações necessárias para dividir um documento")
public class DividirDocumentoCommand {

	@ApiModelProperty("Id do documento a ser dividido")
	@NotNull
	private Long documentoId;
	@ApiModelProperty("Página inicial a ser dividida")
	@NotNull
	private Integer paginaInicial;
	@ApiModelProperty("Página final a ser dividida")
	@NotNull
	private Integer paginaFinal;

	public DividirDocumentoCommand() {
		
	}
	
	public DividirDocumentoCommand(Long documentoId, Integer paginaInicial, Integer paginaFinal) {
		this.documentoId = documentoId;
		this.paginaInicial = paginaInicial;
		this.paginaFinal = paginaFinal;
	}

	public Long getDocumentoId() {
		return documentoId;
	}

	public void setDocumentoId(Long documentoId) {
		this.documentoId = documentoId;
	}

	public Integer getPaginaInicial() {
		return paginaInicial;
	}

	public void setPaginaInicial(Integer paginaInicial) {
		this.paginaInicial = paginaInicial;
	}

	public Integer getPaginaFinal() {
		return paginaFinal;
	}

	public void setPaginaFinal(Integer paginaFinal) {
		this.paginaFinal = paginaFinal;
	}
}
