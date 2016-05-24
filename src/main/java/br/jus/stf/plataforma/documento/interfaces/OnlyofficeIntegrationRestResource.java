package br.jus.stf.plataforma.documento.interfaces;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.client.RestClientException;

import com.wordnik.swagger.annotations.ApiOperation;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.plataforma.documento.application.OnlyofficeCallbackApplicationService;
import br.jus.stf.plataforma.documento.domain.ControladorEdicaoDocumento;
import br.jus.stf.plataforma.documento.domain.model.ConteudoDocumento;
import br.jus.stf.plataforma.documento.domain.model.Documento;
import br.jus.stf.plataforma.documento.domain.model.DocumentoRepository;
import br.jus.stf.plataforma.documento.domain.model.Edicao;
import br.jus.stf.plataforma.documento.interfaces.dto.EdicaoDto;

@RestController
@RequestMapping("/api/onlyoffice")
public class OnlyofficeIntegrationRestResource {

	@Autowired
	private DocumentoRepository documentoRepository;

	@Autowired
	private ControladorEdicaoDocumento controladorEdicaoDocumento;

	@Autowired
	private OnlyofficeCallbackApplicationService onlyofficeCallbackApplicationService;
	
	@Autowired
	@Qualifier("doocumentServerBaseUrl")
	private String doocumentServerBaseUrl;
	
	@Value("${onlyoffice.server.address}")
	private String onlyofficeAddress;

	@ApiOperation("Recupera o conteúdo de um documento")
	@RequestMapping(value = "/documentos/{documentoId}/conteudo.docx", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> recuperarConteudo(@PathVariable("documentoId") Long documentoId)
	        throws IOException {
		ConteudoDocumento documento = documentoRepository.download(new DocumentoId(documentoId));
		InputStream is = documento.stream();
		byte[] bytes = IOUtils.toByteArray(is);
		InputStreamResource isr = new InputStreamResource(new ByteArrayInputStream(bytes));
		HttpHeaders headers = createResponseHeaders(new Long(bytes.length));
		return new ResponseEntity<InputStreamResource>(isr, headers, HttpStatus.OK);

	}

	private HttpHeaders createResponseHeaders(Long tamanho) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentLength(tamanho);
		headers.setContentType(
		        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
		return headers;
	}

	@ApiOperation("Gera ou reutiliza o número de edição para um documento")
	@RequestMapping(value = "/documentos/{documentoId}/edicao", method = RequestMethod.PUT)
	public EdicaoDto recuperarEdicao(@PathVariable("documentoId") Long documentoId) throws IOException {
		Documento documento = documentoRepository.findOne(new DocumentoId(documentoId));
		Edicao edicao = controladorEdicaoDocumento.gerarEdicao(documento.identity());
		return new EdicaoDto(edicao.numero(), edicao.ativo());
	}

	@ApiOperation("Verifica se um documento está em edição")
	@RequestMapping(value = "/documentos/{documentoId}/edicao", method = RequestMethod.GET)
	public ResponseEntity<EdicaoDto> estaEmEdicao(@PathVariable("documentoId") Long documentoId) {
		Edicao edicao = controladorEdicaoDocumento.recuperarEdicao(new DocumentoId(documentoId));
		if (edicao != null) {
			return new ResponseEntity<>(new EdicaoDto(edicao.numero(), edicao.ativo()), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@ApiOperation("Callback para o onlyoffice")
	@RequestMapping(value = "/documentos/{documentoId}/callback", method = RequestMethod.POST)
	public Map<String, Object> callback(@PathVariable("documentoId") Long documentoId,
	        @RequestBody Map<String, Object> json) throws RestClientException, URISyntaxException {
		return onlyofficeCallbackApplicationService.callback(documentoId, json);
	}

	@ApiOperation("Recupera a url base do servidor de documentos")
	@RequestMapping(value = "/server/baseUrl", method = RequestMethod.GET)
	public String serverBaseUrl() {
		return doocumentServerBaseUrl;
	}
	
	@ApiOperation("Recupera a url base do servidor onlyoffice")
	@RequestMapping(value = "/baseUrl", method = RequestMethod.GET)
	public String onlyofficeBaseUrl() {
		return onlyofficeAddress;
	}
	
}