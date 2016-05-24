package br.jus.stf.plataforma.documento.interfaces;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.core.shared.documento.DocumentoTemporarioId;
import br.jus.stf.plataforma.documento.application.DocumentoApplicationService;
import br.jus.stf.plataforma.documento.application.command.SubstituicaoTagDocumento;
import br.jus.stf.plataforma.documento.domain.DocumentoService;
import br.jus.stf.plataforma.documento.domain.model.ConteudoDocumento;
import br.jus.stf.plataforma.documento.domain.model.Documento;
import br.jus.stf.plataforma.documento.domain.model.DocumentoRepository;
import br.jus.stf.plataforma.documento.domain.model.DocumentoTemporario;
import br.jus.stf.plataforma.documento.domain.model.SubstituicaoTag;
import br.jus.stf.plataforma.documento.domain.model.Tag;
import br.jus.stf.plataforma.documento.interfaces.dto.DocumentoDto;
import br.jus.stf.plataforma.documento.interfaces.dto.DocumentoDtoAssembler;
import br.jus.stf.plataforma.documento.interfaces.dto.DocumentoTemporarioDto;
import br.jus.stf.plataforma.documento.interfaces.dto.DocumentoTemporarioDtoAssembler;

/**
 * @author Rodrigo Barreiros
 * 
 * @since 1.0.0
 * @since 25.09.2015
 */
@Component
public class DocumentoServiceFacade {
	
	@Autowired
	private DocumentoApplicationService documentoApplicationService;

	@Autowired
	private DocumentoTemporarioDtoAssembler documentoTemporarioDtoAssembler;
	
	@Autowired
	private DocumentoDtoAssembler documentoDtoAssembler;
	
	@Autowired
	private DocumentoRepository documentoRepository;

	@Autowired
	private DocumentoService documentoService;

	public List<DocumentoTemporarioDto> salvarDocumentos(List<DocumentoTemporarioId> documentosTemporarios) {
		return documentoApplicationService.salvarDocumentos(documentosTemporarios).entrySet().stream()
				.map(entry -> documentoTemporarioDtoAssembler.toDto(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList());
	}

	public String salvarDocumentoTemporario(MultipartFile file) {
		DocumentoTemporario documentoTemporario = new DocumentoTemporario(file);
		return documentoApplicationService.salvarDocumentoTemporario(documentoTemporario);
	}

	public ConteudoDocumento pesquisaDocumento(Long documentoId) {
		return documentoRepository.download(new DocumentoId(documentoId));
	}
	
	public DocumentoDto consultar(Long documentoId) {
		Documento documento = documentoRepository.findOne(new DocumentoId(documentoId));
		return documentoDtoAssembler.toDo(documento.identity().toLong(), documento.tamanho(), documento.quantidadePaginas());
	}

	public void apagarDocumentosTemporarios(List<String> files) {
		documentoApplicationService.apagarDocumentosTemporarios(files);
	}

	public List<DocumentoId> dividirDocumento(DocumentoId id, List<Range<Integer>> intervalos) {
		return documentoApplicationService.dividirDocumentoCompletamente(id, intervalos);
	}

	public DocumentoId unirDocumentos(List<DocumentoId> documentos) {
		return documentoApplicationService.unirDocumentos(documentos);
	}

	public List<Tag> extrairTags(DocumentoId documentoId) {
		ConteudoDocumento conteudoDocumento = documentoRepository.download(documentoId);
		return documentoService.extrairTags(conteudoDocumento);
	}

	public Long gerarDocumentoComTags(Long documentoId, List<SubstituicaoTagDocumento> substituicoes) {
		List<SubstituicaoTag> substituicoesTag = substituicoes.stream()
		        .map(std -> new SubstituicaoTag(std.getNome(), std.getValor())).collect(Collectors.toList());
		DocumentoId documentoGeradoId = documentoApplicationService.gerarDocumentoComTags(new DocumentoId(documentoId), substituicoesTag);
		return documentoGeradoId.toLong();
	}

	public Long gerarDocumentoFinal(Long documento) {
		return documentoApplicationService.gerarDocumentoFinal(new DocumentoId(documento)).toLong();
	}

}