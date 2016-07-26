package br.jus.stf.plataforma.documento.application;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.jus.stf.plataforma.AbstractIntegrationTests;
import br.jus.stf.plataforma.documento.application.DocumentValidatorApplicationService;
import br.jus.stf.plataforma.documento.domain.PdfInputStreamDocument;
import br.jus.stf.plataforma.documento.domain.model.Document;
import br.jus.stf.plataforma.documento.domain.model.pki.PkiIds;
import br.jus.stf.plataforma.documento.domain.model.pki.PkiType;
import br.jus.stf.plataforma.documento.domain.model.validation.DocumentValidation;
import br.jus.stf.plataforma.documento.support.util.SignatureTestUtil;

public class DocumentValidatorApplicationServiceIntegrationTests extends AbstractIntegrationTests {

	private static final String PDF_DE_TESTE_ASSINADO = "pdf-de-teste-assinado-02.pdf";
	private static final String PDF_DE_TESTE_NAO_ASSINADO = "pdf-de-teste-01.pdf";

	@Autowired
	private DocumentValidatorApplicationService documentValidatorApplicationService;

	@Before
	public void setUp() {
	}

	@Test
	public void testValidateValidPdf() throws Exception {
		Document document = new PdfInputStreamDocument(SignatureTestUtil.getDocumentToSign(PDF_DE_TESTE_ASSINADO));
		PkiIds pkiIds = new PkiIds(PkiType.ICP_PLATAFORMA.id(), PkiType.ICP_BRASIL.id());
		DocumentValidation documentValidation = documentValidatorApplicationService.validateDocumentSignature(document,
				pkiIds);
		Assert.assertTrue(documentValidation.valid());
	}

	@Test
	public void testValidateNotSignedPdf() throws Exception {
		Document document = new PdfInputStreamDocument(SignatureTestUtil.getDocumentToSign(PDF_DE_TESTE_NAO_ASSINADO));
		PkiIds pkiIds = new PkiIds(PkiType.ICP_PLATAFORMA.id());
		DocumentValidation documentValidation = documentValidatorApplicationService.validateDocumentSignature(document,
				pkiIds);
		Assert.assertFalse(documentValidation.valid());
	}
	
}
