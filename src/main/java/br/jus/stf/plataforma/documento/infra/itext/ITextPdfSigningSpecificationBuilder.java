package br.jus.stf.plataforma.documento.infra.itext;

import org.springframework.stereotype.Component;

import br.jus.stf.plataforma.documento.domain.PdfSigningSpecificationBuilder;
import br.jus.stf.plataforma.documento.domain.model.signature.HashType;
import br.jus.stf.plataforma.documento.domain.model.signature.PdfSigningSpecification;
import br.jus.stf.plataforma.documento.domain.model.signature.PdfSigningStrategy;
import br.jus.stf.plataforma.documento.domain.model.signature.SigningSpecification;

@Component
public class ITextPdfSigningSpecificationBuilder implements PdfSigningSpecificationBuilder {

	public class ITextPKCS7SpecBuilder implements PKCS7SpecBuilder {

		private String reason;
		private HashType hashType;

		@Override
		public PKCS7SpecBuilder reason(String reason) {
			this.reason = reason;
			return this;
		}

		@Override
		public PKCS7SpecBuilder hashAlgorithm(HashType hashType) {
			this.hashType = hashType;
			return this;
		}

		@Override
		public SigningSpecification build() {
			PdfSigningStrategy strategy = new ITextPdfSigningStrategy(new PKCS7DettachedITextPdfSignatureFinisher());
			return new PdfSigningSpecification(strategy, reason, hashType);
		}

	}

	private PKCS7SpecBuilder pkcs7SpecBuilder = new ITextPKCS7SpecBuilder();

	@Override
	public PKCS7SpecBuilder pkcs7Dettached() {
		return pkcs7SpecBuilder;
	}

}
