package br.jus.stf.plataforma.documento.infra;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.plataforma.documento.domain.ControladorEdicaoDocumento;
import br.jus.stf.plataforma.documento.domain.model.Edicao;

@Component
public class ControladorEdicaoDocumentoImpl implements ControladorEdicaoDocumento {

	private Map<Long, Edicao> edicoes = Collections.synchronizedMap(new HashMap<>());
	
	@Override
	public Edicao gerarEdicao(DocumentoId id) {
		if (!edicoes.containsKey(id.toLong())) {
			edicoes.put(id.toLong(), new Edicao(UUID.randomUUID().toString()));
		}
		return edicoes.get(id.toLong());
	}

	@Override
	public void excluirEdicao(String numeroEdicao) {
		edicoes.entrySet().removeIf(e -> e.getValue().numero().equals(numeroEdicao));
	}

	@Override
	public Edicao recuperarEdicao(DocumentoId id) {
		return edicoes.get(id.toLong());
	}

	@Override
	public void ativarEdicao(String numeroEdicao) {
		edicoes.entrySet().stream().filter(e -> e.getValue().numero().equals(numeroEdicao))
			.forEach(e -> e.getValue().ativar());
	}

}
