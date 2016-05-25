package br.jus.stf.plataforma.documento.infra.persistence;

import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import br.jus.stf.core.shared.documento.TextoId;
import br.jus.stf.plataforma.documento.domain.model.Texto;
import br.jus.stf.plataforma.documento.domain.model.TextoRepository;

/**
 * Repositório de textos.
 * 
 * @author Tomas.Godoi
 *
 */
@Repository
public class TextoRepositoryImpl extends SimpleJpaRepository<Texto, TextoId> implements TextoRepository {

	private EntityManager entityManager;
	
	@Autowired
	public TextoRepositoryImpl(EntityManager entityManager) {
		super(Texto.class, entityManager);
		this.entityManager = entityManager;
	}


	@Override
	public TextoId nextId() {
		Query query = entityManager.createNativeQuery("SELECT documento.seq_texto.NEXTVAL FROM DUAL");
		Long sequencial = ((BigInteger) query.getSingleResult()).longValue();
		return new TextoId(sequencial);
	}

}
