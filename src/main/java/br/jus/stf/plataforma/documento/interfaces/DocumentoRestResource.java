package br.jus.stf.plataforma.documento.interfaces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;

import org.apache.commons.lang3.Range;
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
import br.jus.stf.core.shared.documento.DocumentoTemporarioId;
import br.jus.stf.plataforma.documento.domain.model.ConteudoDocumento;
import br.jus.stf.plataforma.documento.interfaces.commands.DeleteTemporarioCommand;
import br.jus.stf.plataforma.documento.interfaces.commands.DividirDocumentoCommand;
import br.jus.stf.plataforma.documento.interfaces.commands.GerarDocumentoComTagsCommand;
import br.jus.stf.plataforma.documento.interfaces.commands.GerarDocumentoFinalCommand;
import br.jus.stf.plataforma.documento.interfaces.commands.SalvarDocumentosCommand;
import br.jus.stf.plataforma.documento.interfaces.commands.UnirDocumentosCommand;
import br.jus.stf.plataforma.documento.interfaces.commands.UploadDocumentoAssinadoCommand;
import br.jus.stf.plataforma.documento.interfaces.commands.UploadDocumentoCommand;
import br.jus.stf.plataforma.documento.interfaces.dto.DocumentoDto;
import br.jus.stf.plataforma.documento.interfaces.dto.DocumentoTemporarioDto;
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
	private DocumentoServiceFacade documentoServiceFacade;
	
	@Autowired
	private Validator validator;

	@ApiOperation("Salva os documentos temporários")
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public List<DocumentoTemporarioDto> salvar(@RequestBody SalvarDocumentosCommand command) {
		Set<ConstraintViolation<SalvarDocumentosCommand>> result = validator.validate(command);
		if (!result.isEmpty()) {
			throw new IllegalArgumentException(result.toString());
		}
		return documentoServiceFacade.salvarDocumentos(command.getIdsDocumentosTemporarios().stream().map(id -> new DocumentoTemporarioId(id)).collect(Collectors.toList()));
	}	
	
	@ApiOperation("Recupera o conteúdo de um documento do repositório")
	@RequestMapping(value = "/{documentoId}/conteudo", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> recuperar(@PathVariable("documentoId") Long documentoId) throws IOException {
		ConteudoDocumento documento = documentoServiceFacade.pesquisaDocumento(documentoId);
		InputStreamResource is = new InputStreamResource(documento.stream());
		HttpHeaders headers = createResponseHeaders(documento.tamanho());
	    return new ResponseEntity<InputStreamResource>(is, headers, HttpStatus.OK);
	}
	
	@ApiOperation("Retornda os dados de um documento")
	@RequestMapping(value = "/{documentoId}", method = RequestMethod.GET)
	public DocumentoDto consultar(@PathVariable("documentoId") Long documentoId) throws IOException {
	    return documentoServiceFacade.consultar(documentoId);
	}
	
	@ApiOperation("Envia um documento para armazenamento temporário e retorna o indentificador")
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public String upload(UploadDocumentoCommand command) {
		return documentoServiceFacade.salvarDocumentoTemporario(command.getFile());
	}
	
	@ApiOperation("Envia um documento assinado para armazenamento temporário e retorna o indentificador")
	@RequestMapping(value = "/upload/assinado", method = RequestMethod.POST)
	@ApiResponses(value = {@ApiResponse(code = 400, message = "O arquivo enviado não foi assinado digitalmente.")})
	@ResponseStatus(HttpStatus.CREATED)
	public String uploadAssinado(@Valid UploadDocumentoAssinadoCommand command, BindingResult result) {
		if (result.hasErrors()) {
			throw new ValidationException(result.getAllErrors());
		}		
		
		return documentoServiceFacade.salvarDocumentoTemporario(command.getFile());
	}
	
	@ApiOperation("Envia um documento para armazenamento temporário e retorna o indentificador")
	@RequestMapping(value = "/temporarios/delete", method = RequestMethod.POST)
	public void deleteTemp(@Valid @RequestBody DeleteTemporarioCommand command, BindingResult result) {
		if (result.hasErrors()) {
			throw new IllegalArgumentException(result.toString());
		}		
		documentoServiceFacade.apagarDocumentosTemporarios(command.getFiles());
	}

	@ApiOperation("Divide um documento")
	@RequestMapping(value = "/dividir", method = RequestMethod.POST)
	public List<Long> dividirDocumento(@Valid @RequestBody List<DividirDocumentoCommand> commands, BindingResult result) {
		if (result.hasErrors()) {
			throw new IllegalArgumentException(result.toString());
		}
		Map<Long, List<DividirDocumentoCommand>> commandsById = commands.stream().collect(Collectors.groupingBy(DividirDocumentoCommand::getDocumentoId));
		List<DocumentoId> documentosDivididos = new ArrayList<>();
		for (Long id : commandsById.keySet()) {
			List<Range<Integer>> intervalos = commandsById.get(id).stream().map(d -> Range.between(d.getPaginaInicial(), d.getPaginaFinal())).collect(Collectors.toList());
			documentosDivididos.addAll(documentoServiceFacade.dividirDocumento(new DocumentoId(id), intervalos));
		}
		return documentosDivididos.stream().map(d -> d.toLong()).collect(Collectors.toList());
	}
	
	@ApiOperation("Une documentos")
	@RequestMapping(value = "/unir", method = RequestMethod.POST)
	public Long unirDocumentos(@Valid @RequestBody UnirDocumentosCommand command, BindingResult result) {
		if (result.hasErrors()) {
			throw new IllegalArgumentException(result.toString());
		}
		return documentoServiceFacade.unirDocumentos(command.getIdsDocumentos().stream().map(id -> new DocumentoId(id)).collect(Collectors.toList())).toLong();
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
		return documentoServiceFacade.extrairTags(new DocumentoId(documentoId)).stream().map(t -> new TagDto(t.nome())).collect(Collectors.toList());
	}
	
	@ApiOperation("Gera um documento subsitituindo as tags")
	@RequestMapping(value = "/gerar-com-tags")
	public Long gerarDocumentoComTags(GerarDocumentoComTagsCommand command) {
		return documentoServiceFacade.gerarDocumentoComTags(command.getDocumentoId(), command.getSubstituicoes());
	}
	
	@ApiOperation("Gera um documento final a partir do editável")
	@RequestMapping(value = "/gerar-final")
	public Long gerarDocumentoFinal(GerarDocumentoFinalCommand command) {
		return documentoServiceFacade.gerarDocumentoFinal(command.getDocumento());
	}
	
}
