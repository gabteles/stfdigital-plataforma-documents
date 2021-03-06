package br.jus.stf.plataforma.documento.infra;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Source;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.core.shared.documento.PDFMultipartFile;
import br.jus.stf.plataforma.documento.domain.ConversorDocumentoService;
import br.jus.stf.plataforma.documento.domain.model.DocumentoTemporario;

@Component
public class ConversorDocumentoServiceOnlyofficeImpl implements ConversorDocumentoService {

	@Autowired
	@Qualifier("onlyofficeRestTemplate")
	private RestTemplate restTemplate;
	
	@Autowired
	@Qualifier("doocumentServerBaseUrl")
	private String doocumentServerBaseUrl;
	
    @Autowired
    @Qualifier("onlyofficeIntegrationBaseUrl")
    private String onlyofficeIntegrationBaseUrl;
	
	@Autowired
	@Qualifier("onlyofficeMarshaller")
	private Jaxb2Marshaller onlyofficeMashaller;
	
	private TokenExtractor tokenExtractor = new BearerTokenExtractor();

	@Override
	@HystrixCommand
	public DocumentoTemporario converterDocumentoFinal(DocumentoId documentoId) {
		String token = recuperarToken();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_XML_VALUE);
		
		HttpEntity<?> entity = new HttpEntity<>(headers);
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(onlyofficeIntegrationBaseUrl + "/ConvertService.ashx")
			.queryParam("key", UUID.randomUUID().toString())
			.queryParam("url", doocumentServerBaseUrl + "/documents/api/onlyoffice/documentos/" + documentoId.toLong() + "/conteudo.docx?access_token=" + token + "&_csrf=" + token)
			.queryParam("filetype", "docx")
			.queryParam("outputtype", "pdf")
			.queryParam("embeddedfonts", "true");
		HttpEntity<Source> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, Source.class);
		OnlyofficeConversionReply ocr = (OnlyofficeConversionReply) onlyofficeMashaller.unmarshal(response.getBody());
		
		if (ocr.getEndConvert().equals("True")) {
			try {
				return recuperarArquivoConvertido(ocr.getFileUrl());
			} catch (RestClientException | URISyntaxException e) {
				throw new RuntimeException("Erro ao recuperar documento convertido.", e);
			}
		} else {
			throw new RuntimeException("Erro ao converter documento.");
		}
	}

	private String recuperarToken() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		return tokenExtractor.extract(request).getPrincipal().toString();
	}

	private DocumentoTemporario recuperarArquivoConvertido(String fileUrl) throws RestClientException, URISyntaxException {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		ResponseEntity<byte[]> response = restTemplate.exchange(buildUrl(fileUrl), HttpMethod.GET,
		        entity, byte[].class);
		
		return new DocumentoTemporario(new PDFMultipartFile("documento.pdf", response.getBody()));
	}

    private URI buildUrl(String urlString) throws URISyntaxException {
        URI url = new URI(urlString);
        return new URI(onlyofficeIntegrationBaseUrl + url.getPath());
    }
	
}
