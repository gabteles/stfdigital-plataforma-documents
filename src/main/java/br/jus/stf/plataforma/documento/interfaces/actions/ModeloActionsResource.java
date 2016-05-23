package br.jus.stf.plataforma.documento.interfaces.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;

import br.jus.stf.plataforma.documento.application.ModeloApplicationService;
import br.jus.stf.plataforma.documento.application.command.CriarModeloCommand;
import br.jus.stf.plataforma.documento.application.command.EditarModeloCommand;
import br.jus.stf.plataforma.documento.domain.model.Modelo;

/**
 * Ações de Modelo.
 * 
 * @author Tomas.Godoi
 *
 */
@RestController
@RequestMapping("/api/documentos")
public class ModeloActionsResource {

	@Autowired
	private ModeloApplicationService modeloApplicationService;

	/**
	 * Ação para criar um novo modelo.
	 * 
	 * @param command
	 * @return
	 */
	@ApiOperation("Cria um modelo")
	@RequestMapping(value = "criar-modelo", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public Long criarModelo(@RequestBody CriarModeloCommand command) {
		Modelo modelo = modeloApplicationService.handle(command);
		return modelo.identity().toLong();
	}

	/**
	 * Ação para editar um modelo existente.
	 * 
	 * @param command
	 * @return
	 */
	@ApiOperation("Edita um modelo")
	@RequestMapping(value = "editar-modelo", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public Long editarModelo(@RequestBody EditarModeloCommand command) {
		Modelo modelo = modeloApplicationService.handle(command);
		return modelo.identity().toLong();
	}

}
