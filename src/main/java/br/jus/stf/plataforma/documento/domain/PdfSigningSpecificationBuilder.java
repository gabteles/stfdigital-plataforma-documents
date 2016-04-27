package br.jus.stf.plataforma.documento.domain;

import br.jus.stf.plataforma.documento.domain.model.signature.HashType;
import br.jus.stf.plataforma.documento.domain.model.signature.SigningSpecification;

public interface PdfSigningSpecificationBuilder {

	public interface SpecBuilder {

		public SigningSpecification build();

	}

	public interface PKCS7SpecBuilder extends SpecBuilder {

		public PKCS7SpecBuilder reason(String reason);

		public PKCS7SpecBuilder hashAlgorithm(HashType hashType);

		@Override
		public SigningSpecification build();

	}

	public PKCS7SpecBuilder pkcs7Dettached();

}
