package br.jus.stf.plataforma.documento.domain.model.signature;

import br.jus.stf.core.framework.domaindrivendesign.ValueObject;

public enum HashType implements ValueObject<HashType> {

	SHA1("SHA-1"), SHA224("SHA-224"), SHA256("SHA-256"), SHA384("SHA-384"), SHA512("SHA-512");

	private String dashedName;
	
	private HashType(String dashdedName) {
		this.dashedName = dashdedName;
	}
	
	@Override
	public boolean sameValueAs(HashType other) {
		return this.equals(other);
	}

	public String dashedName() {
		return dashedName;
	}
	
}
