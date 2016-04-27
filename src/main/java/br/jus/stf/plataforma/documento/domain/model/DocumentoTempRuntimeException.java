package br.jus.stf.plataforma.documento.domain.model;

/**
 * @author Anderson.Araujo
 * 
 * @since 16.11.2015
 *
 */
public class DocumentoTempRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public DocumentoTempRuntimeException(){
		super();
	}
	
	public DocumentoTempRuntimeException(Throwable mensagem){
		super(mensagem);
	}

}
