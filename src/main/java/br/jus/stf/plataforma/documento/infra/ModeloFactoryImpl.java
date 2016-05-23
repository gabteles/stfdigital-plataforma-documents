package br.jus.stf.plataforma.documento.infra;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.core.shared.documento.DocumentoTemporarioId;
import br.jus.stf.core.shared.documento.TipoDocumentoId;
import br.jus.stf.plataforma.documento.domain.DocumentoAdapter;
import br.jus.stf.plataforma.documento.domain.ModeloFactory;

/**
 * Implementação do Modelo Factory
 * 
 * @author Tomas.Godoi
 *
 */
@Component
public class ModeloFactoryImpl implements ModeloFactory {

	@Autowired
	private DocumentoAdapter documentoAdapter;

	@Autowired
	private ResourceLoader resourceLoader;

	@Override
	public DocumentoId criarDocumentoModeloPadrao(TipoDocumentoId tipoDocumento, String nome) {
		Resource resource = resourceLoader.getResource("classpath:templates/modelo/padrao-brasao.docx");
		DocumentoTemporarioId documentoTemporarioId;
		try {
			documentoTemporarioId = documentoAdapter.upload(tipoDocumento.toLong() + nome + ".docx",
			        IOUtils.toByteArray(resource.getInputStream()));
			return documentoAdapter.salvar(documentoTemporarioId);
		} catch (IOException e) {
			throw new RuntimeException("Erro ao gerar documento do modelo", e);
		}
	}

}
