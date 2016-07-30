package br.jus.stf.plataforma.documento.domain;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import br.jus.stf.plataforma.documento.application.DocumentoApplicationService;
import br.jus.stf.plataforma.documento.application.command.ConcluirEdicaoDocumento;

@Component
public class OnlyofficeCallbackService {

	@Autowired
	@Qualifier("onlyofficeRestTemplate")
	private RestTemplate restTemplate;

	@Autowired
	private DocumentoApplicationService documentoApplicationService;
	
	@Autowired
	private ControladorEdicaoDocumento controladorEdicaoDocumento;

	public Map<String, Object> callback(Long documentoId, Map<String, Object> json)
	        throws RestClientException, URISyntaxException {
		if (json.get("status").equals(0)) {
			return callbackNoDocumentWithKeyFound(json);
		} else if (json.get("status").equals(1)) {
			return callbackDocumentBeingEdited(documentoId, json);
		} else if (json.get("status").equals(2)) {
			return callbackDocumentReadyForSaving(documentoId, json);
		} else if (json.get("status").equals(3)) {
			return callbackDocumentSavingError(json);
		} else if (json.get("status").equals(4)) {
			return callbackDocumentClosedNoChanges(json);
		} else {
			return successResponse();
		}
	}

	private Map<String, Object> successResponse() {
		Map<String, Object> response = new HashMap<>();
		response.put("error", 0);
		return response;
	}

	private Map<String, Object> callbackDocumentClosedNoChanges(Map<String, Object> json) {
		String numeroEdicao = (String) json.get("key");
		controladorEdicaoDocumento.excluirEdicao(numeroEdicao);
		return successResponse();
	}

	private Map<String, Object> callbackDocumentSavingError(Map<String, Object> json) {
		return successResponse();
	}

	private Map<String, Object> callbackDocumentReadyForSaving(Long documentoId, Map<String, Object> json)
	        throws RestClientException, URISyntaxException {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		ResponseEntity<byte[]> response = restTemplate.exchange(new URI((String) json.get("url")), HttpMethod.GET,
		        entity, byte[].class);

		String numeroEdicao = (String) json.get("key");

		ConcluirEdicaoDocumento command = new ConcluirEdicaoDocumento(numeroEdicao, documentoId, response.getBody());
		
		documentoApplicationService.handle(command);

		return successResponse();
	}

	private Map<String, Object> callbackDocumentBeingEdited(Long documentoId, Map<String, Object> json) {
		String numeroEdicao = (String) json.get("key");
		controladorEdicaoDocumento.ativarEdicao(numeroEdicao);
		return successResponse();
	}

	private Map<String, Object> callbackNoDocumentWithKeyFound(Map<String, Object> json) {
		return successResponse();
	}

}