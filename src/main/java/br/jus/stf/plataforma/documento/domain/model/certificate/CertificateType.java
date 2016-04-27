package br.jus.stf.plataforma.documento.domain.model.certificate;

public enum CertificateType {

	R("Root"),

	A("Certificate Authority"),

	F("Final Entity");

	private String description;

	private CertificateType(String desc) {
		this.description = desc;
	}

	public String description() {
		return description;
	}

}
