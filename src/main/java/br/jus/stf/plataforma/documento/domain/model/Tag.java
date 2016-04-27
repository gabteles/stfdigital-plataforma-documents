package br.jus.stf.plataforma.documento.domain.model;

import org.apache.commons.lang3.Validate;

import br.jus.stf.core.framework.domaindrivendesign.ValueObjectSupport;

public class Tag extends ValueObjectSupport<Tag> {

	private String nome;

	public Tag(final String nome) {
		Validate.notBlank(nome, "tag.nome.required");
		
		this.nome = nome;
	}

	public String nome() {
		return nome;
	}

}
