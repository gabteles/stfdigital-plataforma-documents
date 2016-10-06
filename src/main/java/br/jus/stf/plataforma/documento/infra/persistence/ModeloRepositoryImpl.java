package br.jus.stf.plataforma.documento.infra.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import br.jus.stf.core.shared.documento.ModeloId;
import br.jus.stf.core.shared.documento.TipoDocumentoId;
import br.jus.stf.plataforma.documento.domain.model.Modelo;
import br.jus.stf.plataforma.documento.domain.model.ModeloRepository;

/**
 * Implementação do repositório de Modelo.
 * 
 * @author Tomas.Godoi
 *
 */
@Repository
public class ModeloRepositoryImpl extends SimpleJpaRepository<Modelo, ModeloId> implements ModeloRepository {

	private EntityManager entityManager;

	@Autowired
	public ModeloRepositoryImpl(EntityManager entityManager) {
		super(Modelo.class, entityManager);
		this.entityManager = entityManager;
	}

	@Override
	public ModeloId nextId() {
		Query query = entityManager.createNativeQuery("SELECT documents.seq_modelo.NEXTVAL FROM DUAL");
		return new ModeloId(((Number) query.getSingleResult()).longValue());
	}

	@Override
	public List<Modelo> findByTiposDocumento(List<TipoDocumentoId> tiposDocumento) {
		TypedQuery<Modelo> query = entityManager.createQuery("FROM Modelo m WHERE m.tipoDocumento.id IN (:tiposDocumento)", Modelo.class);
		query.setParameter("tiposDocumento", tiposDocumento);
		return query.getResultList();
	}
}
