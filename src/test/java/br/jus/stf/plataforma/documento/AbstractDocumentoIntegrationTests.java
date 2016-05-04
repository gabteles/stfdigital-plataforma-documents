package br.jus.stf.plataforma.documento;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;

import br.jus.stf.core.framework.testing.IntegrationTestsSupport;

@SpringApplicationConfiguration(ApplicationContextInitializer.class)
@ActiveProfiles("test")
@WebIntegrationTest({"server.port:0", "eureka.client.enabled:false"})
public abstract class AbstractDocumentoIntegrationTests extends IntegrationTestsSupport {

}
