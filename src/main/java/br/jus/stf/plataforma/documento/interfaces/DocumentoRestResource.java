package br.jus.stf.plataforma.documento.interfaces;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import br.jus.stf.core.framework.errorhandling.ValidationException;
import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.plataforma.documento.application.DocumentoApplicationService;
import br.jus.stf.plataforma.documento.application.command.DeleteTemporarioCommand;
import br.jus.stf.plataforma.documento.application.command.DividirDocumentosCompletamenteCommand;
import br.jus.stf.plataforma.documento.application.command.GerarDocumentoComTagsCommand;
import br.jus.stf.plataforma.documento.application.command.GerarDocumentoFinalCommand;
import br.jus.stf.plataforma.documento.application.command.SalvarDocumentosCommand;
import br.jus.stf.plataforma.documento.application.command.UnirDocumentosCommand;
import br.jus.stf.plataforma.documento.application.command.UploadDocumentoAssinadoCommand;
import br.jus.stf.plataforma.documento.application.command.UploadDocumentoCommand;
import br.jus.stf.plataforma.documento.domain.DocumentoService;
import br.jus.stf.plataforma.documento.domain.model.ConteudoDocumento;
import br.jus.stf.plataforma.documento.domain.model.Documento;
import br.jus.stf.plataforma.documento.domain.model.DocumentoRepository;
import br.jus.stf.plataforma.documento.domain.model.Tag;
import br.jus.stf.plataforma.documento.interfaces.dto.DocumentoDto;
import br.jus.stf.plataforma.documento.interfaces.dto.DocumentoDtoAssembler;
import br.jus.stf.plataforma.documento.interfaces.dto.DocumentoTemporarioDto;
import br.jus.stf.plataforma.documento.interfaces.dto.DocumentoTemporarioDtoAssembler;
import br.jus.stf.plataforma.documento.interfaces.dto.TagDto;

/**
 * Api REST para salvar e recuperar documentos
 * 
 * @author Lucas Rodrigues
 */
@RestController
@RequestMapping("/api/documentos")
public class DocumentoRestResource {
	
	@Autowired
	private DocumentoRepository documentoRepository;
	
	@Autowired
	private DocumentoApplicationService documentoApplicationService;

	@Autowired
	private DocumentoTemporarioDtoAssembler documentoTemporarioDtoAssembler;
	
	@Autowired
	private DocumentoDtoAssembler documentoDtoAssembler;

	@Autowired
	private DocumentoService documentoService;

	@ApiOperation("Salva os documentos temporários")
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public List<DocumentoTemporarioDto> salvar(@Valid @RequestBody SalvarDocumentosCommand command, BindingResult result) {
		if (result.hasErrors()) {
			throw new IllegalArgumentException(result.toString());
		}
		return documentoApplicationService.handle(command).entrySet().stream()
				.map(entry -> documentoTemporarioDtoAssembler.toDto(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList());
	}	
	
	@ApiOperation("Recupera o conteúdo de um documento do repositório")
	@RequestMapping(value = "/{documentoId}/conteudo", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> recuperar(@PathVariable("documentoId") Long documentoId) throws IOException {
		ConteudoDocumento documento = documentoRepository.download(new DocumentoId(documentoId));
		InputStreamResource is = new InputStreamResource(documento.stream());
		HttpHeaders headers = createResponseHeaders(documento.tamanho());
		return new ResponseEntity<InputStreamResource>(is, headers, HttpStatus.OK);
	}
	
	@ApiOperation("Retorna os dados de um documento")
	@RequestMapping(value = "/{documentoId}", method = RequestMethod.GET)
	public DocumentoDto consultar(@PathVariable("documentoId") Long documentoId) throws IOException {
		Documento documento = documentoRepository.findOne(new DocumentoId(documentoId));
		return documentoDtoAssembler.toDo(documento.identity().toLong(), documento.tamanho(), documento.quantidadePaginas());
	}
	
	@ApiOperation("Envia um documento para armazenamento temporário e retorna o indentificador")
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public String upload(UploadDocumentoCommand command) {
		return documentoApplicationService.handle(command);
	}
	
	@ApiOperation("Envia um documento assinado para armazenamento temporário e retorna o indentificador")
	@RequestMapping(value = "/upload/assinado", method = RequestMethod.POST)
	@ApiResponses(value = {@ApiResponse(code = 400, message = "O arquivo enviado não foi assinado digitalmente.")})
	@ResponseStatus(HttpStatus.CREATED)
	public String uploadAssinado(@Valid UploadDocumentoAssinadoCommand command, BindingResult result) {
		if (result.hasErrors()) {
			throw new ValidationException(result.getAllErrors());
		}
		
		return documentoApplicationService.handle(command);
	}
	
	@ApiOperation("Exclui um documento temporário")
	@RequestMapping(value = "/temporarios/delete", method = RequestMethod.POST)
	public void deleteTemp(@Valid @RequestBody DeleteTemporarioCommand command, BindingResult result) {
		if (result.hasErrors()) {
			throw new IllegalArgumentException(result.toString());
		}
		documentoApplicationService.handle(command);
	}

	@ApiOperation("Divide um documento")
	@RequestMapping(value = "/dividir", method = RequestMethod.POST)
	public List<Long> dividirDocumento(@Valid @RequestBody DividirDocumentosCompletamenteCommand command, BindingResult result) {
		if (result.hasErrors()) {
			throw new IllegalArgumentException(result.toString());
		}
		return documentoApplicationService.handle(command).stream().map(d -> d.toLong()).collect(Collectors.toList());
	}
	
	@ApiOperation("Une documentos")
	@RequestMapping(value = "/unir", method = RequestMethod.POST)
	public Long unirDocumentos(@Valid @RequestBody UnirDocumentosCommand command, BindingResult result) {
		if (result.hasErrors()) {
			throw new IllegalArgumentException(result.toString());
		}
		return documentoApplicationService.handle(command).toLong();
	}
	
	/**
	 * Define os headers para o pdf 
	 * 
	 * @param documentoId
	 * @param response
	 */
	private HttpHeaders createResponseHeaders(Long tamanho) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentLength(tamanho);
		headers.setContentType(MediaType.parseMediaType("application/pdf"));
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		return headers;
	}
	
	@ApiOperation("Extrai as tags de um documento")
	@RequestMapping(value = "/{documentoId}/tags", method = RequestMethod.GET)
	public List<TagDto> extrairTags(@PathVariable("documentoId") Long documentoId) {
		ConteudoDocumento conteudoDocumento = documentoRepository.download(new DocumentoId(documentoId));
		List<Tag> tags = documentoService.extrairTags(conteudoDocumento);
		return tags.stream().map(t -> new TagDto(t.nome())).collect(Collectors.toList());
	}
	
	@ApiOperation("Gera um documento subsitituindo as tags")
	@RequestMapping(value = "/gerar-com-tags")
	public Long gerarDocumentoComTags(GerarDocumentoComTagsCommand command) {
		DocumentoId documentoGeradoId = documentoApplicationService.handle(command);
		return documentoGeradoId.toLong();
	}
	
	@ApiOperation("Gera um documento final a partir do editável")
	@RequestMapping(value = "/gerar-final")
	public Long gerarDocumentoFinal(GerarDocumentoFinalCommand command) {
		return documentoApplicationService.handle(command).toLong();
	}
	
}
