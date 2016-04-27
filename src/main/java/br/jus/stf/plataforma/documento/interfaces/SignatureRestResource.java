package br.jus.stf.plataforma.documento.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.X509Certificate;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wordnik.swagger.annotations.ApiOperation;

import br.jus.stf.core.shared.documento.DocumentoTemporarioId;
import br.jus.stf.plataforma.documento.application.SignatureApplicationService;
import br.jus.stf.plataforma.documento.domain.PdfTempDocument;
import br.jus.stf.plataforma.documento.domain.model.certificate.CertificateUtils;
import br.jus.stf.plataforma.documento.domain.model.pki.PkiIds;
import br.jus.stf.plataforma.documento.domain.model.pki.PkiType;
import br.jus.stf.plataforma.documento.domain.model.signature.DocumentSignerId;
import br.jus.stf.plataforma.documento.domain.model.signature.HashSignature;
import br.jus.stf.plataforma.documento.domain.model.signature.PreSignature;
import br.jus.stf.plataforma.documento.domain.model.signature.SignedDocument;
import br.jus.stf.plataforma.documento.domain.model.signature.SigningException;
import br.jus.stf.plataforma.documento.interfaces.commands.PostSignCommand;
import br.jus.stf.plataforma.documento.interfaces.commands.PreSignCommand;
import br.jus.stf.plataforma.documento.interfaces.commands.PrepareCommand;
import br.jus.stf.plataforma.documento.interfaces.commands.ProvideToSignCommand;
import br.jus.stf.plataforma.documento.interfaces.dto.PreSignatureDto;
import br.jus.stf.plataforma.documento.interfaces.dto.SignedDocumentDto;
import br.jus.stf.plataforma.documento.interfaces.dto.SignerDto;

/**
 * Serviços REST para a assinatura de documentos.
 * 
 * @author Tomas.Godoi
 *
 */
@RestController
@RequestMapping("/api/certification/signature")
public class SignatureRestResource {

	private static final String SIGNING_REASON = "Assinatura de documentos STF Digital";

	@Autowired
	private SignatureApplicationService signatureApplicationService;

	@ApiOperation("Cria um novo contexto de assinatura com o certificado.")
	@RequestMapping(value = "/prepare", method = RequestMethod.POST)
	public SignerDto prepare(@RequestBody PrepareCommand command) {
		// Converte o certificado recebido para o objeto da classe
		// X509Certificate.
		X509Certificate certificate;
		
		try {
			certificate = CertificateUtils
					.bytesToCertificate(Hex.decodeHex(command.getCertificateAsHex().toCharArray()));
		} catch (DecoderException e) {
			throw new RuntimeException(e);
		}
		
		// Prepara uma assinador de documentos.
		DocumentSignerId signerId;
		
		try {
			signerId = signatureApplicationService.prepareToSign(certificate, pkis(), SIGNING_REASON);
		} catch (SigningException e) {
			throw new RuntimeException(e);
		}
		
		return new SignerDto(signerId.id());
	}

	private PkiIds pkis() {
		return new PkiIds(PkiType.ICP_BRASIL.id(), PkiType.ICP_PLATAFORMA.id());
	}

	@ApiOperation("Faz o upload do arquivo para assinatura.")
	@RequestMapping(value = "/upload-to-sign", method = RequestMethod.POST)
	public void uploadToSign(@RequestHeader("Signer-Id") String signerId, @RequestParam("file") MultipartFile file) {
		
		try {
			signatureApplicationService.attachToSign(new DocumentSignerId(signerId),
					new PdfTempDocument(file.getInputStream()));
		} catch (SigningException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	@ApiOperation("Fornece um arquivo já existente no servidor para assinatura.")
	@RequestMapping(value = "/provide-to-sign", method = RequestMethod.POST)
	public void provideToSign(@RequestBody ProvideToSignCommand command) throws SigningException {
		signatureApplicationService.provideToSign(new DocumentSignerId(command.getSignerId()), command.getDocumentId());
	}

	@ApiOperation("Gera o hash do documento a ser assinado.")
	@RequestMapping(value = "/pre-sign", method = RequestMethod.POST)
	public PreSignatureDto preSign(@RequestBody PreSignCommand command) throws SigningException {
		PreSignature preSignature = signatureApplicationService.preSign(new DocumentSignerId(command.getSignerId()));
		return PreSignatureDto.from(preSignature);
	}

	@ApiOperation("Assina efetivamente o documento.")
	@RequestMapping(value = "/post-sign", method = RequestMethod.POST)
	public void postSign(@RequestBody PostSignCommand command) throws SigningException {
		signatureApplicationService.postSign(new DocumentSignerId(command.getSignerId()),
				new HashSignature(command.getSignatureAsHex()));
	}

	@ApiOperation("Recupera o documento assinado.")
	@RequestMapping(value = "/download-signed/{signerId}")
	public void downloadSigned(@PathVariable("signerId") String signerId, HttpServletResponse response)
			throws IOException {
		SignedDocument signedDocument = signatureApplicationService
				.recoverSignedDocument(new DocumentSignerId(signerId));
		InputStream is = signedDocument.document().stream();

		response.setHeader("Content-disposition", "attachment; filename=" + signerId + ".pdf");
		response.setContentType("application/pdf");
		response.setHeader("Content-Length", String.valueOf(is.available()));

		IOUtils.copy(is, response.getOutputStream());
		IOUtils.closeQuietly(is);
		response.flushBuffer();
	}

	@ApiOperation("Salva o documento assinado no contexto de documentos.")
	@RequestMapping(value = "/save-signed/{signerId}", method = RequestMethod.POST)
	public SignedDocumentDto saveSigned(@PathVariable("signerId") String signerId) throws IOException {
		DocumentoTemporarioId documentId = signatureApplicationService.saveSigned(new DocumentSignerId(signerId));
		return new SignedDocumentDto(documentId.toString());
	}

}