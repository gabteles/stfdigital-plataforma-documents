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
 * Adiciona modelos e seus conteúdos no cassandra.
 * 
 * @author Tomas.Godoi
 *
 */
@Component
public class V1_2__Adic_documentos implements SpringJdbcMigration {

	@Autowired
	private CassandraOperations cassandraOperations;

	@Autowired
	private ResourceLoader resourceLoader;

	@Override
	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
		Long documentoId = inserirDocumento(jdbcTemplate,
		        "classpath:documentos/modelos/modelo-oficio-devolucao-remessa-indevida.docx", 1);
		jdbcTemplate.update(
		        "INSERT INTO DOCUMENTS.MODELO (SEQ_MODELO, NOM_MODELO, SEQ_TIPO_DOCUMENTO, SEQ_DOCUMENTO_TEMPLATE) VALUES (DOCUMENTS.SEQ_MODELO.NEXTVAL, ?, ?, ?)",
		        "Ofício de devolução de petição por remessa indevida", 8L, documentoId);
	}

	private Long inserirDocumento(JdbcTemplate jdbcTemplate, String path, int quantidadePaginas) throws IOException {
		Long documentoId = jdbcTemplate.queryForObject("SELECT DOCUMENTS.SEQ_DOCUMENTO.NEXTVAL FROM DUAL",
		        Long.class);
		Resource resource = resourceLoader.getResource(path);
		String conteudoId = storeOnCassandra(documentoId, path, resource);
		jdbcTemplate.update(
		        "INSERT INTO DOCUMENTS.DOCUMENTO (SEQ_DOCUMENTO, NUM_CONTEUDO, QTD_PAGINAS, TAMANHO) VALUES (?, ?, ?, ?)",
		        documentoId, conteudoId, quantidadePaginas, resource.contentLength());
		return documentoId;
	}
	
	private String storeOnCassandra(Long documentoId, String path, Resource resource) throws IOException {
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
