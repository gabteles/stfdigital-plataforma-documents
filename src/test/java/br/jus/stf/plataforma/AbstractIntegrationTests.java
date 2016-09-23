package br.jus.stf.plataforma;

import org.cassandraunit.spring.CassandraDataSet;
import org.cassandraunit.spring.CassandraUnitDependencyInjectionTestExecutionListener;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import br.jus.stf.core.framework.testing.IntegrationTestsSupport;
import br.jus.stf.plataforma.documento.ApplicationContextInitializer;

@SpringBootTest(value = { "server.port:0", "eureka.client.enabled:false",
		"spring.cloud.config.enabled:false" }, classes = ApplicationContextInitializer.class)
@TestExecutionListeners({ CassandraUnitDependencyInjectionTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class, WithSecurityContextTestExecutionListener.class })
@EmbeddedCassandra(configuration = "cassandra.yaml")
@CassandraDataSet("db/migration/cassandra/V1__Estrutura_inicial.cql")
public abstract class AbstractIntegrationTests extends IntegrationTestsSupport {
	
}
