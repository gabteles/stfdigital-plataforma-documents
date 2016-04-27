package br.jus.stf.plataforma.documento.domain.model.signature;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.Validate;

import br.jus.stf.core.framework.domaindrivendesign.ValueObjectSupport;

public class HashToSign extends ValueObjectSupport<HashToSign> {

	private byte[] hashAsBytes;

	public HashToSign(String hashAsHex) {
		Validate.notEmpty(hashAsHex);

		try {
			this.hashAsBytes = Hex.decodeHex(hashAsHex.toCharArray());
		} catch (DecoderException e) {
			throw new RuntimeException("Erro ao decodificar hash em hexadecimal.", e);
		}
	}

	public HashToSign(byte[] hashAsBytes) {
		Validate.notNull(hashAsBytes);

		this.hashAsBytes = hashAsBytes;
	}

	public String hashAsHex() {
		return Hex.encodeHexString(hashAsBytes);
	}

	public byte[] hashAsBytes() {
		return hashAsBytes;
	}

}
