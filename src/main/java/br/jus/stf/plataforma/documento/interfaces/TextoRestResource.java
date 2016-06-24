package br.jus.stf.plataforma.documento.interfaces;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;

import br.jus.stf.core.shared.documento.TextoId;
import br.jus.stf.plataforma.documento.application.TextoApplicationService;
import br.jus.stf.plataforma.documento.application.command.AssinarTextoCommand;
import br.jus.stf.plataforma.documento.application.command.ConcluirTextoCommand;
import br.jus.stf.plataforma.documento.domain.model.ConteudoDocumento;
import br.jus.stf.plataforma.documento.domain.model.Documento;
import br.jus.stf.plataforma.documento.domain.model.DocumentoRepository;
import br.jus.stf.plataforma.documento.domain.model.Texto;
import br.jus.stf.plataforma.documento.domain.model.TextoRepository;
import br.jus.stf.plataforma.documento.interfaces.dto.DocumentoDto;
import br.jus.stf.plataforma.documento.interfaces.dto.DocumentoDtoAssembler;

@RestController
@RequestMapping("/api/textos")
public class TextoRestResource {

	@Autowired
	private TextoRepository textoRepository;
	
	@Autowired
	private DocumentoRepository documentoRepository;
	
	@Autowired
	private TextoApplicationService textoApplicationService;
	
	@Autowired
	private DocumentoDtoAssembler documentoDtoAssembler;
	
	@ApiOperation("Recupera o conteúdo pdf associado a um texto.")
	@RequestMapping(value = "/{textoId}/conteudo.pdf", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> recuperarConteudoPdf(@PathVariable("textoId") Long textoId) throws IOException {
		Texto texto = textoRepository.findOne(new TextoId(textoId));
		ConteudoDocumento documento = documentoRepository.download(texto.documentoFinal());
		InputStreamResource is = new InputStreamResource(documento.stream());
		HttpHeaders headers = createResponseHeaders(documento.tamanho());
		return new ResponseEntity<InputStreamResource>(is, headers, HttpStatus.OK);
	}
	
	@ApiOperation("Recupera o documento final associado a um texto")
	@RequestMapping(value = "/{textoId}/documento-final", method = RequestMethod.GET)
	public DocumentoDto recuperarDocumentoFinal(@PathVariable("textoId") Long textoId) {
		Texto texto = textoRepository.findOne(new TextoId(textoId));
		Documento documentoFinal = documentoRepository.findOne(texto.documentoFinal());
		return documentoDtoAssembler.toDo(documentoFinal);
	}
	
	@ApiOperation("Conclui um texto, gerando seu documento final associado")
	@RequestMapping(value = "/concluir", method = RequestMethod.POST)
	public void concluir(@RequestBody @Valid ConcluirTextoCommand command) {
		textoApplicationService.handle(command);
	}
	
	@ApiOperation("Realiza a assinatura do texto")
	@RequestMapping(value = "/assinar", method = RequestMethod.POST)
	public void assinar(@RequestBody @Valid AssinarTextoCommand command) {
		textoApplicationService.handle(command);
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
	
}
