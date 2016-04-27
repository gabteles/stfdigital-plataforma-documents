package br.jus.stf.plataforma.documento.domain.model.pki;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompositePki implements Pki {

	private List<Pki> pkis;

	public CompositePki(Pki... pkis) {
		this.pkis = Arrays.asList(pkis);
	}

	@Override
	public boolean belongsToPki(X509Certificate certificate) {
		return pkis.stream().anyMatch(pki -> pki.belongsToPki(certificate));
	}

	@Override
	public X509Certificate[] certificateChainOf(X509Certificate certificate) {
		X509Certificate[] chain = null;
		for (Pki pki : pkis) {
			chain = pki.certificateChainOf(certificate);
			if (chain != null && chain.length > 1)
				break;
		}
		return chain;
	}

	@Override
	public List<X509Certificate> getTrustedAnchors() {
		List<X509Certificate> trustedAnchors = new ArrayList<>();
		pkis.forEach(pki -> trustedAnchors.addAll(pki.getTrustedAnchors()));
		return trustedAnchors;
	}

}
