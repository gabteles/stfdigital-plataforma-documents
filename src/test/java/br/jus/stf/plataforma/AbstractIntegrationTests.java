package br.jus.stf.plataforma;

import org.springframework.boot.test.context.SpringBootTest;

import br.jus.stf.core.framework.testing.IntegrationTestsSupport;
import br.jus.stf.plataforma.documento.ApplicationContextInitializer;

@SpringBootTest(value = {"server.port:0", "eureka.client.enabled:false"}, classes = ApplicationContextInitializer.class)
public abstract class AbstractIntegrationTests extends IntegrationTestsSupport {

}
