package br.jus.stf.plataforma.documento.infra.configuration;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Fornece mecanismo para registro de providers e recuperação do provider padrão
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
	 * Retorna o provider padrão.
	 * 
	 * @return provider padrão
	 */
	public static String provider() {
		return PROVIDER;
	}

}
