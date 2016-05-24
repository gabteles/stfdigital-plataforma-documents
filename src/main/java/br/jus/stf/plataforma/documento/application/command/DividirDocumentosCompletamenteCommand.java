package br.jus.stf.plataforma.documento.application.command;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Command para divisão de um documento
 * 
 * @author Tomas.Godoi
 *
 */
@ApiModel("Contém as informações necessárias para dividir um documento")
public class DividirDocumentosCompletamenteCommand {

	@ApiModelProperty("Id do documento a ser dividido")
	@NotNull
	private Long documentoId;

	@ApiModelProperty("Intervalos para divisão")
	@NotEmpty
	private List<Intervalo> intervalos;

	public DividirDocumentosCompletamenteCommand() {

	}

	public DividirDocumentosCompletamenteCommand(Long documentoId, List<Intervalo> intervalos) {
		this.documentoId = documentoId;
		this.intervalos = intervalos;
	}

	public Long getDocumentoId() {
		return documentoId;
	}

	public void setDocumentoId(Long documentoId) {
		this.documentoId = documentoId;
	}

	public List<Intervalo> getIntervalos() {
		return intervalos;
	}

	public void setIntervalos(List<Intervalo> intervalos) {
		this.intervalos = intervalos;
	}

}
