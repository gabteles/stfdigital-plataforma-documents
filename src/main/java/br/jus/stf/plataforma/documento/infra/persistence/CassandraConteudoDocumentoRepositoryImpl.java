package br.jus.stf.plataforma.documento.infra.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.plataforma.documento.domain.model.ConteudoDocumento;
import br.jus.stf.plataforma.documento.domain.model.DocumentoTemporario;

/**
 * Implementação do repositório de conteúdo com armazenamento no Cassandra.
 * 
 * @author Rafael Alencar
 * @since 19.09.2016
 *
 */
@Repository
public class CassandraConteudoDocumentoRepositoryImpl implements ConteudoDocumentoRepository {

	@Autowired
	private CassandraOperations cassandraOperations;
	
	@Value("${spring.data.cassandra.keyspace-name}")
	private String keySpace;
	
	@Override
	public ConteudoDocumento downloadConteudo(String numeroConteudo) {
		ConteudoDocumento conteudo = null;
		Statement readFile = QueryBuilder.select("bin_conteudo").from(keySpace, "documento")
				.where(QueryBuilder.eq("seq_documento", Integer.parseInt(numeroConteudo)));
		Row row = cassandraOperations.getSession().execute(readFile).one();
	    
		if (row != null) {
	        ByteBuffer buffer = row.getBytes("bin_conteudo");
	        byte[] bytes = new byte[buffer.remaining()];
	        
	        buffer.get(bytes);
	        
			conteudo = new ConteudoDocumento(bytes, new Long(bytes.length));
	    }
		
		return conteudo;
	}

	@Override
	public void deleteConteudo(String numeroConteudo) {
		Delete.Where delete = QueryBuilder.delete().from(keySpace, "documento")
				.where(QueryBuilder.eq("seq_documento", Integer.parseInt(numeroConteudo)));
		
		cassandraOperations.execute(delete);
	}

	@Override
	public String save(DocumentoId documentoId, DocumentoTemporario documentoTemporario) {
		InputStream stream = null;
		
		try {
			stream = documentoTemporario.stream();
			
			ByteBuffer buffer = ByteBuffer.wrap(IOUtils.toByteArray(stream));
			Insert insert = QueryBuilder.insertInto(keySpace, "documento")
					.value("seq_documento", documentoId.toLong()).value("bin_conteudo", buffer);
			
			cassandraOperations.execute(insert);
			
			return documentoId.toString();
		} catch (IOException e) {
			throw new RuntimeException("Não foi possível carregar o stream do arquivo ", e);
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

}
