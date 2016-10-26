package br.jus.stf.plataforma.documento.interfaces;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;

import br.jus.stf.plataforma.documento.domain.model.TipoDocumentoRepository;
import br.jus.stf.plataforma.documento.interfaces.dto.TipoDocumentoDto;
import br.jus.stf.plataforma.documento.interfaces.dto.TipoDocumentoDtoAssembler;

@RestController
@RequestMapping("/api/tipos-documento")
public class TipoDocumentoRestResource {

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Autowired
    private TipoDocumentoDtoAssembler tipoDocumentoDtoAssembler;

    @ApiOperation("Recupera os tipos de modelo cadastrados")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<TipoDocumentoDto> listar() {
        return tipoDocumentoRepository.findAll().stream()
                .map(tipoDocumentoDtoAssembler::toDto)
                .collect(Collectors.toList());
    }
}
