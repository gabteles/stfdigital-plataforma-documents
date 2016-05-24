package br.jus.stf.plataforma.documento.application.command;

import com.wordnik.swagger.annotations.ApiModel;

/**
 * Command para conclusão da edição de um documento.
 * 
 * @author Tomas.Godoi
 *
 */
@ApiModel(value = "Contém as informações para a conclusão da edição de um documento")
public class ConcluirEdicaoDocumento {

	private String numeroEdicao;

	private Long documentoId;

	private byte[] conteudo;

	public ConcluirEdicaoDocumento(String numeroEdicao, Long documentoId, byte[] conteudo) {
		this.numeroEdicao = numeroEdicao;
		this.documentoId = documentoId;
		this.conteudo = conteudo;
	}

	public String getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(String numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public Long getDocumentoId() {
		return documentoId;
	}

	public void setDocumentoId(Long documentoId) {
		this.documentoId = documentoId;
	}

	public byte[] getConteudo() {
		return conteudo;
	}

	public void setConteudo(byte[] conteudo) {
		this.conteudo = conteudo;
	}

}
