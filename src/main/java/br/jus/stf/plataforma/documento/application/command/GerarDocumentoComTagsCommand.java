package br.jus.stf.plataforma.documento.application.command;

import java.util.List;

import org.elasticsearch.common.lang3.Validate;

import com.sun.istack.internal.NotNull;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel("Comando para gerar documento com substituição de tags")
public class GerarDocumentoComTagsCommand {

	@ApiModelProperty("Id do documento")
	@NotNull
	private Long documentoId;

	@ApiModelProperty("Lista de substituições a serem realizadas")
	private List<SubstituicaoTag> substituicoes;

	public GerarDocumentoComTagsCommand() {

	}

	public GerarDocumentoComTagsCommand(final Long documentoId, final List<SubstituicaoTag> substituicoes) {
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

	public List<SubstituicaoTag> getSubstituicoes() {
		return substituicoes;
	}

	public void setSubstituicoes(List<SubstituicaoTag> substituicoes) {
		this.substituicoes = substituicoes;
	}

}
