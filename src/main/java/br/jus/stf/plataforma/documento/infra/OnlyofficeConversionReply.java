package br.jus.stf.plataforma.documento.infra;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FileResult")
public class OnlyofficeConversionReply {

	private String fileUrl;
	private String percent;
	private String endConvert;

	public String getFileUrl() {
		return fileUrl;
	}

	@XmlElement(name = "FileUrl")
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getPercent() {
		return percent;
	}

	@XmlElement(name = "Percent")
	public void setPercent(String percent) {
		this.percent = percent;
	}

	public String getEndConvert() {
		return endConvert;
	}

	@XmlElement(name = "EndConvert")
	public void setEndConvert(String endConvert) {
		this.endConvert = endConvert;
	}

}
