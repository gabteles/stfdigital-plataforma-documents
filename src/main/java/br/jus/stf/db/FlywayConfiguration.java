package br.jus.stf.db;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.internal.util.Location;
import org.flywaydb.core.internal.util.scanner.Scanner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @author Tomas Godoi
 * @since 13.05.2016
 *
 */
@Configuration
public class FlywayConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;
	
	@Value("${flyway.validate-on-migrate}")
	private boolean validateOnMigrate;
	
	@Value("${flyway.table}")
    private String table;
	
	/**
	 * Configura o versionamento do do banco via flyway
	 * 
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
	@Bean(name = "flyway", initMethod="migrate")
	@DependsOn("dataSource")
	public Flyway flyway(DataSource dataSource) throws Exception {
		Flyway flyway = new Flyway();
		
		flyway.setValidateOnMigrate(validateOnMigrate);
		flyway.setTable(table);
		flyway.setDataSource(dataSource);
		flyway.setResolvers(new ApplicationContextAwareSpringJdbcMigrationResolver(new Scanner(Thread.currentThread().getContextClassLoader()),
				new Location(flyway.getLocations()[0]), flyway, applicationContext));
		
		return flyway;
	}

	@Override
	public void setApplicationContext(ApplicationContext ac) throws BeansException {
		this.applicationContext = ac;
	}
	
}
