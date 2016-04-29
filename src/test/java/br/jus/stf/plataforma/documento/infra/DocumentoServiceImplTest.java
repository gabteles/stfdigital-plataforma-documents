package br.jus.stf.plataforma.documento.infra;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.jus.stf.core.shared.documento.PDFMultipartFile;
import br.jus.stf.plataforma.documento.domain.ContadorPaginas;
import br.jus.stf.plataforma.documento.domain.model.DocumentoTemporario;

/**
 * Testes da service
 * 
 * @author Tomas.Godoi
 *
 */
public class DocumentoServiceImplTest {

	@InjectMocks
	private DocumentoServiceImpl documentoPdfService;

	@Spy
	private ContadorPaginas contadorPaginas = new ContadorPaginasStrategyConfiguration().contadorPaginas();

	private DocumentoTemporario documentoPdf;

	@Before
	public void setUp() throws IOException {
		MockitoAnnotations.initMocks(this);
		documentoPdf = criarDocumentoPdf();
	}

	private DocumentoTemporario criarDocumentoPdf() throws IOException {
		InputStream is = getClass().getResourceAsStream("/pdf/archimate.pdf");
		return new DocumentoTemporario(new PDFMultipartFile("arquivo.pdf", IOUtils.toByteArray(is)));
	}

	@Test
	public void testContarPaginas() {
		Integer quantidadePaginas = documentoPdfService.contarPaginas(documentoPdf);
		Assert.assertEquals("Quantidade de páginas deveria ser 42.", new Integer(42), quantidadePaginas);
	}
}
