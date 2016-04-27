package br.jus.stf.plataforma.documento.interfaces.dto;

import br.jus.stf.plataforma.documento.domain.model.signature.PreSignature;

public class PreSignatureDto {

	private String data;
	private String hash;
	private String hashType;
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getHashType() {
		return hashType;
	}

	public void setHashType(String hashType) {
		this.hashType = hashType;
	}

	public static PreSignatureDto from(PreSignature preSignature) {
		PreSignatureDto dto = new PreSignatureDto();
		dto.setData(preSignature.auth().authAsHex());
		dto.setHash(preSignature.hash().hashAsHex());
		dto.setHashType(preSignature.hashType().toString());
		return dto;
	}

}
