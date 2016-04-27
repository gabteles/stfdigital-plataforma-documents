package br.jus.stf.plataforma.documento.infra.configuration.oracle;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

import br.jus.stf.plataforma.documento.infra.DocumentProfiles;

@Configuration
@Profile(DocumentProfiles.DOCUMENTO_ORACLE)
public class OracleDocumentoPersistenceConfiguration {

	@Bean(name = "documentoJdbcTemplate")
	public JdbcTemplate documentoJdbcTemplate() {
		return new JdbcTemplate(documentoOracleDataSource());
	}

	@Bean(name = "dataSourceDocumento")
	public DataSource documentoOracleDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
		dataSource.setUrl("jdbc:oracle:thin:@10.200.3.129:1521:documento");
		dataSource.setUsername("sys as sysdba");
		dataSource.setPassword("oracle123");
		
		dataSource.setRemoveAbandoned(true);
		dataSource.setInitialSize(10);
		dataSource.setMaxActive(50);
		
		return dataSource;
	}

}
