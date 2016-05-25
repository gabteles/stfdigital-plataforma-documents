package br.jus.stf.plataforma.documento.interfaces.actions;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;

import br.jus.stf.plataforma.documento.application.TextoApplicationService;
import br.jus.stf.plataforma.documento.application.command.GerarTextoPeticaoCommand;
import br.jus.stf.plataforma.documento.domain.model.Texto;
import br.jus.stf.plataforma.documento.interfaces.dto.TextoDto;

/**
 * Ações de textos.
 * 
 * @author Tomas.Godoi
 *
 */
@RestController
@RequestMapping("/api/textos")
public class TextoActionsResource {

	@Autowired
	private TextoApplicationService textoApplicationService;

	@ApiOperation("Gera um Texto")
	@RequestMapping(value = "gerar-texto", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public TextoDto gerarTextoDePeticao(@RequestBody @Valid GerarTextoPeticaoCommand command) {
		Texto texto = textoApplicationService.handle(command);
		return new TextoDto(texto.identity().toLong(), texto.documento().toLong());
	}

}
