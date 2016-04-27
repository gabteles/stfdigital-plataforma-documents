package br.jus.stf.plataforma.documento.domain.model.signature;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.Validate;

public class AuthenticatedAttributes {

	private byte[] authAsBytes;

	public AuthenticatedAttributes(String authAsHex) {
		Validate.notEmpty(authAsHex);

		try {
			this.authAsBytes = Hex.decodeHex(authAsHex.toCharArray());
		} catch (DecoderException e) {
			throw new RuntimeException("Erro ao decodificar auth em hexadecimal.", e);
		}
	}

	public AuthenticatedAttributes(byte[] authAsBytes) {
		Validate.notNull(authAsBytes);

		this.authAsBytes = authAsBytes;
	}

	public String authAsHex() {
		return Hex.encodeHexString(authAsBytes);
	}

	public byte[] authAsBytes() {
		return authAsBytes;
	}

}
