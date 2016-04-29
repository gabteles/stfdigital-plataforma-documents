package br.jus.stf.plataforma.documento.domain.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;

public class DocumentoTemporarioUnitTests {
	
	private MockMultipartFile mockMultiFile;
	
	@Before
	public void setUp() throws IOException {
		mockMultiFile = new MockMultipartFile("file", "archimate.pdf", "application/pdf",
				getClass().getResourceAsStream("/pdf/archimate.pdf"));
	}
	
	@Test
	public void criaDocumentoValido() throws IOException {
		DocumentoTemporario documentoTemporario = new DocumentoTemporario(mockMultiFile);

	    assertNotNull(documentoTemporario);
		assertEquals(documentoTemporario.contentType(), "application/pdf");
		assertEquals(documentoTemporario.tamanho(), new Long(733596));
		assertTrue(IOUtils.contentEquals(mockMultiFile.getInputStream(), documentoTemporario.stream()));
	}
	
	@Test(expected=NullPointerException.class)
	public void criaDocumentoTemporarioComFileNulo() {
		new DocumentoTemporario(null);
	}
	
	@Test
	public void comparaDocumentoTemporarioDiferentes() throws IOException {
		DocumentoTemporario documentoTemporario1 = new DocumentoTemporario(mockMultiFile);
		
		mockMultiFile = new MockMultipartFile("file2", "padraoAD-V2.pdf", "application/pdf",
				IOUtils.toByteArray(getClass().getResourceAsStream("/pdf/padraoAD-V2.pdf")));
		DocumentoTemporario documentoTemporario2 = new DocumentoTemporario(mockMultiFile);
		
		assertFalse(documentoTemporario1.equals(documentoTemporario2));
	}
	
	@Test
	public void comparaDocumentoTemporarioComValoresDiferentes() throws IOException {
		DocumentoTemporario documentoTemporario1 = new DocumentoTemporario(mockMultiFile);

		mockMultiFile = new MockMultipartFile("file2", "padraoAD-V2.pdf", "application/pdf",
				IOUtils.toByteArray(getClass().getResourceAsStream("/pdf/padraoAD-V2.pdf")));
		DocumentoTemporario documentoTemporario2 = new DocumentoTemporario(mockMultiFile);
		
		assertFalse(documentoTemporario1.sameValueAs(documentoTemporario2));
	}
	
	@Test
	public void comparaDocumentoTemporarioComHashesDiferentes() throws IOException {
		DocumentoTemporario documentoTemporario1 = new DocumentoTemporario(mockMultiFile);
		
		mockMultiFile = new MockMultipartFile("file2", "padraoAD-V2.pdf", "application/pdf",
				getClass().getResourceAsStream("/pdf/padraoAD-V2.pdf"));
		DocumentoTemporario documentoTemporario2 = new DocumentoTemporario(mockMultiFile);
		
		assertFalse(documentoTemporario1.hashCode() == documentoTemporario2.hashCode());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testaExclusaoDocumentoTemporario() {
		DocumentoTemporario documentoTemporario = new DocumentoTemporario(mockMultiFile);
		
		documentoTemporario.delete();
		documentoTemporario.stream();
	}

}
