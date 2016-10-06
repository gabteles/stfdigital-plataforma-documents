package br.jus.stf.plataforma.documento.infra.configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;


/**
 * @author Rafael.Alencar
 * @since 19.09.2016
 *
 */
@Configuration
@Profile({"production", "development", "!test"})
public class CassandraConfiguration extends AbstractCassandraConfiguration {

	@Value("${cassandra.keyspace}")
	private String keySpace;
	@Value("${cassandra.contactpoints}")
	private String contactPoints;
	@Value("${cassandra.port}")
	private String port;
	
	@Autowired
	private ResourceLoader resourceLoader;

	@Bean
    public CassandraClusterFactoryBean cluster() {
        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        
        cluster.setContactPoints(contactPoints);
        cluster.setPort(Integer.parseInt(port));
        cluster.setStartupScripts(getStartupScripts());
        
        return cluster;
    }

	@Override
	protected String getKeyspaceName() {
		return keySpace;
	}
	
	@Override
	protected List<String> getStartupScripts() {
		List<String> scripts = Collections.emptyList();
		
		try (InputStream stream = resourceLoader
				.getResource("classpath:db/migration/cassandra/V1__Estrutura_inicial.cql").getInputStream();
				InputStreamReader reader = new InputStreamReader(stream);
				BufferedReader buffer = new BufferedReader(reader)) {
			scripts = buffer.lines().map(line -> line.split(System.getProperty("line.separator")))
					.flatMap(Arrays::stream).map(String::valueOf).collect(Collectors.toList());
		} catch (IOException e) {
			new RuntimeException("Não foi possível carregar o arquivo ", e);
		}
		
		return scripts;
	}

}
