package br.jus.stf.plataforma.documento.infra.pki;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.jus.stf.plataforma.documento.domain.model.certificate.CertificateUtils;
import br.jus.stf.plataforma.documento.domain.model.pki.Pki;
import br.jus.stf.plataforma.documento.domain.model.validation.CertificateValidation;
import br.jus.stf.plataforma.documento.domain.model.validation.CertificateValidationException;
import br.jus.stf.plataforma.documento.domain.model.validation.CertificateValidator;
import br.jus.stf.plataforma.documento.infra.configuration.CryptoProvider;

public class BouncyCastlePkiCertificateValidator implements CertificateValidator {

	private Pki pki;

	public BouncyCastlePkiCertificateValidator(Pki pki) {
		this.pki = pki;
	}

	@Override
	public CertificateValidation validateNow(X509Certificate certificate) {
		// TODO Recuperar as CRLs de agora.
		return validateThenWithCRLs(certificate, null, Collections.emptyList());
	}

	@Override
	public CertificateValidation validateThen(X509Certificate cert, Calendar date) {
		// TODO Recuperar as CRLs do período que inclui date.
		return validateThenWithCRLs(cert, date, Collections.emptyList());
	}

	@Override
	public CertificateValidation validateThenWithCRLs(X509Certificate cert, Calendar date, Collection<X509CRL> crls) {
		CertificateValidation validation = new CertificateValidation();
		try {
			verifyNotExpired(cert, date);
			if (!pki.belongsToPki(cert)) {
				validation.associateChain(new X509Certificate[] { cert });
				throw new CertificateValidationException(
						"Certificado não pertence a nehuma infraestrutura de chaves públicas confiável.");
			}
			X509Certificate[] chain = pki.certificateChainOf(cert);
			validation.associateChain(chain);

			validateCertificateSignatureEnabled(cert);

			validateCertificateChain(Arrays.asList(chain));

		} catch (CertificateValidationException e) {
			validation.appendValidationError(e.getMessage());
		}
		return validation;
	}

	private void verifyNotExpired(X509Certificate certificate, Calendar date) throws CertificateValidationException {
		try {
			if (date != null) {
				certificate.checkValidity(date.getTime());
			} else {
				certificate.checkValidity();
			}
		} catch (CertificateExpiredException e) {
			throw new CertificateValidationException("O certificado se encontra expirado.", e);
		} catch (CertificateNotYetValidException e) {
			throw new CertificateValidationException("O certificado ainda não é válido.", e);
		}
	}

	private void validateCertificateSignatureEnabled(X509Certificate certificate)
			throws CertificateValidationException {
		// Verifica se o certificado está habilitado para assinatura digital.
		if (certificate.getKeyUsage() == null || (!certificate.getKeyUsage()[0] && !certificate.getKeyUsage()[1])) {
			throw new CertificateValidationException("Certificado não é habilitado para assinatura: "
					+ CertificateUtils.subjectNameAsString(certificate));
		}
	}

	private void validateCertificateChain(List<X509Certificate> chain) throws CertificateValidationException {
		try {
			// constroi os parametros de validacao
			PKIXParameters pkixParameters = buildPkixParameters(chain);

			CertPath certPath = CertificateFactory.getInstance("X.509", CryptoProvider.provider())
					.generateCertPath(chain);
			CertPathValidator validador = CertPathValidator.getInstance("PKIX", CryptoProvider.provider());
			// executa a validacao com os parametros informados
			validador.validate(certPath, pkixParameters);
		} catch (CertificateException e) {
			throw new CertificateValidationException("Erro do certificado.");
		} catch (NoSuchProviderException e) {
			throw new RuntimeException("Provider não encontrado ao validar o certificado.", e);
		} catch (CertPathValidatorException e) {
			throw new CertificateValidationException("Falha na validação da cadeia do certificado.");
		} catch (InvalidAlgorithmParameterException e) {
			throw new RuntimeException("Erro algoritmo invalido ao validar o certificado.");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Erro algoritmo invalido ao validar o certificado.");
		}
	}

	/**
	 * Constrói um objeto PKIXParameters, utilizado na validação do certificado.
	 * Indica que será utilizada a lista de revogação. Insere o trustedAnchors,
	 * com as autoridades certificadoras confiáveis.
	 * 
	 * @param crl
	 * @return
	 * @throws InvalidAlgorithmParameterException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateValidationException
	 * @throws RaizNaoEncontradaException
	 * @throws CRLInvalidaException
	 */
	private PKIXParameters buildPkixParameters(List<X509Certificate> chain)
			throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, CertificateValidationException {

		Set<TrustAnchor> trustedAnchors = new HashSet<TrustAnchor>();
		// recupera a lista de ACs confiaveis
		List<X509Certificate> trusted = pki.getTrustedAnchors();

		if (trusted == null || trusted.isEmpty()) {
			throw new CertificateValidationException("Nenhuma raiz confiável foi encontrada.");
		}

		for (X509Certificate c : trusted) {
			trustedAnchors.add(new TrustAnchor(c, null));
		}
		// cria o pkixParameters
		PKIXParameters pkixParameters = new PKIXParameters(trustedAnchors);

		// TODO Implementar a validação de CRL.
		pkixParameters.setRevocationEnabled(false);

		// TODO Permitir validar em uma data.

		// TODO Permitir CertPathCheckers customizados.
		// pkixParameters.addCertPathChecker(new
		// IcpBrasilCriticalExtensionsChecker());
		return pkixParameters;
	}

}
