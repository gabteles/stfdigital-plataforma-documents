package br.jus.stf.plataforma.documento.infra;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.multipart.MultipartFile;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.core.shared.documento.DocumentoTemporarioId;
import br.jus.stf.core.shared.documento.DocxMultipartFile;
import br.jus.stf.plataforma.documento.application.command.GerarDocumentoComTagsCommand;
import br.jus.stf.plataforma.documento.application.command.SalvarDocumentosCommand;
import br.jus.stf.plataforma.documento.application.command.SubstituicaoTagDocumento;
import br.jus.stf.plataforma.documento.application.command.SubstituicaoTagTexto;
import br.jus.stf.plataforma.documento.application.command.UploadDocumentoCommand;
import br.jus.stf.plataforma.documento.domain.DocumentoAdapter;
import br.jus.stf.plataforma.documento.interfaces.DocumentoRestResource;

@Component("suporteDocumentoRestAdapter")
public class DocumentoRestAdapter implements DocumentoAdapter {

	@Autowired
	private DocumentoRestResource documentoRestResource;

	@Override
	public InputStream recuperarConteudo(DocumentoId documentoId) {
		try {
			ResponseEntity<InputStreamResource> response = documentoRestResource.recuperar(documentoId.toLong());
			return response.getBody().getInputStream();
		} catch (IllegalStateException | IOException e) {
			throw new RuntimeException("Erro ao recuperar conteúdo do documento.", e);
		}
	}

	@Override
	public DocumentoId salvar(DocumentoTemporarioId documentoTemporario) {
		SalvarDocumentosCommand command = new SalvarDocumentosCommand();
		command.setIdsDocumentosTemporarios(Arrays.asList(documentoTemporario.toString()));
		LinkedHashSet<DocumentoId> docs = documentoRestResource.salvar(command, new MapBindingResult(new HashMap<>(), "errors")).stream()
		        .map(dto -> new DocumentoId(dto.getDocumentoId()))
		        .collect(Collectors.toCollection(() -> new LinkedHashSet<DocumentoId>()));
		return docs.iterator().next();
	}

	@Override
	public DocumentoTemporarioId upload(String nome, byte[] documento) {
		MultipartFile file = new DocxMultipartFile(nome, documento);
		return new DocumentoTemporarioId(documentoRestResource.upload(new UploadDocumentoCommand(file)));
	}

	@Override
	public DocumentoId gerarDocumentoComTags(DocumentoId documentoId, List<SubstituicaoTagTexto> substituicoes) {
		List<SubstituicaoTagDocumento> substituicoesDocumento = substituicoes.stream()
		        .map(stt -> new SubstituicaoTagDocumento(stt.getNome(), stt.getValor())).collect(Collectors.toList());
		Long id = documentoRestResource
		        .gerarDocumentoComTags(new GerarDocumentoComTagsCommand(documentoId.toLong(), substituicoesDocumento));
		return new DocumentoId(id);
	}

}
