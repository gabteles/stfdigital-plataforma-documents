package br.jus.stf.db.migration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.apache.commons.io.IOUtils;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;

/**
 * Adiciona textos, documentos e seus conteúdos no H2 e no cassandra.
 * 
 * @author Rafael Alencar
 *
 */
@Component
public class V1_3__Adiciona_documento_texto_teste implements SpringJdbcMigration {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Autowired
	private ResourceLoader resourceLoader;

	@Override
	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
		for (int i = 9000; i < 9004; i++) {
			Long modeloId = inserirDocumento(jdbcTemplate,
			        "classpath:documentos/modelos/modelo-oficio-devolucao-remessa-indevida.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", 1);
			
			Long documentoId = inserirDocumento(jdbcTemplate,
			        "classpath:documentos/pdftestes/pdf-B-3-pgs.pdf", "application/pdf", 3);
			
			jdbcTemplate.update(
					"INSERT INTO documents.texto (seq_texto, seq_documento, seq_documento_final) VALUES (?, ?, ?)",
					i, modeloId, documentoId);
		}
	}

	private Long inserirDocumento(JdbcTemplate jdbcTemplate, String path, String mimeType, int quantidadePaginas) throws IOException {
		Long documentoId = jdbcTemplate.queryForObject("SELECT documents.seq_documento.NEXTVAL FROM DUAL",
		        Long.class);
		Resource resource = resourceLoader.getResource(path);
		String conteudoId = storeOnCassandra(documentoId, path, mimeType, resource);
		
		jdbcTemplate.update(
				"INSERT INTO documents.documento (seq_documento, num_conteudo, qtd_paginas, tamanho) VALUES (?, ?, ?, ?)",
				documentoId, conteudoId, quantidadePaginas, resource.contentLength());
		
		return documentoId;
	}
	
	private String storeOnCassandra(Long documentoId, String path, String mimeType, Resource resource) throws IOException {
		InputStream stream = null;
		try {
			stream = resource.getInputStream();

			ByteBuffer buffer = ByteBuffer.wrap(IOUtils.toByteArray(stream));
			Insert insert = QueryBuilder.insertInto("documents", "documento")
					.value("seq_documento", documentoId).value("bin_conteudo", buffer);
			
			cassandraOperations.execute(insert);
			
			return documentoId.toString();
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

}
