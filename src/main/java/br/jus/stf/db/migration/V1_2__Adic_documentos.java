package br.jus.stf.db.migration;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Adiciona modelos e seus conteúdos no mongo.
 * 
 * @author Tomas.Godoi
 *
 */
@Component
public class V1_2__Adic_documentos implements SpringJdbcMigration {

	@Autowired
	private GridFsOperations gridOperations;

	@Autowired
	private ResourceLoader resourceLoader;

	@Override
	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
		Long documentoId = inserirDocumento(jdbcTemplate,
		        "classpath:documentos/modelos/modelo-oficio-devolucao-remessa-indevida.docx", 1);
		jdbcTemplate.update(
		        "INSERT INTO DOCUMENTO.MODELO (SEQ_MODELO, NOM_MODELO, SEQ_TIPO_MODELO, SEQ_DOCUMENTO_TEMPLATE) VALUES (CORPORATIVO.SEQ_MODELO.NEXTVAL, ?, ?, ?)",
		        "Ofício de devolução de petição por remessa indevida", 8L, documentoId);
	}

	private Long inserirDocumento(JdbcTemplate jdbcTemplate, String path, int quantidadePaginas) throws IOException {
		Long documentoId = jdbcTemplate.queryForObject("SELECT DOCUMENTO.SEQ_DOCUMENTO.NEXTVAL FROM DUAL",
		        Long.class);
		Resource resource = resourceLoader.getResource(path);
		String conteudoId = storeOnMongo(documentoId, path, resource);
		jdbcTemplate.update(
		        "INSERT INTO DOCUMENTO.DOCUMENTO (SEQ_DOCUMENTO, NUM_CONTEUDO, QTD_PAGINAS, TAMANHO) VALUES (?, ?, ?, ?)",
		        documentoId, conteudoId, quantidadePaginas, resource.contentLength());
		return documentoId;
	}

	public String storeOnMongo(Long documentoId, String path, Resource resource) throws IOException {
		InputStream stream = null;
		try {
			stream = resource.getInputStream();
			DBObject metaData = new BasicDBObject();

			metaData.put("seq_documento", documentoId);
			metaData.put("nom_arquivo", FilenameUtils.getName(path));
			metaData.put("num_tamanho_bytes", resource.contentLength());

			return gridOperations
			        .store(stream, FilenameUtils.getName(path),
			                "application/vnd.openxmlformats-officedocument.wordprocessingml.document", metaData)
			        .getId().toString();
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

}
