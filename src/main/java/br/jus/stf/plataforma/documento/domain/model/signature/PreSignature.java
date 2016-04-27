package br.jus.stf.plataforma.documento.domain.model.signature;

import org.apache.commons.lang3.Validate;

import br.jus.stf.core.framework.domaindrivendesign.ValueObjectSupport;

/**
 * Representa um resumo de dados prontos para serem assinados.
 * 
 * @author Tomas.Godoi
 *
 */
public class PreSignature extends ValueObjectSupport<PreSignature> {

	private AuthenticatedAttributes auth;
	private HashToSign hash;
	private HashType hashType;

	public PreSignature(AuthenticatedAttributes auth, HashToSign hash, HashType hashType) {
		Validate.notNull(auth);
		Validate.notNull(hash);
		Validate.notNull(hashType);

		this.auth = auth;
		this.hash = hash;
		this.hashType = hashType;
	}

	public AuthenticatedAttributes auth() {
		return auth;
	}

	public HashToSign hash() {
		return hash;
	}

	public HashType hashType() {
		return hashType;
	}

}
