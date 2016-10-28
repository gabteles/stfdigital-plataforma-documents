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
import br.jus.stf.plataforma.documento.application.command.ConcluirEdicaoDocumentoCommand;
import br.jus.stf.plataforma.documento.application.command.DeleteTemporarioCommand;
import br.jus.stf.plataforma.documento.application.command.DividirDocumentosCommand;
import br.jus.stf.plataforma.documento.application.command.DividirDocumentosCompletamenteCommand;
import br.jus.stf.plataforma.documento.application.command.GerarDocumentoComTagsCommand;
import br.jus.stf.plataforma.documento.application.command.SalvarDocumentosCommand;
import br.jus.stf.plataforma.documento.application.command.UnirDocumentosCommand;
import br.jus.stf.plataforma.documento.application.command.UploadDocumentoAssinadoCommand;
import br.jus.stf.plataforma.documento.application.command.UploadDocumentoCommand;
import br.jus.stf.plataforma.documento.domain.ControladorEdicaoDocumento;
import br.jus.stf.plataforma.documento.domain.DocumentoService;
import br.jus.stf.plataforma.documento.domain.model.ConteudoDocumento;
import br.jus.stf.plataforma.documento.domain.model.Documento;
import br.jus.stf.plataforma.documento.domain.model.DocumentoRepository;
import br.jus.stf.plataforma.documento.domain.model.DocumentoTemporario;
import br.jus.stf.plataforma.documento.domain.model.SubstituicaoTag;
import br.jus.stf.plataforma.documento.infra.persistence.ConteudoDocumentoRepository;

/**
 * @author Rodrigo Barreiros
 * 
 * @since 1.0.0
 * @since 25.09.2015
 */
@ApplicationService
@Transactional
public class DocumentoApplicationService {

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private DocumentoService documentoService;

    @Autowired
    private ConteudoDocumentoRepository conteudoDocumentoRepository;

    @Autowired
    private ControladorEdicaoDocumento controladorEdicaoDocumento;

    /**
     * Salva os documentos temporários no repositório
     * 
     * @param command
     * @return
     */
    @Command
    public Map<String, DocumentoId> handle(SalvarDocumentosCommand command) {
        List<DocumentoTemporarioId> documentosTemporarios = command.getIdsDocumentosTemporarios().stream()
                .map(DocumentoTemporarioId::new).collect(Collectors.toList());

        return documentosTemporarios.stream()
                .collect(Collectors.toMap(docTemp -> docTemp.toString(), documentoService::salvar));
    }

    /**
     * @param command
     * @return
     */
    @Command
    public String handle(UploadDocumentoCommand command) {
        DocumentoTemporario documentoTemporario = new DocumentoTemporario(command.getFile());
        return documentoService.salvarDocumentoTemporario(documentoTemporario);
    }

    /**
     * 
     * @param command
     * @return
     */
    @Command
    public String handle(UploadDocumentoAssinadoCommand command) {
        DocumentoTemporario documentoTemporario = new DocumentoTemporario(command.getFile());
        return documentoService.salvarDocumentoTemporario(documentoTemporario);
    }

    /**
     * @param command
     */
    @Command
    public void handle(DeleteTemporarioCommand command) {
        command.getFiles().stream()
                .forEach(documentoRepository::removeTemp);
    }

    /**
     * @param command
     * @return
     */
    @Command
    public List<DocumentoId> handle(DividirDocumentosCompletamenteCommand command) {
        List<DocumentoId> documentosDivididos = new ArrayList<>();
        List<Range<Integer>> intervalos = command.getIntervalos().stream()
                .map(i -> Range.between(i.getPaginaInicial(), i.getPaginaFinal())).collect(Collectors.toList());
        documentosDivididos
                .addAll(dividirDocumentoCompletamente(new DocumentoId(command.getDocumentoId()), intervalos));
        return documentosDivididos;
    }

    /**
     * @param command
     * @return
     */
    @Command
    public List<DocumentoId> handle(DividirDocumentosCommand command) {
        List<DocumentoId> documentosDivididos = new ArrayList<>();
        List<Range<Integer>> intervalos = command.getIntervalos().stream()
                .map(i -> Range.between(i.getPaginaInicial(), i.getPaginaFinal())).collect(Collectors.toList());
        List<DocumentoTemporarioId> documentosTemporarios =
                documentoService.dividirDocumento(new DocumentoId(command.getDocumentoId()), intervalos);
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
        List<DocumentoTemporarioId> documentosTemporarios =
                documentoService.dividirDocumentoCompletamente(id, intervalos);
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
        List<DocumentoId> documentos =
                command.getIdsDocumentos().stream().map(DocumentoId::new).collect(Collectors.toList());
        List<ConteudoDocumento> conteudos =
                documentos.stream().map(documentoRepository::download).collect(Collectors.toList());
        Long tamanhoNovoDocumento = 1L;

        for (ConteudoDocumento conteudo : conteudos) {
            tamanhoNovoDocumento += conteudo.tamanho();
        }

        if (tamanhoNovoDocumento > DocumentoTemporario.TAMANHO_MAXIMO) {
            throw new IllegalArgumentException("O tamanho do arquivo excede o limite máximo de 10MB.");
        }

        DocumentoTemporario temp = documentoService.unirConteudos(conteudos);
        DocumentoTemporarioId tempId = new DocumentoTemporarioId(documentoRepository.storeTemp(temp));

        return documentoService.salvar(tempId);
    }

    private List<DocumentoId> salvar(List<DocumentoTemporarioId> documentosTemporarios) {
        List<DocumentoId> documentosSalvos = new ArrayList<>();
        for (DocumentoTemporarioId docTempId : documentosTemporarios) {
            DocumentoId novoDocumento = documentoService.salvar(docTempId);
            documentosSalvos.add(novoDocumento);
        }
        return documentosSalvos;
    }

    /**
     * @param command
     */
    @Command
    public void handle(ConcluirEdicaoDocumentoCommand command) {
        DocumentoId documentoId = new DocumentoId(command.getDocumentoId());
        DocumentoTemporario documentoTemporario =
                new DocumentoTemporario(new DocxMultipartFile("documento.docx", command.getConteudo()));

        Documento documento = documentoRepository.findOne(documentoId);
        conteudoDocumentoRepository.deleteConteudo(documento.numeroConteudo());
        String numeroConteudo = conteudoDocumentoRepository.save(documentoId, documentoTemporario);
        documento.alterarConteudo(numeroConteudo, documentoService.contarPaginas(documentoTemporario));
        documentoRepository.save(documento);
        controladorEdicaoDocumento.excluirEdicao(command.getNumeroEdicao());
    }

    /**
     * @param command
     * @return
     */
    @Command
    public DocumentoId handle(GerarDocumentoComTagsCommand command) {
        List<SubstituicaoTag> substituicoesTag = command.getSubstituicoes().stream()
                .map(std -> new SubstituicaoTag(std.getNome(), std.getValor())).collect(Collectors.toList());
        return documentoService.gerarDocumentoTemporarioComTags(new DocumentoId(command.getDocumentoId()),
                substituicoesTag);
    }

}
