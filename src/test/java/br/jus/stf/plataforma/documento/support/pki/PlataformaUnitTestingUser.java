package br.jus.stf.plataforma.documento.support.pki;

import java.io.InputStream;

import br.jus.stf.plataforma.documento.infra.pki.CustomKeyStore;
import br.jus.stf.plataforma.documento.infra.pki.PkiUtil;


public class PlataformaUnitTestingUser {

	private static PlataformaUnitTestingUser singleton;

	private static final String KEYSTORE_PATH = "/certification/pkis/icp-plataforma/certs/77787705205.p12";

	private CustomKeyStore userStore;

	private PlataformaUnitTestingUser() {
		InputStream is = getClass().getResourceAsStream(KEYSTORE_PATH);
		try {
			userStore = PkiUtil.userKeystoreToCustomKeyStores(is, "changeit".toCharArray());
		} catch (Exception e) {
			throw new RuntimeException("Erro ao criar Plataforma Pki", e);
		}
	}

	public static PlataformaUnitTestingUser instance() {
		if (singleton == null) {
			synchronized (PlataformaUnitTestingUser.class) {
				singleton = new PlataformaUnitTestingUser();
			}
		}
		return singleton;
	}

	public CustomKeyStore userStore() {
		return userStore;
	}
	
}
