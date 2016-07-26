package br.jus.stf.plataforma.documento.support.pki;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.apache.commons.lang3.Validate;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERTaggedObject;

public class IcpBrasilDadosPessoaFisica {

	private static final ASN1ObjectIdentifier IDENTIFIER = new ASN1ObjectIdentifier("2.16.76.1.3.1");

	public static class Rg {

		private String numero;
		private String orgaoUf;

		public Rg(String numero, String orgaoUf) {
			Validate.notNull(numero, "rg.numero.required");
			Validate.notNull(orgaoUf, "rg.orgaoUf.required");

			Validate.isTrue(numero.length() <= 15, "rg.numero.length");
			Validate.isTrue(orgaoUf.length() <= 6, "rg.numero.length");

			this.numero = numero;
			this.orgaoUf = orgaoUf;
		}

		public String numero() {
			return numero;
		}

		public String orgaoUf() {
			return orgaoUf;
		}

	}

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("ddMMyyyy");

	private LocalDate dataDeNascimento;
	private String cpf;
	private String pisPasep;
	private Rg rg;

	public IcpBrasilDadosPessoaFisica(LocalDate dataDeNascimento, String cpf, String pisPasep, Rg rg) {
		Optional.ofNullable(cpf)
				.ifPresent(c -> Validate.isTrue(cpf.length() == 11, "icpDadosPessoaFisica.cpf.tamanho"));
		Optional.ofNullable(pisPasep)
				.ifPresent(p -> Validate.isTrue(p.length() == 11, "icpDadosPessoaFisica.pisPasep.tamanho"));

		this.dataDeNascimento = dataDeNascimento;
		this.cpf = cpf;
		this.pisPasep = pisPasep;
		this.rg = rg;
	}

	public ASN1ObjectIdentifier identifier() {
		return IDENTIFIER;
	}

	public ASN1Encodable asn1Object() {
		StringBuilder objStr = new StringBuilder();
		if (dataDeNascimento == null) {
			objStr.append("00000000");
		} else {
			objStr.append(FORMATTER.format(dataDeNascimento));
		}
		objStr.append(Optional.ofNullable(cpf).orElse("00000000000"));
		objStr.append(Optional.ofNullable(pisPasep).orElse("00000000000"));
		if (rg == null) {
			objStr.append("000000000000000000000");
		} else {
			objStr.append(rg.numero() + rg.orgaoUf());
		}
		return new DERTaggedObject(true, 0, new DEROctetString(objStr.toString().getBytes()));
	}

}
