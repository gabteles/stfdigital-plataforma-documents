package br.jus.stf.db.migration;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.stereotype.Component;

/**
 * Carga dos certificados digitais.
 * 
 * @author Rafael Alencar
 *
 */
@Component
public class V1_4__Carga_certificado_digital implements SpringJdbcMigration {

    @Autowired
    private ResourceLoader resourceLoader;
    
    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        String command =
                "insert into documents.certificado_digital (SEQ_CERTIFICADO_DIGITAL, DSC_CERTIFICADO_DIGITAL, COD_SERIAL, TXT_CERTIFICADO_DIGITAL, DAT_VALIDADE_INICIAL, DAT_VALIDADE_FINAL, SEQ_CERTIFICADO_EMISSOR, TIP_CERTIFICADO_DIGITAL, TIP_PKI) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        List<String> certificadosParameters = Arrays.asList(
                "cn=Autoridade Certificadora Raiz Brasileira v2 ou=Instituto Nacional de Tecnologia da Informacao – ITI o=ICP-Brasil c=BR, 01, classpath:certification/pkis/icp-brasil/acrb-v2.cer, 21/06/2010 17:04:57, 21/06/2023 17:04:57, 0, R, ICP_BRASIL",
                "cn=Autoridade Certificadora da Justica v4 ou=Autoridade Certificadora Raiz Brasileira v2 o=ICP-Brasil c=BR, 09, classpath:certification/pkis/icp-brasil/ac-jus-v4.cer, 22/11/2011 15:15:01, 22/11/2021 15:15:01, 1, A, ICP_BRASIL",
                "cn=AC CAIXA-JUS v2 ou=Autoridade Certificadora da Justica - AC-JUS o=ICP-Brasil c=BR, 03, classpath:certification/pkis/icp-brasil/ac-caixa-jus-v2.cer, 28/12/2011 11:03:21, 28/12/2019 11:03:21, 1, A, ICP_BRASIL",
                "cn=AC CAIXA v2 ou=Autoridade Certificadora Raiz Brasileira v2 o=ICP-Brasil c=BR, 0b, classpath:certification/pkis/icp-brasil/ac-caixa-v2.cer, 02/12/2011 10:16:53, 02/12/2021 10:16:53, 3, A, ICP_BRASIL",
                "cn=AC CAIXA PF v2 ou=Caixa Economica Federal o=ICP-Brasil c=BR, 28eea57c362904d8, classpath:certification/pkis/icp-brasil/ac-caixa-pf-v2.cer, 23/12/2011 11:52:58, 21/12/2019 11:52:58, 1, A, ICP_BRASIL");

        for (String certificadoParameters : certificadosParameters) {
            Long certificadoId =
                    jdbcTemplate.queryForObject("SELECT documents.seq_certificado_digital.NEXTVAL FROM DUAL",
                            Long.class);
            String[] parameters = certificadoParameters.split(", ", -1);

            salvarCertificado(jdbcTemplate, command, certificadoId, parameters);
        }
    }

    private void salvarCertificado(JdbcTemplate jdbcTemplate, String command, Long certificadoId, String[] parameters)
            throws IOException, ParseException {
        InputStream stream = null;

        try {
            stream = resourceLoader.getResource(parameters[2]).getInputStream();
            SqlLobValue arquivo = new SqlLobValue(stream, stream.available());
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date validadeInicio = formato.parse(parameters[3]);
            Date validadeFim = formato.parse(parameters[4]);
            Long certificadoPai = certificadoId - Integer.parseInt(parameters[5]);

            jdbcTemplate.update(command,
                    new Object[] { certificadoId, parameters[0], parameters[1], arquivo, validadeInicio,
                            validadeFim, certificadoPai, parameters[6],
                            parameters[7] },
                    new int[] { Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.CLOB, Types.DATE, Types.DATE,
                            Types.BIGINT, Types.VARCHAR, Types.VARCHAR });
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

}
