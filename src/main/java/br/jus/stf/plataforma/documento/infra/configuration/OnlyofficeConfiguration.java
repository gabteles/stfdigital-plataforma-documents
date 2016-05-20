package br.jus.stf.plataforma.documento.infra.configuration;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

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

	@Value("${http.client.ssl.trust-store}")
	private String trustStore;

	@Value("${http.client.ssl.trust-store-password}")
	private char[] trustStorePassword;
	
	@Value("${onlyoffice.documentserver.host:}")
	private String documentServerHost;
	
	@Value("${onlyoffice.documentserver.protocol:http}")
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
	
	@PostConstruct
	public void configureTrustStore() throws Exception {
		String keystoreType = "JKS";
		InputStream keystoreLocation = null;
		char[] keystorePassword = null;
		char[] keyPassword = null;

		KeyStore keystore = KeyStore.getInstance(keystoreType);
		keystore.load(keystoreLocation, keystorePassword);
		KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmfactory.init(keystore, keyPassword);

		InputStream truststoreLocation = new FileInputStream(trustStore);
		char[] truststorePassword = trustStorePassword;
		String truststoreType = "JKS";

		KeyStore truststore = KeyStore.getInstance(truststoreType);
		truststore.load(truststoreLocation, truststorePassword);
		TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmfactory.init(truststore);

		KeyManager[] keymanagers = kmfactory.getKeyManagers();
		TrustManager[] trustmanagers = tmfactory.getTrustManagers();

		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(keymanagers, trustmanagers, new SecureRandom());
		SSLContext.setDefault(sslContext);
	}

}
