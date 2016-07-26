package br.jus.stf.plataforma.documento.application;

import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.X509Certificate;

import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;

import br.jus.stf.plataforma.AbstractIntegrationTests;
import br.jus.stf.plataforma.documento.application.SignatureApplicationService;
import br.jus.stf.plataforma.documento.domain.PdfInputStreamDocument;
import br.jus.stf.plataforma.documento.domain.model.Document;
import br.jus.stf.plataforma.documento.domain.model.pki.PkiIds;
import br.jus.stf.plataforma.documento.domain.model.pki.PkiType;
import br.jus.stf.plataforma.documento.domain.model.signature.DocumentSignerId;
import br.jus.stf.plataforma.documento.domain.model.signature.HashSignature;
import br.jus.stf.plataforma.documento.domain.model.signature.PreSignature;
import br.jus.stf.plataforma.documento.domain.model.signature.SignedDocument;
import br.jus.stf.plataforma.documento.infra.session.SessionDocumentSignerRepository;
import br.jus.stf.plataforma.documento.support.pki.PlataformaUnitTestingUser;
import br.jus.stf.plataforma.documento.support.util.SignatureTestUtil;

public class SignatureApplicationServiceIntegrationTests extends AbstractIntegrationTests {

	private static final String PDF_DE_TESTE = "pdf-de-teste-01.pdf";

	private static final String SIGNING_REASON = "Unit Test STF Digital";

	@Spy
	private SessionDocumentSignerRepository documentSignerRepository;

	@InjectMocks
	@Autowired
	private SignatureApplicationService signatureService;

	private HttpSession session;

	private PlataformaUnitTestingUser unitTestingUser;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		session = new MockHttpSession();
		Mockito.doReturn(session).when(documentSignerRepository).session();

		unitTestingUser = PlataformaUnitTestingUser.instance();
	}

	@Test
	public void testSign() throws Exception {
		DocumentSignerId signerId;

		// Passo 1 - Server-side: Criar um contexto de assinatura a partir do
		// SignatureService.
		X509Certificate certificate = unitTestingUser.userStore().certificate();
		PkiIds ids = new PkiIds(PkiType.ICP_PLATAFORMA.id());
		signerId = signatureService.prepareToSign(certificate, ids, SIGNING_REASON);

		// Passo 2 - Server-side: Receber o documento para assinatura.
		Document document = new PdfInputStreamDocument(SignatureTestUtil.getDocumentToSign(PDF_DE_TESTE));
		signatureService.attachToSign(signerId, document);

		// Passo 3 - Server-side: Pré-assinar, gerando o hash
		PreSignature preSignature = signatureService.preSign(signerId);

		// Passo 4 - Client-side: Assinar o hash gerado
		HashSignature signature = sign(preSignature, unitTestingUser.userStore().keyPair().getPrivate());

		// Passo 5 - Server-side: Pós-assinar, gerando o documento
		signatureService.postSign(signerId, signature);

		// Passo 6 - Server-side: Recuperar o documento assinado
		SignedDocument signedDocument = signatureService.recoverSignedDocument(signerId);
		Assert.assertNotNull(signedDocument);
	}

	private HashSignature sign(PreSignature preSignature, PrivateKey key) throws Exception {
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initSign(key);
		signature.update(preSignature.auth().authAsBytes());

		byte[] signed = signature.sign();

		return new HashSignature(signed);
	}

}
