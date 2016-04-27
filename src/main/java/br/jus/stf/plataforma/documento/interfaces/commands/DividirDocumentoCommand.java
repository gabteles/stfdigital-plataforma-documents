package br.jus.stf.plataforma.documento.interfaces.commands;

import javax.validation.constraints.NotNull;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Command para divis�o de um documento
 * 
 * @author Tomas.Godoi
 *
 */
@ApiModel("Cont�m as informa��es necess�rias para dividir um documento")
public class DividirDocumentoCommand {

	@ApiModelProperty("Id do documento a ser dividido")
	@NotNull
	private Long documentoId;
	@ApiModelProperty("P�gina inicial a ser dividida")
	@NotNull
	private Integer paginaInicial;
	@ApiModelProperty("P�gina final a ser dividida")
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
