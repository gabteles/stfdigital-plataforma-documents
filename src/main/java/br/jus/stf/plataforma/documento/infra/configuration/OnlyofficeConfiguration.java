package br.jus.stf.plataforma.documento.infra.configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.RestTemplate;

import br.jus.stf.plataforma.documento.infra.OnlyofficeConversionReply;

/**
 * Configurações necessárias para a integração com o onlyoffice.
 * 
 * @author Tomas.Godoi
 *
 */
@Configuration
public class OnlyofficeConfiguration {
	
	@Value("${onlyoffice.documentserver.host:}")
	private String documentServerHost;
	
	@Value("${onlyoffice.documentserver.protocol:https}")
	private String documentServerProtocol;
	
	@Value("${onlyoffice.documentserver.port:8765}")
	private Integer port;

	@Bean(name = "onlyofficeRestTemplate")
	public RestTemplate onlyofficeRestTemplate() throws Exception {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new ByteArrayHttpMessageConverter());
		messageConverters.add(new SourceHttpMessageConverter<>());
		messageConverters.add(new MarshallingHttpMessageConverter(new Jaxb2Marshaller()));

		RestTemplate restTemplate = new RestTemplate(messageConverters);

		return restTemplate;
	}

	@Bean(name = "onlyofficeMarshaller")
	public Jaxb2Marshaller onlyofficeMarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(OnlyofficeConversionReply.class);
		return marshaller;
	}
	
	@Bean(name = "doocumentServerBaseUrl")
	public String documentServerHost() throws UnknownHostException {
		if (StringUtils.isBlank(documentServerHost)) {
			return documentServerProtocol + "://" + InetAddress.getLocalHost().getHostName() + ":" + port;
		} else {
			return documentServerProtocol + "://" + documentServerHost + ":" + port;
		}
	}

}
