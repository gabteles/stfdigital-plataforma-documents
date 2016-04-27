package br.jus.stf.plataforma.documento.domain.model.pki;

import java.security.cert.X509Certificate;
import java.util.List;

public interface Pki {

	public boolean belongsToPki(X509Certificate certificate);

	public X509Certificate[] certificateChainOf(X509Certificate certificate);

	public List<X509Certificate> getTrustedAnchors();

}
