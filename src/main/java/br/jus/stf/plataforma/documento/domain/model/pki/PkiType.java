package br.jus.stf.plataforma.documento.domain.model.pki;

public enum PkiType {

	ICP_BRASIL("ICP Brasil"), ICP_PLATAFORMA("ICP Plataforma");

	private String description;

	private PkiType(String desc) {
		this.description = desc;
	}

	public PkiId id() {
		return new PkiId(name());
	}

	public String description() {
		return description;
	}

}
