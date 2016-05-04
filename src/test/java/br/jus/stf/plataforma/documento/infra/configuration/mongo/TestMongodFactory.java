package br.jus.stf.plataforma.documento.infra.configuration.mongo;

import java.io.IOException;
import java.net.UnknownHostException;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.jus.stf.plataforma.documento.infra.LocalData;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.Storage;
import de.flapdoodle.embed.mongo.distribution.IFeatureAwareVersion;
import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory;
import de.flapdoodle.embed.process.runtime.Network;

/**
 * Factory para criar um mongodb que persistirá em um diretório do home do
 * usuário.
 * 
 * @author Tomas.Godoi
 *
 */
@Configuration
@Profile("test")
public class TestMongodFactory extends MongodForTestsFactory {

	public TestMongodFactory() throws IOException {
		super();
	}

	@Override
	protected IMongodConfig newMongodConfig(IFeatureAwareVersion version) throws UnknownHostException, IOException {
		return new MongodConfigBuilder().version(version).net(new Net(37019, Network.localhostIsIPv6()))
				.replication(new Storage(LocalData.instance().dataDirAbsolutePath() + "/mongodb", null, 0)).build();
	}

}

