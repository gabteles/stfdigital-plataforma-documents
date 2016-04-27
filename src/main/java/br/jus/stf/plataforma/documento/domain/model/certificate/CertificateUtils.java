package br.jus.stf.plataforma.documento.domain.model.certificate;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class CertificateUtils {

	private CertificateUtils() {

	}

	public static X509Certificate bytesToCertificate(byte[] certificateBytes) {
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			return (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certificateBytes));
		} catch (CertificateException e) {
			throw new RuntimeException("Erro ao converter bytes para Certificate", e);
		}
	}

	/**
	 * Retorna uma string descritiva do certificado.
	 * 
	 * @param certificate Certificado
	 * @return subjectName como String
	 */
	public static String subjectNameAsString(X509Certificate certificate) {
		StringBuilder certStringBuilder = new StringBuilder();
		if (certificate.getSubjectX500Principal() != null) {
			certStringBuilder.append(certificate.getSubjectX500Principal().getName());
		}
		return certStringBuilder.toString();
	}

}
