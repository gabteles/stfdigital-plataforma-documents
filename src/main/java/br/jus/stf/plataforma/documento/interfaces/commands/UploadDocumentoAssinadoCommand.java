package br.jus.stf.plataforma.documento.interfaces.commands;

import org.springframework.web.multipart.MultipartFile;

import br.jus.stf.plataforma.documento.interfaces.validators.SignedDocument;

public class UploadDocumentoAssinadoCommand {

	@SignedDocument
	private MultipartFile file;

	public UploadDocumentoAssinadoCommand() {
		
	}
	
	public UploadDocumentoAssinadoCommand(MultipartFile file) {
		this.file = file;
	}
	
	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
}
