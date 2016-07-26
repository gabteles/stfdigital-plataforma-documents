package br.jus.stf.plataforma.documento.support.pki;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import br.jus.stf.plataforma.documento.infra.configuration.CryptoProvider;
import br.jus.stf.plataforma.documento.infra.pki.CustomKeyStore;


public class CustomPkiGenerator {

	static {
		CryptoProvider.loadProviders();
	}

	public CustomPki generateCustomPKI(String rootCN, String... intermediateCNs) throws Exception {
		CustomKeyStore rootCA = generateRootCA(rootCN);
		CustomKeyStore store = rootCA;

		List<CustomKeyStore> intermediateCAs = new ArrayList<>();

		for (String cn : intermediateCNs) {
			store = generateIntermediateCA(store, cn);
			intermediateCAs.add(store);
		}

		return new CustomPki(rootCA, intermediateCAs.toArray(new CustomKeyStore[0]));
	}

	private CustomKeyStore generateRootCA(String rootCN) throws Exception {
		KeyPair kp = generateKeyPair(4096);

		PublicKey publicKey = kp.getPublic();
		PrivateKey privateKey = kp.getPrivate();

		String issuer = "C = BR, O = STF, OU = STF DIGITAL, CN = " + rootCN;
		String subject = issuer; // Auto assinado.
		BigInteger serial = BigInteger.valueOf(1L);
		Date notBefore = new Date(System.currentTimeMillis());
		Date notAfter = new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365 * 23));

		X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(new X500Name(issuer), serial, notBefore,
				notAfter, new X500Name(subject), publicKey);

		JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();

		builder.addExtension(Extension.authorityKeyIdentifier, false, extUtils.createAuthorityKeyIdentifier(publicKey));

		builder.addExtension(Extension.subjectKeyIdentifier, false, extUtils.createSubjectKeyIdentifier(publicKey));

		builder.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));

		KeyUsage keyUsage = new KeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign);
		builder.addExtension(Extension.keyUsage, true, keyUsage);

		X509CertificateHolder holder = builder
				.build(new JcaContentSignerBuilder("SHA512WithRSA").setProvider(CryptoProvider.provider()).build(privateKey));

		X509Certificate certificate = new JcaX509CertificateConverter().setProvider(CryptoProvider.provider()).getCertificate(holder);

		return new CustomKeyStore(kp, certificate);
	}

	private CustomKeyStore generateIntermediateCA(CustomKeyStore ca, String cn) throws Exception {
		KeyPair kp = generateKeyPair(4096);

		PublicKey publicKey = kp.getPublic();

		String subject = "C = BR, O = STF, OU = STF DIGITAL, CN = " + cn;

		BigInteger serial = BigInteger.valueOf(1L);
		Date notBefore = new Date(System.currentTimeMillis());
		Date notAfter = new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365 * 10));

		X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(ca.certificate(), serial, notBefore,
				notAfter, new X500Name(subject), publicKey);

		JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();

		builder.addExtension(Extension.authorityKeyIdentifier, false,
				extUtils.createAuthorityKeyIdentifier(ca.certificate().getPublicKey()));
		builder.addExtension(Extension.subjectKeyIdentifier, false, extUtils.createSubjectKeyIdentifier(publicKey));
		builder.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));

		KeyUsage keyUsage = new KeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign);
		builder.addExtension(Extension.keyUsage, true, keyUsage);

		X509CertificateHolder holder = builder.build(
				new JcaContentSignerBuilder("SHA512WithRSA").setProvider(CryptoProvider.provider()).build(ca.keyPair().getPrivate()));

		X509Certificate certificate = new JcaX509CertificateConverter().setProvider(CryptoProvider.provider()).getCertificate(holder);

		return new CustomKeyStore(kp, certificate);
	}

	public CustomKeyStore generateFinalUser(CustomKeyStore ca, String cn, int intSerial, String email,
			IcpBrasilDadosPessoaFisica dadosPf, int validityYears) throws Exception {
		KeyPair kp = generateKeyPair(2048);

		PublicKey publicKey = kp.getPublic();

		String subject = "C = BR, O = STF, OU = STF DIGITAL, CN = " + cn;

		BigInteger serial = BigInteger.valueOf(intSerial);
		Date notBefore = new Date(System.currentTimeMillis());
		Date notAfter = new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365 * validityYears));

		X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(ca.certificate(), serial, notBefore,
				notAfter, new X500Name(subject), publicKey);

		JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();

		builder.addExtension(Extension.subjectKeyIdentifier, false, extUtils.createSubjectKeyIdentifier(publicKey));
		builder.addExtension(Extension.authorityKeyIdentifier, false,
				extUtils.createAuthorityKeyIdentifier(ca.certificate().getPublicKey()));

		ExtendedKeyUsage extKeyUsage = new ExtendedKeyUsage(new KeyPurposeId[] { KeyPurposeId.id_kp_clientAuth,
				KeyPurposeId.id_kp_emailProtection, KeyPurposeId.id_kp_smartcardlogon });
		builder.addExtension(Extension.extendedKeyUsage, false, extKeyUsage);

		KeyUsage keyUsage = new KeyUsage(
				KeyUsage.digitalSignature | KeyUsage.nonRepudiation | KeyUsage.keyEncipherment);
		builder.addExtension(Extension.keyUsage, true, keyUsage);

		List<GeneralName> genNames = new ArrayList<>();
		if (email != null) {
			genNames.add(new GeneralName(GeneralName.rfc822Name, new DERIA5String(email)));
		}

		if (dadosPf != null) {
			DERSequence extra = new DERSequence(new ASN1Encodable[] { dadosPf.identifier(), dadosPf.asn1Object() });
			genNames.add(new GeneralName(GeneralName.otherName, extra));
		}

		builder.addExtension(Extension.subjectAlternativeName, false,
				new GeneralNames(genNames.toArray(new GeneralName[0])));

		X509CertificateHolder holder = builder.build(
				new JcaContentSignerBuilder("SHA256WithRSA").setProvider(CryptoProvider.provider()).build(ca.keyPair().getPrivate()));

		X509Certificate certificate = new JcaX509CertificateConverter().setProvider(CryptoProvider.provider()).getCertificate(holder);

		return new CustomKeyStore(kp, certificate);
	}

	private KeyPair generateKeyPair(int keySize) throws Exception {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(keySize);

		KeyPair kp = kpg.generateKeyPair();
		return kp;
	}

}
