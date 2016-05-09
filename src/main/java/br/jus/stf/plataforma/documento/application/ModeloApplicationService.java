package br.jus.stf.plataforma.documento.application;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.core.shared.documento.ModeloId;
import br.jus.stf.core.shared.documento.TipoDocumentoId;
import br.jus.stf.plataforma.documento.domain.ModeloFactory;
import br.jus.stf.plataforma.documento.domain.model.Modelo;
import br.jus.stf.plataforma.documento.domain.model.ModeloRepository;
import br.jus.stf.plataforma.documento.domain.model.TipoModelo;
import br.jus.stf.plataforma.documento.domain.model.TipoModeloRepository;

@Component
@Transactional
public class ModeloApplicationService {

	@Autowired
	private TipoModeloRepository tipoModeloRepository;

	@Autowired
	private ModeloRepository modeloRepository;

	@Autowired
	private ModeloFactory modeloFactory;

	public Modelo criarModelo(TipoDocumentoId tipoModeloId, String nome) {
		TipoModelo tipoModelo = tipoModeloRepository.findOne(tipoModeloId);
		ModeloId modeloId = modeloRepository.nextId();
		DocumentoId documentoId = modeloFactory.criarDocumentoModeloPadrao(tipoModeloId, nome);
		Modelo modelo = new Modelo(modeloId, tipoModelo, nome, documentoId);

		return modeloRepository.save(modelo);
	}

}
