package br.jus.stf.plataforma.documento.interfaces.commands;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Command para uni�o de documentos.
 * 
 * @author Tomas.Godoi
 *
 */
@ApiModel("Cont�m as informa��es necess�rias unir documentos")
public class UnirDocumentosCommand {

	@ApiModelProperty("Documentos a serem unidos")
	@NotEmpty
	private List<Long> idsDocumentos;

	public UnirDocumentosCommand() {
		
	}
	
	public UnirDocumentosCommand(List<Long> idsDocumentos) {
		this.idsDocumentos = idsDocumentos;
	}

	public List<Long> getIdsDocumentos() {
		return idsDocumentos;
	}

	public void setIdsDocumentos(List<Long> idsDocumentos) {
		this.idsDocumentos = idsDocumentos;
	}
}
