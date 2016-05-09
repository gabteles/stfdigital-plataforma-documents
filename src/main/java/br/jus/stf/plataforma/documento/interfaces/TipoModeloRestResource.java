package br.jus.stf.plataforma.documento.interfaces;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;

import br.jus.stf.plataforma.documento.domain.model.TipoModeloRepository;
import br.jus.stf.plataforma.documento.interfaces.dto.TipoModeloDto;
import br.jus.stf.plataforma.documento.interfaces.dto.TipoModeloDtoAssembler;

@RestController
@RequestMapping("/api/tipos-modelo")
public class TipoModeloRestResource {

	@Autowired
	private TipoModeloRepository tipoModeloRepository;

	@Autowired
	private TipoModeloDtoAssembler tipoModeloDtoAssembler;

	@ApiOperation("Recupera os tipos de modelo cadastrados")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<TipoModeloDto> listar() {
		return tipoModeloRepository.findAll().stream().map(tm -> tipoModeloDtoAssembler.toDto(tm)).collect(Collectors.toList());
	}
}
