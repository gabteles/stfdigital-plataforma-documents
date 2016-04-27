package br.jus.stf.plataforma.documento.infra;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.jus.stf.plataforma.documento.domain.model.certificate.Certificate;
import br.jus.stf.plataforma.documento.domain.model.certificate.CertificateType;
import br.jus.stf.plataforma.documento.domain.model.certificate.CertificateUtils;
import br.jus.stf.plataforma.documento.domain.model.pki.Pki;
import br.jus.stf.plataforma.documento.domain.model.pki.PkiId;
import br.jus.stf.plataforma.documento.domain.model.pki.PkiRepository;
import br.jus.stf.plataforma.documento.domain.model.pki.PkiType;
import br.jus.stf.plataforma.documento.domain.model.pki.ReadOnlyPki;
import br.jus.stf.plataforma.documento.infra.pki.PlataformaPki;

@Repository
public class PkiRepositoryImpl implements PkiRepository {

	@Autowired
	private EntityManager entityManager;

	@Override
	public Pki findOne(PkiId pkiId) {
		if (isPlataformaPki(pkiId)) {
			return PlataformaPki.instance();
		}
		TypedQuery<Certificate> query = entityManager.createQuery("from Certificate c where c.pki = :pki",
				Certificate.class);
		query.setParameter("pki", PkiType.valueOf(pkiId.id()));
		List<Certificate> certificates = query.getResultList();
		List<X509Certificate> rootCerts = new ArrayList<>();
		List<X509Certificate> intermediateCerts = new ArrayList<>();
		for (Certificate c : certificates) {
			if (c.certificateType() == CertificateType.R) {
				rootCerts.add(CertificateUtils.bytesToCertificate(c.content()));
			} else if (c.certificateType() == CertificateType.A) {
				intermediateCerts.add(CertificateUtils.bytesToCertificate(c.content()));
			}
		}
		
		return new ReadOnlyPki(pkiId, rootCerts, intermediateCerts);
	}

	private boolean isPlataformaPki(PkiId pkiId) {
		return pkiId.equals(PkiType.ICP_PLATAFORMA.id());
	}

	@Override
	public Pki[] findAll(PkiId[] ids) {
		List<Pki> pkis = new ArrayList<>();
		for (PkiId id : ids) {
			pkis.add(findOne(id));
		}
		return pkis.toArray(new Pki[0]);
	}

}
