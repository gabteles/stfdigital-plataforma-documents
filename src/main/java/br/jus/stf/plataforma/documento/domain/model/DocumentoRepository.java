package br.jus.stf.plataforma.documento.domain.model;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import br.jus.stf.core.shared.documento.DocumentoId;

/**
 * @author Rafael.Alencar
 * @version 1.0
 * @created 14-ago-2015 18:34:02
 */
@NoRepositoryBean
public interface DocumentoRepository extends Repository<Documento, DocumentoId> {
	
	/**
	 * Pesquisa um documento
	 * 
	 * @param documentoId
	 */
	public Documento findOne(DocumentoId documentoId);
	
	/**
	 * Pesquisa um documento
	 * 
	 * @param documentoId
	 * @return o stream do conte�do
	 */
	public ConteudoDocumento download(DocumentoId documentoId);
	
	/**
	 * Salva um documento
	 * 
	 * @param documento
	 */
	public Documento save(Documento documento);
	
	/**
	 * Exclui um documento
	 * 
	 * @param documento
	 */
	public void delete(Documento documento);

	/**
	 * Salva um documento tempor�rio
	 * 
	 * @param documentoTemporario
	 * @return identificacao do tempor�rio
	 */
	public String storeTemp(DocumentoTemporario documentoTemporario);
	
	/**
	 * Remove um arquivo tempor�rio
	 * 
	 * @param tempId
	 * @return
	 */
	public void removeTemp(String tempId);
	
	/**
	 * Recupera o pr�ximo id do documento
	 * 
	 * @return
	 */
	public DocumentoId nextId();

}