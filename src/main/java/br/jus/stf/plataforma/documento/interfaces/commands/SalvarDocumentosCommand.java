package br.jus.stf.plataforma.documento.interfaces.commands;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Comando para persistir documentos temporários
 * 
 * @author Lucas.Rodrigues
 *
 */
public class SalvarDocumentosCommand {
	
	@NotEmpty
	private List<String> idsDocumentosTemporarios;

	/**
	 * @return the documentos
	 */
	public List<String> getIdsDocumentosTemporarios() {
		return idsDocumentosTemporarios;
	}

	/**
	 * @param documentos the documentos to set
	 */
	public void setIdsDocumentosTemporarios(List<String> documentos) {
		this.idsDocumentosTemporarios = documentos;
	}

}
