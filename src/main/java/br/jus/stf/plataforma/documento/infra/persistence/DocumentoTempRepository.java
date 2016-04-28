package br.jus.stf.plataforma.documento.infra.persistence;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import br.jus.stf.core.shared.documento.DocumentoTemporarioId;
import br.jus.stf.plataforma.documento.domain.model.DocumentoTemporario;

/**
 * Repositório temporário de documentos.
 * 
 * @author Tomas.Godoi
 *
 */
@Repository
public class DocumentoTempRepository {

	private static Map<String, DocumentoTemporario> TEMP_FILES = new HashMap<String, DocumentoTemporario>();

	public String storeTemp(DocumentoTemporario documentoTemporario) {
		TEMP_FILES.put(documentoTemporario.tempId(), documentoTemporario);
		return documentoTemporario.tempId();
	}

	public DocumentoTemporario recoverTemp(DocumentoTemporarioId documentoTemporario) {
		return TEMP_FILES.get(documentoTemporario.toString());
	}

	public void removeTemp(String tempId) {
		DocumentoTemporario documentoTemporario = TEMP_FILES.get(tempId);
		documentoTemporario.delete();
		TEMP_FILES.remove(tempId);
	}

}
