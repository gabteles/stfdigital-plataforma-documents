package br.jus.stf.plataforma.documento.infra.configuration.mongo;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.mongodb.Mongo;

import br.jus.stf.plataforma.documento.infra.AndProfilesCondition;
import br.jus.stf.plataforma.documento.infra.DocumentProfiles;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory;

/**
 * @author Rafael.Alencar
 */
@Configuration
//@Profile({ "!" + Profiles.MONGO_SERVER, Profiles.DOCUMENTO_MONGO })
@Profile({"!" + DocumentProfiles.DOCUMENTO_FS, "!" + DocumentProfiles.DOCUMENTO_ORACLE }) // Setando o profile do mongo se os outros nÃ£o forem ativados.
@Conditional(AndProfilesCondition.class)
public class MongoMemoryConfiguration extends AbstractMongoConfiguration {

	private MongodForTestsFactory factory;
	@Autowired
	private Environment env;

	public MongodForTestsFactory factory() throws IOException {
		if (factory == null) {
			LocalMongoCleaner.killExtracted();
			if (env.acceptsProfiles(DocumentProfiles.KEEP_DATA)) {
				LocalMongoCleaner.clearLock();
				factory = new PersistentMongodFactory();
			} else {
				factory = MongodForTestsFactory.with(Version.Main.PRODUCTION);
			}
		}
		return factory;
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
