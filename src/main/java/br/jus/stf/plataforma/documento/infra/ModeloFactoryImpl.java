package br.jus.stf.plataforma.documento.infra;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.core.shared.documento.DocumentoTemporarioId;
import br.jus.stf.core.shared.documento.DocxMultipartFile;
import br.jus.stf.core.shared.documento.TipoDocumentoId;
import br.jus.stf.plataforma.documento.application.DocumentoApplicationService;
import br.jus.stf.plataforma.documento.application.command.SalvarDocumentosCommand;
import br.jus.stf.plataforma.documento.application.command.UploadDocumentoCommand;
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
	private ResourceLoader resourceLoader;
	
	@Autowired
	private DocumentoApplicationService documentoApplicationService;

	@Override
	public DocumentoId criarDocumentoModeloPadrao(TipoDocumentoId tipoDocumento, String nome) {
		Resource resource = resourceLoader.getResource("classpath:templates/modelo/padrao-brasao.docx");
		DocumentoTemporarioId documentoTemporarioId;
		try {
			documentoTemporarioId = upload(tipoDocumento.toLong() + nome + ".docx",
			        IOUtils.toByteArray(resource.getInputStream()));
			return salvar(documentoTemporarioId);
		} catch (IOException e) {
			throw new RuntimeException("Erro ao gerar documento do modelo", e);
		}
	}

	private DocumentoTemporarioId upload(String nome, byte[] documento) {
		MultipartFile file = new DocxMultipartFile(nome, documento);
		return new DocumentoTemporarioId(documentoApplicationService.handle(new UploadDocumentoCommand(file)));
	}
	
	private DocumentoId salvar(DocumentoTemporarioId documentoTemporario) {
		SalvarDocumentosCommand command = new SalvarDocumentosCommand();
		command.setIdsDocumentosTemporarios(Arrays.asList(documentoTemporario.toString()));
		Map<String, DocumentoId> documentosSalvos = documentoApplicationService.handle(command);
		return documentosSalvos.values().iterator().next();
	}

}
