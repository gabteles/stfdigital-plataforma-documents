package br.jus.stf.plataforma.documento.support.generators;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import org.apache.commons.io.FileUtils;

import br.jus.stf.plataforma.documento.infra.pki.CustomKeyStore;
import br.jus.stf.plataforma.documento.support.pki.CustomPki;

public class GeneratorUtil {

	private GeneratorUtil() {

	}

	public static void storeOnDisk(CustomPki customPki, String keystorePath) throws Exception {
		KeyStore pkcs12Store = KeyStore.getInstance("PKCS12");
		pkcs12Store.load(null, null);
		pkcs12Store.setKeyEntry("root", customPki.rootCA().keyPair().getPrivate(), "changeit".toCharArray(),
				new Certificate[] { customPki.rootCA().certificate() });
		int i = 1;
		for (CustomKeyStore store : customPki.intermediateCAs()) {
			pkcs12Store.setKeyEntry("ca" + i, store.keyPair().getPrivate(), "changeit".toCharArray(),
					new Certificate[] { store.certificate() });
			i++;
		}

		OutputStream outputStream = new FileOutputStream(keystorePath);
		pkcs12Store.store(outputStream, "changeit".toCharArray());
		outputStream.flush();
		outputStream.close();
	}

	public static void storeKeystoreOnDisk(CustomKeyStore store, String keystorePath) throws Exception {
		KeyStore pkcs12Store = KeyStore.getInstance("PKCS12");
		pkcs12Store.load(null, null);
		pkcs12Store.setKeyEntry("user", store.keyPair().getPrivate(), "changeit".toCharArray(),
				new Certificate[] { store.certificate() });

		OutputStream outputStream = new FileOutputStream(keystorePath);
		pkcs12Store.store(outputStream, "changeit".toCharArray());
		outputStream.flush();
		outputStream.close();
	}

	public static void storeCertificateOnDisk(CustomKeyStore store, String certificatePath)
			throws CertificateEncodingException, IOException {
		X509Certificate certificate = store.certificate();
		FileUtils.writeByteArrayToFile(new File(certificatePath), certificate.getEncoded());
	}

	public static void storeCertificatesOnDisk(CustomPki customPki, String path)
			throws CertificateEncodingException, IOException {
		storeCertificateOnDisk(customPki.rootCA(), path + "/root.cer");
		int i = 1;
		for (CustomKeyStore store : customPki.intermediateCAs()) {
			storeCertificateOnDisk(store, path + "/ca" + i + ".cer");
			i++;
		}
	}

}
