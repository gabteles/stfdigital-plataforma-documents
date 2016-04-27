package br.jus.stf.plataforma.documento.infra.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.plataforma.documento.domain.model.ConteudoDocumento;
import br.jus.stf.plataforma.documento.domain.model.DocumentoTemporario;
import br.jus.stf.plataforma.documento.infra.DocumentProfiles;

/**
 * Implementação do repositório de conteúdo com armazenamento no Oracle.
 * 
 * @author Tomas.Godoi
 *
 */
@Repository
@Profile(DocumentProfiles.DOCUMENTO_ORACLE)
public class OracleConteudoDocumentoRepositoryImpl implements ConteudoDocumentoRepository {

	private static final String CONTEUDO_ID_COLUMN = "SEQ_DOCUMENTO";
	private static final String CONTEUDO_BINARIO_COLUMN = "BIN_ARQUIVO";

	private static final String CONTEUDO_INSERT_SQL = "INSERT INTO DOCUMENTO.DOCUMENTO_ORACLE (SEQ_DOCUMENTO, BIN_ARQUIVO) VALUES (DOCUMENTO.SEQ_DOCUMENTO.NEXTVAL, ?)";
	private static final String CONTEUDO_SELECT_SQL = "SELECT SEQ_DOCUMENTO, BIN_ARQUIVO FROM DOCUMENTO.DOCUMENTO_ORACLE WHERE SEQ_DOCUMENTO = ?";
	private static final String CONTEUDO_DELETE_SQL = "DELETE FROM DOCUMENTO.DOCUMENTO_ORACLE WHERE SEQ_DOCUMENTO = ?";

	@Autowired
	@Qualifier("documentoJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	private class ConteudoDocumentoDownloadMapper implements RowMapper<ConteudoDocumento> {

		@Override
		public ConteudoDocumento mapRow(ResultSet rs, int rowNum) throws SQLException {
			Blob blob = rs.getBlob(CONTEUDO_BINARIO_COLUMN);
			try {
				byte[] bytes = IOUtils.toByteArray(blob.getBinaryStream());
				return new ConteudoDocumento(bytes, new Long(bytes.length));
			} catch (IOException e) {
				throw new SQLException("Erro ao recuperar o conteúdo do documento.", e);
			}
		}

	}

	@Override
	public ConteudoDocumento downloadConteudo(String numeroConteudo) {
		return jdbcTemplate.queryForObject(CONTEUDO_SELECT_SQL, new ConteudoDocumentoDownloadMapper(), numeroConteudo);
	}

	@Override
	public void deleteConteudo(String numeroConteudo) {
		jdbcTemplate.update(CONTEUDO_DELETE_SQL, numeroConteudo);
	}

	@Override
	public String save(DocumentoId documentoId, DocumentoTemporario documentoTemporario) {
		InputStream stream = null;
		try {
			stream = documentoTemporario.stream();
			final InputStream blobStream = stream;

			PreparedStatementCreator psc = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(CONTEUDO_INSERT_SQL,
							new String[] { CONTEUDO_ID_COLUMN });
					ps.setBlob(1, blobStream);
					return ps;
				}
			};

			GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(psc, keyHolder);

			return String.valueOf(keyHolder.getKey().longValue());
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

}