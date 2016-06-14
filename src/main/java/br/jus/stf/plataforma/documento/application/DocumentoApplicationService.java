package br.jus.stf.plataforma.documento.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.Range;
import org.springframework.beans.factory.annotation.Autowired;

import br.jus.stf.core.framework.component.command.Command;
import br.jus.stf.core.framework.domaindrivendesign.ApplicationService;
import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.core.shared.documento.DocumentoTemporarioId;
import br.jus.stf.core.shared.documento.DocxMultipartFile;
import br.jus.stf.plataforma.documento.application.command.ConcluirEdicaoDocumento;
import br.jus.stf.plataforma.documento.application.command.DeleteTemporarioCommand;
import br.jus.stf.plataforma.documento.application.command.DividirDocumentosCommand;
import br.jus.stf.plataforma.documento.application.command.DividirDocumentosCompletamenteCommand;
import br.jus.stf.plataforma.documento.application.command.GerarDocumentoComTagsCommand;
import br.jus.stf.plataforma.documento.application.command.GerarDocumentoFinalCommand;
import br.jus.stf.plataforma.documento.application.command.SalvarDocumentosCommand;
import br.jus.stf.plataforma.documento.application.command.UnirDocumentosCommand;
import br.jus.stf.plataforma.documento.application.command.UploadDocumentoAssinadoCommand;
import br.jus.stf.plataforma.documento.application.command.UploadDocumentoCommand;
import br.jus.stf.plataforma.documento.domain.ControladorEdicaoDocumento;
import br.jus.stf.plataforma.documento.domain.ConversorDocumentoService;
import br.jus.stf.plataforma.documento.domain.DocumentoService;
import br.jus.stf.plataforma.documento.domain.model.ConteudoDocumento;
import br.jus.stf.plataforma.documento.domain.model.Documento;
import br.jus.stf.plataforma.documento.domain.model.DocumentoRepository;
import br.jus.stf.plataforma.documento.domain.model.DocumentoTemporario;
import br.jus.stf.plataforma.documento.domain.model.SubstituicaoTag;
import br.jus.stf.plataforma.documento.infra.persistence.ConteudoDocumentoRepository;
import br.jus.stf.plataforma.documento.infra.persistence.DocumentoTempRepository;

/**
 * @author Rodrigo Barreiros
 * 
 * @since 1.0.0
 * @since 25.09.2015
 */
@ApplicationService
@Transactional
public class DocumentoApplicationService {

	private final Long TAMANHO_MAXIMO = 10485760L;
	
	@Autowired
	private DocumentoRepository documentoRepository;
	
	@Autowired
	private DocumentoService documentoService;
	
	@Autowired
	private DocumentoTempRepository documentoTempRepository;
	
	@Autowired
	private ConteudoDocumentoRepository conteudoDocumentoRepository;
	
	@Autowired
	private ControladorEdicaoDocumento controladorEdicaoDocumento;
	
	@Autowired
	private ConversorDocumentoService conversorDocumentoService;

	/**
	 * Salva os documentos temporários no repositório
	 * 
	 * @param command
	 * @return
	 */
	@Command
	public Map<String, DocumentoId> handle(SalvarDocumentosCommand command) {
		List<DocumentoTemporarioId> documentosTemporarios = command.getIdsDocumentosTemporarios().stream().map(id -> new DocumentoTemporarioId(id)).collect(Collectors.toList());
		return documentosTemporarios.stream()
				.collect(Collectors.toMap(docTemp -> docTemp.toString(), docTemp -> salvar(docTemp)));
	}

	/**
	 * @param command
	 * @return
	 */
	@Command
	public String handle(UploadDocumentoCommand command) {
		DocumentoTemporario documentoTemporario = new DocumentoTemporario(command.getFile());
		return salvarDocumentoTemporario(documentoTemporario);
	}

	private String salvarDocumentoTemporario(DocumentoTemporario documentoTemporario) {
		if (documentoTemporario.tamanho() > TAMANHO_MAXIMO) {
			throw new IllegalArgumentException("O tamanho do arquivo excede o limite máximo de 10MB.");
		}
		
		return documentoRepository.storeTemp(documentoTemporario);
	}
	
	/**
	 * 
	 * @param command
	 * @return
	 */
	@Command
	public String handle(UploadDocumentoAssinadoCommand command) {
		DocumentoTemporario documentoTemporario = new DocumentoTemporario(command.getFile());
		return salvarDocumentoTemporario(documentoTemporario);
	}
	
	@Command
	public void handle(DeleteTemporarioCommand command) {
		command.getFiles().stream()
			.forEach(tempId -> documentoRepository.removeTemp(tempId));
	}
	
	@Command
	public List<DocumentoId> handle(DividirDocumentosCompletamenteCommand command) {
		List<DocumentoId> documentosDivididos = new ArrayList<>();
		List<Range<Integer>> intervalos = command.getIntervalos().stream().map(i -> Range.between(i.getPaginaInicial(), i.getPaginaFinal())).collect(Collectors.toList());
		documentosDivididos.addAll(dividirDocumentoCompletamente(new DocumentoId(command.getDocumentoId()), intervalos));
		return documentosDivididos;
	}
	
	@Command
	public List<DocumentoId> handle(DividirDocumentosCommand command) {
		List<DocumentoId> documentosDivididos = new ArrayList<>();
		List<Range<Integer>> intervalos = command.getIntervalos().stream().map(i -> Range.between(i.getPaginaInicial(), i.getPaginaFinal())).collect(Collectors.toList());
		List<DocumentoTemporarioId> documentosTemporarios = documentoService.dividirDocumento(new DocumentoId(command.getDocumentoId()), intervalos);
		List<DocumentoId> documentosSalvos = salvar(documentosTemporarios);
		documentosDivididos.addAll(documentosSalvos);
		return documentosDivididos;
	}
	
	/**
	 * Divide um documento completamente.
	 * 
	 * @param id
	 * @param intervalos
	 * @return
	 */
	private List<DocumentoId> dividirDocumentoCompletamente(DocumentoId id, List<Range<Integer>> intervalos) {
		List<DocumentoTemporarioId> documentosTemporarios = documentoService.dividirDocumentoCompletamente(id, intervalos);
		return salvar(documentosTemporarios);
	}

	/**
	 * Une os documentos especificados em um só.
	 * 
	 * @param command
	 * @return
	 */
	@Command
	public DocumentoId handle(UnirDocumentosCommand command) {
		List<DocumentoId> documentos = command.getIdsDocumentos().stream().map(id -> new DocumentoId(id)).collect(Collectors.toList());
		List<ConteudoDocumento> conteudos = documentos.stream().map(d -> documentoRepository.download(d)).collect(Collectors.toList());
		Long tamanhoNovoDocumento = 1L;
		
		for (ConteudoDocumento conteudo : conteudos) {
			tamanhoNovoDocumento += conteudo.tamanho();
		}
		
		if (tamanhoNovoDocumento > TAMANHO_MAXIMO) {
			throw new IllegalArgumentException("O tamanho do arquivo excede o limite máximo de 10MB.");
		}
		
		DocumentoTemporario temp = documentoService.unirConteudos(conteudos);
		DocumentoTemporarioId tempId = new DocumentoTemporarioId(documentoRepository.storeTemp(temp));
		DocumentoId novoDocumento = salvar(tempId);
		return novoDocumento;
	}

	private List<DocumentoId> salvar(List<DocumentoTemporarioId> documentosTemporarios) {
		List<DocumentoId> documentosSalvos = new ArrayList<>();
		for (DocumentoTemporarioId docTempId : documentosTemporarios) {
			DocumentoId novoDocumento = salvar(docTempId);
			documentosSalvos.add(novoDocumento);
		}
		return documentosSalvos;
	}
	
	private DocumentoId salvar(DocumentoTemporarioId docTempId) {
		DocumentoTemporario docTemp = documentoTempRepository.recoverTemp(docTempId);
		
		DocumentoId id = documentoRepository.nextId();
		
		String numeroConteudo = conteudoDocumentoRepository.save(id, docTemp);
		
		Documento documento = new Documento(id, numeroConteudo, documentoService.contarPaginas(docTemp), docTemp.tamanho());
		documento = documentoRepository.save(documento);
		
		documentoTempRepository.removeTemp(docTempId.toString());
		docTemp.delete();
		return documento.identity();
	}

	@Command
	public void handle(ConcluirEdicaoDocumento command) {
        DocumentoId documentoId = new DocumentoId(command.getDocumentoId());
		DocumentoTemporario documentoTemporario = new DocumentoTemporario(new DocxMultipartFile("documento.docx", command.getConteudo()));
        
		Documento documento = documentoRepository.findOne(documentoId);
		conteudoDocumentoRepository.deleteConteudo(documento.numeroConteudo());
		String numeroConteudo = conteudoDocumentoRepository.save(documentoId, documentoTemporario);
		documento.alterarConteudo(numeroConteudo, documentoService.contarPaginas(documentoTemporario));
		documentoRepository.save(documento);
		controladorEdicaoDocumento.excluirEdicao(command.getNumeroEdicao());
	}
	
	@Command
	public DocumentoId handle(GerarDocumentoComTagsCommand command) {
		List<SubstituicaoTag> substituicoesTag = command.getSubstituicoes().stream()
		        .map(std -> new SubstituicaoTag(std.getNome(), std.getValor())).collect(Collectors.toList());
		ConteudoDocumento conteudo = documentoRepository.download(new DocumentoId(command.getDocumentoId()));
		DocumentoTemporario documentoTemporario = documentoService.preencherTags(substituicoesTag, conteudo);

		String documentoTemporarioId = salvarDocumentoTemporario(documentoTemporario);
		return salvar(new DocumentoTemporarioId(documentoTemporarioId));
	}

	@Command
	public DocumentoId handle(GerarDocumentoFinalCommand command) {
		DocumentoTemporario documentoTemporario = conversorDocumentoService.converterDocumentoFinal(new DocumentoId(command.getDocumento()));
		String documentoTemporarioId = salvarDocumentoTemporario(documentoTemporario);
		return salvar(new DocumentoTemporarioId(documentoTemporarioId));
	}
	
}
