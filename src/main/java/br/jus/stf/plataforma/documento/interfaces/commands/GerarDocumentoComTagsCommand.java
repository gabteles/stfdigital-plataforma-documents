package br.jus.stf.plataforma.documento.interfaces.commands;

import java.util.List;

import org.elasticsearch.common.lang3.Validate;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel("Comando para gerar documento com substituição de tags")
public class GerarDocumentoComTagsCommand {

	@ApiModelProperty("Id do documento")
	private Long documentoId;

	@ApiModelProperty("Lista de substituições a serem realizadas")
	private List<SubstituicaoTagDocumento> substituicoes;

	public GerarDocumentoComTagsCommand() {

	}

	public GerarDocumentoComTagsCommand(final Long documentoId, final List<SubstituicaoTagDocumento> substituicoes) {
		Validate.notNull(documentoId);
		Validate.notNull(substituicoes);

		this.documentoId = documentoId;
		this.substituicoes = substituicoes;
	}

	public Long getDocumentoId() {
		return documentoId;
	}

	public void setDocumentoId(Long documentoId) {
		this.documentoId = documentoId;
	}

	public List<SubstituicaoTagDocumento> getSubstituicoes() {
		return substituicoes;
	}

	public void setSubstituicoes(List<SubstituicaoTagDocumento> substituicoes) {
		this.substituicoes = substituicoes;
	}

}
