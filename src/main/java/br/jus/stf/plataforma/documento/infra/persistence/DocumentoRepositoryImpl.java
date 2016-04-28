package br.jus.stf.plataforma.documento.infra.persistence;

import java.math.BigInteger;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.plataforma.documento.domain.model.ConteudoDocumento;
import br.jus.stf.plataforma.documento.domain.model.Documento;
import br.jus.stf.plataforma.documento.domain.model.DocumentoRepository;
import br.jus.stf.plataforma.documento.domain.model.DocumentoTemporario;

/**
 * Repositório de documentos.
 * 
 * O armazenamento dos documentos temporários é delegado para DocumentoTempoRepository.
 * 
 * O armazenamento do conteúdo dos documentos é delegado para alguma implementação de 
 * ConteudoDocumentoRepository.
 * 
 * @author Lucas Rodrigues
 */
@Repository
public class DocumentoRepositoryImpl extends SimpleJpaRepository<Documento, DocumentoId> implements DocumentoRepository {

	private EntityManager entityManager;
	
	@Autowired
	private DocumentoTempRepository documentoTempRepository;
	
	@Autowired
	private ConteudoDocumentoRepository conteudoDocumentoRepository;
	
	@Autowired
	public DocumentoRepositoryImpl(EntityManager entityManager) {
		super(Documento.class, entityManager);
		this.entityManager = entityManager;
	}
	
	@Override
	public ConteudoDocumento download(DocumentoId documentoId) {
		Documento documento = Optional.ofNullable(super.findOne(documentoId))
					.orElseThrow(IllegalArgumentException::new);
		return conteudoDocumentoRepository.downloadConteudo(documento.numeroConteudo());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Documento save(Documento documento) {
		Documento documentoPersistido = super.save(documento);

		entityManager.flush();
		return documentoPersistido;
	}

	@Override
	public void delete(Documento documento) {
		String numeroConteudo = documento.numeroConteudo();
		
		super.delete(documento);
		conteudoDocumentoRepository.deleteConteudo(numeroConteudo);
	}
	
	@Override
	public String storeTemp(DocumentoTemporario documentoTemporario) {
		return documentoTempRepository.storeTemp(documentoTemporario);
	}
	
	@Override
	public void removeTemp(String tempId) {
		documentoTempRepository.removeTemp(tempId);
	}

	@Override
	public DocumentoId nextId() {
		javax.persistence.Query query = entityManager.createNativeQuery("SELECT CORPORATIVO.SEQ_DOCUMENTO.NEXTVAL FROM DUAL");
		Long sequencial = ((BigInteger) query.getSingleResult()).longValue();
		return new DocumentoId(sequencial);
	}
	
	@Override
	public Documento findOne(DocumentoId documentoId){
		return super.findOne(documentoId);
	}

}
