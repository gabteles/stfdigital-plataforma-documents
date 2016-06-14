package br.jus.stf.plataforma.documento.application;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.jus.stf.core.framework.component.command.Command;
import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.core.shared.documento.ModeloId;
import br.jus.stf.core.shared.documento.TipoDocumentoId;
import br.jus.stf.plataforma.documento.application.command.CriarModeloCommand;
import br.jus.stf.plataforma.documento.application.command.EditarModeloCommand;
import br.jus.stf.plataforma.documento.domain.ModeloFactory;
import br.jus.stf.plataforma.documento.domain.model.Modelo;
import br.jus.stf.plataforma.documento.domain.model.ModeloRepository;
import br.jus.stf.plataforma.documento.domain.model.TipoDocumento;
import br.jus.stf.plataforma.documento.domain.model.TipoDocumentoRepository;

/**
 * Application Service de Modelos.
 * 
 * @author tomas.godoi
 *
 */
@Component
@Transactional
public class ModeloApplicationService {

	@Autowired
	private TipoDocumentoRepository tipoDocumentoRepository;

	@Autowired
	private ModeloRepository modeloRepository;

	@Autowired
	private ModeloFactory modeloFactory;

	/**
	 * Cria um novo modelo.
	 * 
	 * @param command
	 * @return
	 */
	@Command(description = "Modelos - Criação")
	public Modelo handle(CriarModeloCommand command) {
		TipoDocumento tipoDocumento = tipoDocumentoRepository.findOne(new TipoDocumentoId(command.getTipoDocumento()));
		ModeloId modeloId = modeloRepository.nextId();
		DocumentoId documentoId = modeloFactory.criarDocumentoModeloPadrao(tipoDocumento.identity(), command.getNome());
		Modelo modelo = new Modelo(modeloId, tipoDocumento, command.getNome(), documentoId);

		return modeloRepository.save(modelo);
	}

	/**
	 * Edita um modelo existente.
	 * 
	 * @param command
	 * @return
	 */
	@Command(description = "Modelos - Edição")
	public Modelo handle(EditarModeloCommand command) {
		Modelo modelo = modeloRepository.findOne(new ModeloId(command.getId()));
		TipoDocumento tipoDocumento = tipoDocumentoRepository.findOne(new TipoDocumentoId(command.getTipoDocumento()));
		
		modelo.editar(tipoDocumento, command.getNome());

		return modeloRepository.save(modelo);
	}

}
