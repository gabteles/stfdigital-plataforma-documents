package br.jus.stf.plataforma.documento.interfaces.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;

import br.jus.stf.core.shared.documento.TipoDocumentoId;
import br.jus.stf.plataforma.documento.application.ModeloApplicationService;
import br.jus.stf.plataforma.documento.domain.model.Modelo;
import br.jus.stf.plataforma.documento.interfaces.commands.CriarModeloCommand;

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

	@ApiOperation("Cria um modelo")
	@RequestMapping(value = "criar-modelo", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public Long criarModelo(@RequestBody CriarModeloCommand command) {
		Modelo modelo = modeloApplicationService.criarModelo(new TipoDocumentoId(command.getTipoModelo()),
		        command.getNome());
		return modelo.identity().toLong();
	}

}
