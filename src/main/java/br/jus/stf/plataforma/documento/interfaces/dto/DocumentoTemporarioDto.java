package br.jus.stf.plataforma.documento.interfaces.dto;

/**
 * @author Lucas.Rodrigues
 *
 */
public class DocumentoTemporarioDto {

	private String tempId;
	private Long documentoId;
	
	public DocumentoTemporarioDto() {
		
	}
	
	public DocumentoTemporarioDto(String tempId, Long documentoId) {
		this.setTempId(tempId);
		this.setDocumentoId(documentoId);
	}

	/**
	 * @return the tempId
	 */
	public String getTempId() {
		return tempId;
	}

	/**
	 * @param tempId the tempId to set
	 */
	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

	/**
	 * @return the documentoId
	 */
	public Long getDocumentoId() {
		return documentoId;
	}

	/**
	 * @param documentoId the documentoId to set
	 */
	public void setDocumentoId(Long documentoId) {
		this.documentoId = documentoId;
	}
	
}