package br.jus.stf.plataforma.documento.infra.configuration.mongo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

/**
 * @author Rafael.Alencar
 *
 */
@Configuration
@Profile("development")
public class MongoConfiguration extends AbstractMongoConfiguration {

	@Value("${mongo.host}")
	private String hostMongo;
	@Value("${mongo.databaseName}")
	private String databaseName;

	@Bean
	public GridFsTemplate gridFsTemplate() {

		GridFsTemplate gridFsTemplate = null;

		try {
			gridFsTemplate = new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return gridFsTemplate;
	}
	
	@Override
	protected String getDatabaseName() {
		return databaseName;
	}

	@Override
	@Bean
	public Mongo mongo() throws Exception {
		return new MongoClient(hostMongo);
	}

}
