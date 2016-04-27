package br.jus.stf.plataforma.documento.interfaces.commands;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;


public class DeleteTemporarioCommand {

	@NotEmpty
	private List<String> files;

	public DeleteTemporarioCommand() {
	}
	
	public DeleteTemporarioCommand(List<String> files) {
		this.files = files;
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}
}
