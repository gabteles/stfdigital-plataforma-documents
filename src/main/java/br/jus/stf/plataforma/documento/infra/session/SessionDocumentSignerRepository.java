package br.jus.stf.plataforma.documento.infra.session;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.jus.stf.plataforma.documento.domain.model.signature.DocumentSigner;
import br.jus.stf.plataforma.documento.domain.model.signature.DocumentSignerId;
import br.jus.stf.plataforma.documento.domain.model.signature.DocumentSignerRepository;

@Component
public class SessionDocumentSignerRepository implements DocumentSignerRepository {

	@Override
	public DocumentSigner save(DocumentSigner signer) {
		DocumentSignerId signerId = signer.id();
		session().setAttribute(signerId.id(), signer);
		return signer;
	}

	@Override
	public DocumentSigner findOne(DocumentSignerId signerId) {
		DocumentSigner signer = (DocumentSigner) session().getAttribute(signerId.id());
		if (signer != null) {
			return signer;
		} else {
			return null;
		}
	}

	public HttpSession session() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return sra.getRequest().getSession();
	}

	@Override
	public DocumentSignerId nextId() {
		return new DocumentSignerId(UUID.randomUUID().toString());
	}
}
