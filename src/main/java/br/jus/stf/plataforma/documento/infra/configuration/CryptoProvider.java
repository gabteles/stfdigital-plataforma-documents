package br.jus.stf.plataforma.documento.infra.configuration;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Fornece mecanismo para registro de providers e recupera��o do provider padr�o
 * a ser utilizado.
 * 
 * @author Tomas.Godoi
 *
 */
public final class CryptoProvider {

	private static final String PROVIDER = "BC";

	private CryptoProvider() {

	}

	/**
	 * Carrega os providers de criptografia.
	 */
	public static void loadProviders() {
		loadBouncyCastle();
	}

	private static void loadBouncyCastle() {
		if (Security.getProperty(PROVIDER) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
	}

	/**
	 * Retorna o provider padr�o.
	 * 
	 * @return provider padr�o
	 */
	public static String provider() {
		return PROVIDER;
	}

}
