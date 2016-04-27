package br.jus.stf.plataforma.documento.infra.pki;

import java.io.InputStream;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import br.jus.stf.plataforma.documento.domain.model.pki.Pki;
import br.jus.stf.plataforma.documento.domain.model.pki.PkiId;
import br.jus.stf.plataforma.documento.domain.model.pki.ReadOnlyPki;

public class PlataformaPki implements Pki {

	private static PlataformaPki singleton;

	private static final String KEYSTORE_PATH = "/certification/pkis/icp-plataforma/private/keystore.p12";

	private CustomKeyStore rootStore;
	private List<CustomKeyStore> intermediateStores;

	private ReadOnlyPki pki;

	private PlataformaPki() {
		InputStream is = getClass().getResourceAsStream(KEYSTORE_PATH);
		try {
			List<CustomKeyStore> stores = PkiUtil.pkiKeystoreToCustomKeyStores(is, "changeit".toCharArray());
			rootStore = stores.get(0);
			intermediateStores = stores.subList(1, stores.size());
			pki = new ReadOnlyPki(new PkiId("ICP_PLATAFORMA"), Arrays.asList(rootStore.certificate()),
					intermediateStores.stream().map(s -> s.certificate()).collect(Collectors.toList()));
		} catch (Exception e) {
			throw new RuntimeException("Erro ao criar Plataforma Pki", e);
		}
	}

	public static PlataformaPki instance() {
		if (singleton == null) {
			synchronized (PlataformaPki.class) {
				singleton = new PlataformaPki();
			}
		}
		return singleton;
	}

	@Override
	public boolean belongsToPki(X509Certificate certificate) {
		return pki.belongsToPki(certificate);
	}

	@Override
	public X509Certificate[] certificateChainOf(X509Certificate certificate) {
		return pki.certificateChainOf(certificate);
	}

	@Override
	public List<X509Certificate> getTrustedAnchors() {
		return Arrays.asList(rootStore.certificate());
	}

}
