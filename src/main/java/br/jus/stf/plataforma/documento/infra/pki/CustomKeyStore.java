package br.jus.stf.plataforma.documento.infra.pki;

import java.security.KeyPair;
import java.security.cert.X509Certificate;

import org.apache.commons.lang.Validate;

public class CustomKeyStore {

	private KeyPair keyPair;
	private X509Certificate certificate;

	public CustomKeyStore(KeyPair keyPair, X509Certificate certificate) {
		Validate.notNull(keyPair, "KeyPair é obrigatório.");
		Validate.notNull(certificate, "Certificate é obrigatório.");

		this.keyPair = keyPair;
		this.certificate = certificate;
	}

	public KeyPair keyPair() {
		return keyPair;
	}

	public X509Certificate certificate() {
		return certificate;
	}

}
