package br.jus.stf.plataforma.documento.domain.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Table;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import br.jus.stf.core.framework.domaindrivendesign.Entity;
import br.jus.stf.core.shared.documento.DocumentoId;

/**
 * @author Rafael.Alencar
 * @version 1.0
 * @created 14-ago-2015 18:34:02
 */
@javax.persistence.Entity
@Table(name = "DOCUMENTO", schema = "CORPORATIVO")
public class Documento implements Entity<Documento, DocumentoId> {

	@EmbeddedId
	private DocumentoId id;
	
	@Column(name = "NUM_CONTEUDO", nullable = false)	
	private String numeroConteudo;
	
	@Column(name = "TAMANHO", nullable = false)	
	private Long tamanho;
	
	@Column(name = "QTD_PAGINAS")
	private Integer quantidadePaginas;
	
	Documento() {

	}

	public Documento(final DocumentoId id, final String numeroConteudo, Integer quantidadePaginas, Long tamanho) {
		Validate.notNull(id, "documento.id.required");
		Validate.notBlank(numeroConteudo, "documento.numeroConteudo.required");
		Validate.notNull(tamanho, "documento.tamanho.required");
		
		this.id = id;
		this.numeroConteudo = numeroConteudo;
		this.quantidadePaginas = quantidadePaginas;
		this.tamanho = tamanho;
	}

	public void alterarConteudo(String numeroConteudo, Integer quantidadePaginas) {
		Validate.notBlank(numeroConteudo, "documento.numeroConteudo.required");
		
		this.numeroConteudo = numeroConteudo;
		this.quantidadePaginas = quantidadePaginas;
	}
	
	@Override
	public DocumentoId identity() {
		return id;
	}

	public String numeroConteudo(){
		return numeroConteudo;
	}
	
	public Long tamanho() {
		return tamanho;
	}

	public Integer quantidadePaginas() {
		return quantidadePaginas;
	}
	
	@Override
	public int hashCode(){
		return new HashCodeBuilder().append(id).toHashCode();
	}
	
	@Override
	public boolean equals(final Object o){
		if (this == o) {
			return true;
		}
		
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
	
		Documento other = (Documento) o;
		return sameIdentityAs(other);
	}
	
	@Override
	public boolean sameIdentityAs(final Documento other) {
		return other != null && this.id.sameValueAs(other.id);
	}
	
}