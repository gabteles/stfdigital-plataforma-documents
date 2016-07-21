package br.jus.stf.plataforma.documento;

import org.springframework.boot.test.context.SpringBootTest;

import br.jus.stf.core.framework.testing.IntegrationTestsSupport;

@SpringBootTest(value = {"server.port:0", "eureka.client.enabled:false"}, classes = ApplicationContextInitializer.class)
public abstract class AbstractDocumentoIntegrationTests extends IntegrationTestsSupport {

}
