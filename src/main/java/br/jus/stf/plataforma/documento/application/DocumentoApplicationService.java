package br.jus.stf.plataforma.documento.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.core.shared.documento.DocumentoTemporarioId;
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
@Service
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
	 * Salva os documentos tempor�rios no reposit�rio
	 * 
	 * @param documentosTemporarios
	 * @return
	 */
	public Map<String, DocumentoId> salvarDocumentos(List<DocumentoTemporarioId> documentosTemporarios) {
		return documentosTemporarios.stream()
				.collect(Collectors.toMap(docTemp -> docTemp.toString(), docTemp -> salvar(docTemp)));
	}

	/**
	 * @param documentoTemporario
	 * @return
	 */
	public String salvarDocumentoTemporario(DocumentoTemporario documentoTemporario) {
		if (documentoTemporario.tamanho() > TAMANHO_MAXIMO) {
			throw new IllegalArgumentException("O tamanho do arquivo excede o limite m�ximo de 10MB.");
		}
		
		return documentoRepository.storeTemp(documentoTemporario);
	}

	public void apagarDocumentosTemporarios(List<String> documentoTemporarioIds) {
		documentoTemporarioIds.stream()
			.forEach(tempId -> documentoRepository.removeTemp(tempId));
	}

	/**
	 * Divide um documento.
	 * 
	 * @param id
	 * @param intervalos
	 * @return
	 */
	public List<DocumentoId> dividirDocumento(DocumentoId id, List<Range<Integer>> intervalos) {
		List<DocumentoTemporarioId> documentosTemporarios = documentoService.dividirDocumento(id, intervalos);
		return salvar(documentosTemporarios);
	}
	
	/**
	 * Divide um documento completamente.
	 * 
	 * @param id
	 * @param intervalos
	 * @return
	 */
	public List<DocumentoId> dividirDocumentoCompletamente(DocumentoId id, List<Range<Integer>> intervalos) {
		List<DocumentoTemporarioId> documentosTemporarios = documentoService.dividirDocumentoCompletamente(id, intervalos);
		return salvar(documentosTemporarios);
	}

	/**
	 * Une os documentos especificados em um s�.
	 * 
	 * @param documentos
	 * @return
	 */
	public DocumentoId unirDocumentos(List<DocumentoId> documentos) {
		List<ConteudoDocumento> conteudos = documentos.stream().map(d -> documentoRepository.download(d)).collect(Collectors.toList());
		Long tamanhoNovoDocumento = 1L;
		
		for (ConteudoDocumento conteudo : conteudos) {
			tamanhoNovoDocumento += conteudo.tamanho();
		}
		
		if (tamanhoNovoDocumento > TAMANHO_MAXIMO) {
			throw new IllegalArgumentException("O tamanho do arquivo excede o limite m�ximo de 10MB.");
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

	public void concluirEdicaoDocumento(String numeroEdicao, DocumentoId documentoId, DocumentoTemporario documentoTemporario) {
		Documento documento = documentoRepository.findOne(documentoId);
		conteudoDocumentoRepository.deleteConteudo(documento.numeroConteudo());
		String numeroConteudo = conteudoDocumentoRepository.save(documentoId, documentoTemporario);
		documento.alterarConteudo(numeroConteudo, documentoService.contarPaginas(documentoTemporario));
		documentoRepository.save(documento);
		controladorEdicaoDocumento.excluirEdicao(numeroEdicao);
	}
	
	public DocumentoId gerarDocumentoComTags(DocumentoId documentoId, List<SubstituicaoTag> substituicoes) {
		ConteudoDocumento conteudo = documentoRepository.download(documentoId);
		DocumentoTemporario documentoTemporario = documentoService.preencherTags(substituicoes, conteudo);

		String documentoTemporarioId = salvarDocumentoTemporario(documentoTemporario);
		return salvar(new DocumentoTemporarioId(documentoTemporarioId));
	}

	public DocumentoId gerarDocumentoFinal(DocumentoId documentoId) {
		DocumentoTemporario documentoTemporario = conversorDocumentoService.converterDocumentoFinal(documentoId);
		String documentoTemporarioId = salvarDocumentoTemporario(documentoTemporario);
		return salvar(new DocumentoTemporarioId(documentoTemporarioId));
	}
	
}
