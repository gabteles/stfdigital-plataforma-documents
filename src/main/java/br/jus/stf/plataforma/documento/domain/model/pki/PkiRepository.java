package br.jus.stf.plataforma.documento.domain.model.pki;

public interface PkiRepository {

	Pki findOne(PkiId id);

	Pki[] findAll(PkiId[] ids);
	
}
