package br.jus.stf.plataforma.documento.infra.pki;

import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public final class PkiUtil {

	private PkiUtil() {

	}

	public static List<CustomKeyStore> pkiKeystoreToCustomKeyStores(InputStream input, char[] password) throws Exception {
		KeyStore pkcs12Store = KeyStore.getInstance("PKCS12");
		pkcs12Store.load(input, password);
		Enumeration<String> aliases = pkcs12Store.aliases();

		SortedMap<String, CustomKeyStore> stores = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				if ("root".equals(o1) && !"root".equals(o2)) {
					return -1;
				} else if (!o1.startsWith("root") && o2.startsWith("root")) {
					return 1;
				} else if (o1.startsWith("ca") && o2.startsWith("ca")) {
					int n1 = Integer.parseInt(o1.substring(2));
					int n2 = Integer.parseInt(o2.substring(2));
					if (n1 > n2) {
						return 1;
					} else if (n1 < n2) {
						return -1;
					} else {
						return 0;
					}
				} else {
					return 0;
				}
			}

		});

		while (aliases.hasMoreElements()) {
			String alias = aliases.nextElement();
			PrivateKey key = (PrivateKey) pkcs12Store.getKey(alias, password);
			X509Certificate certificate = (X509Certificate) pkcs12Store.getCertificate(alias);
			CustomKeyStore store = new CustomKeyStore(new KeyPair(certificate.getPublicKey(), key), certificate);
			stores.put(alias, store);
		}

		return new ArrayList<>(stores.values());
	}

	public static CustomKeyStore userKeystoreToCustomKeyStores(InputStream input, char[] password) throws Exception {
		KeyStore pkcs12Store = KeyStore.getInstance("PKCS12");
		pkcs12Store.load(input, password);

		String alias = "user";
		
		PrivateKey key = (PrivateKey) pkcs12Store.getKey(alias, password);
		X509Certificate certificate = (X509Certificate) pkcs12Store.getCertificate(alias);
		return new CustomKeyStore(new KeyPair(certificate.getPublicKey(), key), certificate);
	}
	
}
