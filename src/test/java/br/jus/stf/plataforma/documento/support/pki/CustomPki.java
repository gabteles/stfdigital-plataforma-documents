package br.jus.stf.plataforma.documento.support.pki;

import org.apache.commons.lang.Validate;

import br.jus.stf.plataforma.documento.infra.pki.CustomKeyStore;


public class CustomPki {

	private CustomKeyStore rootCA;
	private CustomKeyStore[] intermediateCAs;

	public CustomPki(CustomKeyStore rootCA, CustomKeyStore... intermediateCAs) {
		Validate.notNull(rootCA, "RootCA é obrigatório");
		Validate.notNull(intermediateCAs, "IntermediateCA é obrigatório");

		this.rootCA = rootCA;
		this.intermediateCAs = intermediateCAs;
	}

	public CustomKeyStore rootCA() {
		return rootCA;
	}

	public CustomKeyStore[] intermediateCAs() {
		return intermediateCAs;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("### Root CA ###");
		sb.append(rootCA.certificate().toString()).append("\n\n");

		for (CustomKeyStore ca : intermediateCAs) {
			sb.append("### Intermediate CA ###");
			sb.append(ca.certificate().toString()).append("\n\n");
		}

		return sb.toString();
	}

}
