package br.jus.stf.plataforma.documento.application.command;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Comando para geração de textos da petição com substituição de tags.
 * 
 * @author Tomas.Godoi
 *
 */
@ApiModel("Comando para geração de texto")
public class GerarTextoPeticaoCommand {
	
	@ApiModelProperty("O id do modelo de texto")
	private Long modeloId;

	@ApiModelProperty("As substituições das tags")
	private List<SubstituicaoTag> substituicoes;

	public Long getModeloId() {
		return modeloId;
	}

	public void setModeloId(Long modeloId) {
		this.modeloId = modeloId;
	}

	public List<SubstituicaoTag> getSubstituicoes() {
		return substituicoes;
	}

	public void setSubstituicoes(List<SubstituicaoTag> substituicoes) {
		this.substituicoes = substituicoes;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}