package br.jus.stf.plataforma.documento.infra.configuration.mongo;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.mongodb.Mongo;

import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory;

/**
 * @author Rafael.Alencar
 */
@Configuration
@Profile("test")
public class MongoMemoryConfiguration extends AbstractMongoConfiguration {

	public MongodForTestsFactory factory() throws IOException {
		return MongodForTestsFactory.with(Version.Main.PRODUCTION);
	}

	@Bean
	public GridFsTemplate gridFsTemplate() throws Exception {
		return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
	}

	@Override
	protected String getDatabaseName() {
		return "test";
	}

	@Override
	public Mongo mongo() throws Exception {
		return factory().newMongo();
	}
}