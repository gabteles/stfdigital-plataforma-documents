package br.jus.stf.plataforma.documento.application.command;

import org.springframework.web.multipart.MultipartFile;

public class UploadDocumentoCommand {

	private MultipartFile file;

	public UploadDocumentoCommand() {

	}

	public UploadDocumentoCommand(MultipartFile file) {
		this.file = file;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

}
