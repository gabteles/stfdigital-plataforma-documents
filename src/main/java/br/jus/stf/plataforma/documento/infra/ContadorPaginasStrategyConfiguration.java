package br.jus.stf.plataforma.documento.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.jus.stf.plataforma.documento.domain.ContadorPaginas;

@Configuration
public class ContadorPaginasStrategyConfiguration {

	@Bean
	public ContadorPaginas contadorPaginas() {
		ContadorPaginasImpl contador = new ContadorPaginasImpl();
		contador.addStrategy("application/pdf", new ContadorPaginasPdf());
		contador.addStrategy("application/vnd.openxmlformats-officedocument.wordprocessingml.document", new ContadorPaginasDocx());
		return contador;
	}

}
