package br.jus.stf.plataforma.documento.application.command;

import javax.validation.constraints.NotNull;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel("Contém um intervalo de páginas")
public class Intervalo {

	@ApiModelProperty("Página inicial")
	@NotNull
	private Integer paginaInicial;
	@ApiModelProperty("Página final")
	@NotNull
	private Integer paginaFinal;

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
