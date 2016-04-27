package br.jus.stf.plataforma.documento.domain.model.pki;

import org.apache.commons.lang3.Validate;

import br.jus.stf.core.framework.domaindrivendesign.ValueObjectSupport;

public class PkiId extends ValueObjectSupport<PkiId> {

	private String id;

	public PkiId(String id) {
		Validate.notEmpty(id, "pkiId.id.required");

		this.id = id;
	}

	public String id() {
		return id;
	}

}
