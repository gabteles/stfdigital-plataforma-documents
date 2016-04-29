package br.jus.stf.plataforma.documento.infra;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.jus.stf.plataforma.documento.domain.model.ConteudoDocumento;
import br.jus.stf.plataforma.documento.domain.model.DocumentoTemporario;
import br.jus.stf.plataforma.documento.domain.model.SubstituicaoTag;
import br.jus.stf.plataforma.documento.domain.model.Tag;

/**
 * Testes da service
 * 
 * @author Tomas.Godoi
 *
 */
public class DocumentoServiceImplDocxTest {

	private DocumentoServiceImpl documentoService;

	private ConteudoDocumento documentoDocx;

	@Before
	public void setUp() throws IOException {
		documentoService = new DocumentoServiceImpl();
		documentoDocx = criarDocumentoDocx();
	}

	private ConteudoDocumento criarDocumentoDocx() throws IOException {
		InputStream is = getClass()
		        .getResourceAsStream("/documentos/modelos/modelo-oficio-devolucao-remessa-indevida.docx");
		byte[] bytes = IOUtils.toByteArray(is);
		return new ConteudoDocumento(bytes, new Long(bytes.length));
	}

	@Test
	public void testExtrairTags() {
		List<Tag> tags = documentoService.extrairTags(documentoDocx);
		Assert.assertEquals("Quantidade de tags deveria ser 6.", 6, tags.size());
		Assert.assertEquals("Primeira tag deveria ser Número do Ofício.", "Número do Ofício", tags.get(0).nome());
		Assert.assertEquals("Última tag deveria ser Destinatário.", "Destinatário", tags.get(tags.size() - 1).nome());
	}

	@Test
	public void preencherTags() throws IOException {
		List<SubstituicaoTag> substituicoes = Arrays.asList(new SubstituicaoTag("Número do Ofício", "123"),
		        new SubstituicaoTag("Data Atual", "10 de Março de 2016"),
		        new SubstituicaoTag("Número da Petição", "17"), new SubstituicaoTag("Vocativo", "Senhor Secretário"),
		        new SubstituicaoTag("Secretário Judiciário", "João da Silva"),
		        new SubstituicaoTag("Destinatário", "Ao Secretário da Secretaria Judiciária do STJ"));
		DocumentoTemporario documentoPreenchido = documentoService.preencherTags(substituicoes, documentoDocx);
		Assert.assertNotNull(documentoPreenchido);
		byte[] bytes = IOUtils.toByteArray(documentoPreenchido.stream());
		List<Tag> tags = documentoService.extrairTags(new ConteudoDocumento(bytes, new Long(bytes.length)));
		Assert.assertEquals("Quantidade de tags deveria ser 0.", 0, tags.size());
	}

}
