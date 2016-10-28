package br.jus.stf.plataforma.documento.interfaces;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.plataforma.documento.application.DocumentoApplicationService;
import br.jus.stf.plataforma.documento.application.command.DeleteTemporarioCommand;
import br.jus.stf.plataforma.documento.application.command.DividirDocumentosCompletamenteCommand;
import br.jus.stf.plataforma.documento.application.command.GerarDocumentoComTagsCommand;
import br.jus.stf.plataforma.documento.application.command.SalvarDocumentosCommand;
import br.jus.stf.plataforma.documento.application.command.UnirDocumentosCommand;
import br.jus.stf.plataforma.documento.application.command.UploadDocumentoAssinadoCommand;
import br.jus.stf.plataforma.documento.application.command.UploadDocumentoCommand;
import br.jus.stf.plataforma.documento.domain.model.ConteudoDocumento;
import br.jus.stf.plataforma.documento.domain.model.Documento;
import br.jus.stf.plataforma.documento.domain.model.DocumentoRepository;
import br.jus.stf.plataforma.documento.interfaces.dto.DocumentoDto;
import br.jus.stf.plataforma.documento.interfaces.dto.DocumentoDtoAssembler;
import br.jus.stf.plataforma.documento.interfaces.dto.DocumentoTemporarioDto;
import br.jus.stf.plataforma.documento.interfaces.dto.DocumentoTemporarioDtoAssembler;

/**
 * Api REST para salvar e recuperar documentos
 * 
 * @author Lucas Rodrigues
 */
@RestController
@RequestMapping("/api/documentos")
public class DocumentoRestResource {

    private static final String DOCUMENTO_INVALIDO_PATTERN = "Documento Inválido: %S";

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private DocumentoApplicationService documentoApplicationService;

    @Autowired
    private DocumentoTemporarioDtoAssembler documentoTemporarioDtoAssembler;

    @Autowired
    private DocumentoDtoAssembler documentoDtoAssembler;

    /**
     * @param command Comando com os dados dos documento temporário.
     * @param result Resultado das validações.
     * @return Dtos de documentos temporários salvos.
     */
    @ApiOperation("Salva os documentos temporários.")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public List<DocumentoTemporarioDto> salvar(@Valid @RequestBody SalvarDocumentosCommand command,
            BindingResult result) {
        isValid(result);

        return documentoApplicationService.handle(command).entrySet().stream()
                .map(entry -> documentoTemporarioDtoAssembler.toDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * @param documentoId Identificador do documento procurado.
     * @return Stream com o conteúdo do documento.
     * @throws IOException
     */
    @ApiOperation("Recupera o conteúdo de um documento do repositório.")
    @RequestMapping(value = "/{documentoId}/conteudo", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> recuperar(@PathVariable("documentoId") Long documentoId)
            throws IOException {
        ConteudoDocumento documento = documentoRepository.download(new DocumentoId(documentoId));
        InputStreamResource is = new InputStreamResource(documento.stream());
        HttpHeaders headers = createResponseHeaders(documento.tamanho());

        return new ResponseEntity<>(is, headers, HttpStatus.OK);
    }

    /**
     * Define os headers para o pdf
     * 
     * @param tamanho Tamanho do arquivo.
     * @return HttpHeaders para o arquivo pdf.
     */
    private HttpHeaders createResponseHeaders(Long tamanho) {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentLength(tamanho);
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return headers;
    }

    /**
     * @param documentoId Identificador do documento procurado.
     * @return Dto com os dados de um documento.
     * @throws IOException
     */
    @ApiOperation("Retorna os dados de um documento.")
    @RequestMapping(value = "/{documentoId}", method = RequestMethod.GET)
    public DocumentoDto consultar(@PathVariable("documentoId") Long documentoId) throws IOException {
        Documento documento = documentoRepository.findOne(new DocumentoId(documentoId));

        return documentoDtoAssembler.toDo(documento);
    }

    /**
     * @param command Comando com o documento temporário.
     * @return Identificador atribuído ao documento temporário.
     */
    @ApiOperation("Envia um documento para armazenamento temporário e retorna o indentificador.")
    @RequestMapping(value = "/conteudo", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String upload(UploadDocumentoCommand command) {
        return documentoApplicationService.handle(command);
    }

    /**
     * @param command Comando com o documento temporário assinado digitalmente.
     * @param result Resultado das validações.
     * @return Identificador atribuído ao documento temporário.
     */
    @ApiOperation("Envia um documento assinado para armazenamento temporário e retorna o indentificador.")
    @RequestMapping(value = "/conteudo/assinado", method = RequestMethod.POST)
    @ApiResponses(value = { @ApiResponse(code = 400, message = "O arquivo enviado não foi assinado digitalmente.") })
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadAssinado(@Valid UploadDocumentoAssinadoCommand command, BindingResult result) {
        isValid(result);

        return documentoApplicationService.handle(command);
    }

    /**
     * @param command Comando com os documentos temporários para exclusão.
     * @param result Resultado das validações.
     */
    @ApiOperation("Exclui um documento temporário.")
    @RequestMapping(value = "/conteudo/exclusao", method = RequestMethod.POST)
    public void deleteTemp(@Valid @RequestBody DeleteTemporarioCommand command, BindingResult result) {
        isValid(result);
        documentoApplicationService.handle(command);
    }

    /**
     * @param documentoId Identificador do documento que será dividido.
     * @param command Commando com os dados para divisão do documento.
     * @param result Resultado das validações.
     * @return Identificadores dos documentos gerados pela divisão.
     */
    @ApiOperation("Divide um documento.")
    @RequestMapping(value = "/{documentoId}/divisao", method = RequestMethod.POST)
    public List<Long> dividirDocumento(@PathVariable("documentoId") Long documentoId,
            @Valid @RequestBody DividirDocumentosCompletamenteCommand command, BindingResult result) {
        isValid(documentoId, command.getDocumentoId(), result);

        return documentoApplicationService.handle(command).stream()
                .map(d -> d.toLong())
                .collect(Collectors.toList());
    }

    /**
     * @param command Comando com os dados para divisão do documento.
     * @param result Resultado das validações.
     * @return Identificador do documento gerado pela união.
     */
    @ApiOperation("Une documentos.")
    @RequestMapping(value = "/uniao", method = RequestMethod.POST)
    public Long unirDocumentos(@Valid @RequestBody UnirDocumentosCommand command, BindingResult result) {
        isValid(result);

        return documentoApplicationService.handle(command).toLong();
    }

    /**
     * @param documentoId Identificador do documento que terá as tags substituídas pelos valores informados.
     * @param command Comando com os valores que substituirão as tags.
     * @return Identificador do documento gerado.
     */
    @ApiOperation("Gera um documento com as tags substituídas pelos valores informados.")
    @RequestMapping(value = "/{documentoId}/documento")
    public Long gerarDocumentoComTags(@PathVariable("documentoId") Long documentoId,
            @Valid GerarDocumentoComTagsCommand command, BindingResult binding) {
        isValid(documentoId, command.getDocumentoId(), binding);

        DocumentoId documentoGeradoId = documentoApplicationService.handle(command);

        return documentoGeradoId.toLong();
    }

    private static void isValid(Long documentoIdPath, Long documentoIdCommand, BindingResult binding) {
        isValid(binding);

        if (!documentoIdPath.equals(documentoIdCommand)) {
            throw new IllegalArgumentException(message(
                    Arrays.asList(
                            new ObjectError("Documento", "Identificadores do comando incompatíveis."))));
        }
    }

    private static void isValid(BindingResult binding) {
        if (binding.hasErrors()) {
            throw new IllegalArgumentException(message(binding.getAllErrors()));
        }
    }

    private static String message(List<ObjectError> errors) {
        return String.format(DOCUMENTO_INVALIDO_PATTERN, errors);
    }

}
