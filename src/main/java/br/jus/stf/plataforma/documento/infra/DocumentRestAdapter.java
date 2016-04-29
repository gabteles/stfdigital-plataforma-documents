package br.jus.stf.plataforma.documento.infra;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.core.shared.documento.DocumentoTemporarioId;
import br.jus.stf.core.shared.documento.PDFMultipartFile;
import br.jus.stf.plataforma.documento.domain.DocumentAdapter;
import br.jus.stf.plataforma.documento.domain.PdfTempDocument;
import br.jus.stf.plataforma.documento.domain.model.Document;
import br.jus.stf.plataforma.documento.interfaces.DocumentoRestResource;
import br.jus.stf.plataforma.documento.interfaces.commands.SalvarDocumentosCommand;
import br.jus.stf.plataforma.documento.interfaces.commands.UploadDocumentoCommand;

@Component
public class DocumentRestAdapter implements DocumentAdapter {

	@Autowired
	private DocumentoRestResource docRestResource;

	@Override
	public Document retrieve(DocumentoId id) throws IOException {
		ResponseEntity<InputStreamResource> response = docRestResource.recuperar(id.toLong());
		return new PdfTempDocument(response.getBody().getInputStream());
	}

	@Override
	public DocumentoTemporarioId upload(String name, Document document) {
		try {
			MultipartFile file = new PDFMultipartFile(name, IOUtils.toByteArray(document.stream()));
			return new DocumentoTemporarioId(docRestResource.upload(new UploadDocumentoCommand(file)));
		} catch (IOException e) {
			throw new IllegalStateException("Erro ao ler documento para upload.", e);
		}

	}

	@Override
	public DocumentoId save(DocumentoTemporarioId tempDocument) {
		SalvarDocumentosCommand command = new SalvarDocumentosCommand();
		command.setIdsDocumentosTemporarios(Arrays.asList(tempDocument.toString()));
		LinkedHashSet<DocumentoId> docs = docRestResource.salvar(command, null).stream()
				.map(dto -> new DocumentoId(dto.getDocumentoId()))
				.collect(Collectors.toCollection(() -> new LinkedHashSet<DocumentoId>()));
		return docs.iterator().next();
	}

}