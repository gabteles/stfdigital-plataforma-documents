package br.jus.stf.plataforma.documento.domain.model.signature;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.Validate;

public class HashSignature {

	private byte[] signatureAsBytes;

	public HashSignature(byte[] bytes) {
		Validate.notNull(bytes);

		signatureAsBytes = bytes;
	}

	public HashSignature(String signatureAsHex) {
		Validate.notEmpty(signatureAsHex);

		try {
			signatureAsBytes = Hex.decodeHex(signatureAsHex.toCharArray());
		} catch (DecoderException e) {
			throw new RuntimeException("Erro ao decodificar assinatura em hexadecimal.", e);
		}
	}

	public String signatureAsHex() {
		return Hex.encodeHexString(signatureAsBytes);
	}

	public byte[] signatureAsBytes() {
		return signatureAsBytes;
	}

}
