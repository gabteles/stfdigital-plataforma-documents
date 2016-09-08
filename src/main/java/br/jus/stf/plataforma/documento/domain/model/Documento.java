package br.jus.stf.plataforma.documento.domain.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Table;

import org.apache.commons.lang3.Validate;

import br.jus.stf.core.framework.domaindrivendesign.EntitySupport;
import br.jus.stf.core.shared.documento.DocumentoId;

/**
 * @author Rafael.Alencar
 * @version 1.0
 * @created 14-ago-2015 18:34:02
 */
@javax.persistence.Entity
@Table(name = "DOCUMENTO", schema = "DOCUMENTS")
public class Documento extends EntitySupport<Documento, DocumentoId> {

	@EmbeddedId
	private DocumentoId id;
	
	@Column(name = "NUM_CONTEUDO", nullable = false)	
	private String numeroConteudo;
	
	@Column(name = "TAMANHO", nullable = false)	
	private Long tamanho;
	
	@Column(name = "QTD_PAGINAS")
	private Integer quantidadePaginas;
	
	Documento() {
		// Construtor default.
	}

	/**
	 * @param id
	 * @param numeroConteudo
	 * @param quantidadePaginas
	 * @param tamanho
	 */
	public Documento(final DocumentoId id, final String numeroConteudo, Integer quantidadePaginas, Long tamanho) {
		Validate.notNull(id, "documento.id.required");
		Validate.notBlank(numeroConteudo, "documento.numeroConteudo.required");
		Validate.notNull(tamanho, "documento.tamanho.required");
		
		this.id = id;
		this.numeroConteudo = numeroConteudo;
		this.quantidadePaginas = quantidadePaginas;
		this.tamanho = tamanho;
	}

	/**
	 * @param numeroConteudo
	 * @param quantidadePaginas
	 */
	public void alterarConteudo(String numeroConteudo, Integer quantidadePaginas) {
		Validate.notBlank(numeroConteudo, "documento.numeroConteudo.required");
		
		this.numeroConteudo = numeroConteudo;
		this.quantidadePaginas = quantidadePaginas;
	}
	
	/**
	 * @return
	 */
	public String numeroConteudo(){
		return numeroConteudo;
	}
	
	/**
	 * @return
	 */
	public Long tamanho() {
		return tamanho;
	}

	/**
	 * @return
	 */
	public Integer quantidadePaginas() {
		return quantidadePaginas;
	}
	
	@Override
	public DocumentoId identity() {
		return id;
	}
	
}