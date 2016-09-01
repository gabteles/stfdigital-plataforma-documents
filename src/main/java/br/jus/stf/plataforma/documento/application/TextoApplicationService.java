package br.jus.stf.plataforma.documento.application;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import br.jus.stf.core.framework.component.command.Command;
import br.jus.stf.core.framework.domaindrivendesign.ApplicationService;
import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.core.shared.documento.DocumentoTemporarioId;
import br.jus.stf.core.shared.documento.ModeloId;
import br.jus.stf.core.shared.documento.TextoId;
import br.jus.stf.plataforma.documento.application.command.AssinarTextoCommand;
import br.jus.stf.plataforma.documento.application.command.ConcluirTextoCommand;
import br.jus.stf.plataforma.documento.application.command.GerarTextoCommand;
import br.jus.stf.plataforma.documento.domain.ConversorDocumentoService;
import br.jus.stf.plataforma.documento.domain.DocumentoService;
import br.jus.stf.plataforma.documento.domain.model.Documento;
import br.jus.stf.plataforma.documento.domain.model.DocumentoRepository;
import br.jus.stf.plataforma.documento.domain.model.DocumentoTemporario;
import br.jus.stf.plataforma.documento.domain.model.Modelo;
import br.jus.stf.plataforma.documento.domain.model.ModeloRepository;
import br.jus.stf.plataforma.documento.domain.model.SubstituicaoTag;
import br.jus.stf.plataforma.documento.domain.model.Texto;
import br.jus.stf.plataforma.documento.domain.model.TextoRepository;

@ApplicationService
@Transactional
public class TextoApplicationService {

	@Autowired
	private ModeloRepository modeloRepository;
	
	@Autowired
	private TextoRepository textoRepository;
	
	@Autowired
	private DocumentoRepository documentoRepository;
	
	@Autowired
	private DocumentoService documentoService;
	
	@Autowired
	private ConversorDocumentoService conversorDocumentoService;
	
	@Command(description = "Editar Conteúdo do Modelo")
	public Texto handle(GerarTextoCommand command) {
		Modelo modelo = modeloRepository.findOne(new ModeloId(command.getModeloId()));
		
		List<SubstituicaoTag> substituicoesTag = command.getSubstituicoes().stream()
		        .map(std -> new SubstituicaoTag(std.getNome(), std.getValor())).collect(Collectors.toList());
		DocumentoId documentoId = documentoService.gerarDocumentoTemporarioComTags(modelo.documento(), substituicoesTag);
		
		TextoId textoId = textoRepository.nextId();
		Texto texto = new Texto(textoId, documentoId);
		
		texto = textoRepository.save(texto);
		
		return texto;
	}
	
	@Command(description = "Concluir Texto")
	public void handle(ConcluirTextoCommand command) {
		Texto texto = textoRepository.findOne(new TextoId(command.getTextoId()));
		DocumentoId documentoFinal = gerarDocumentoFinal(texto.documento());
		texto.associarDocumentoFinal(documentoFinal);
	}
	
	private DocumentoId gerarDocumentoFinal(DocumentoId documento) {
		DocumentoTemporario documentoTemporario = conversorDocumentoService.converterDocumentoFinal(documento);
		String documentoTemporarioId = documentoService.salvarDocumentoTemporario(documentoTemporario);
		return documentoService.salvar(new DocumentoTemporarioId(documentoTemporarioId));
	}

	public void handle(AssinarTextoCommand command) {
		Texto texto = textoRepository.findOne(new TextoId(command.getTextoId()));
		DocumentoId documentoSalvo = documentoService.salvar(new DocumentoTemporarioId(command.getDocumentoTemporarioId()));
		Documento documentoNaoAssinado = documentoRepository.findOne(texto.documentoFinal());
		texto.associarDocumentoFinal(documentoSalvo);
		textoRepository.save(texto);
		documentoRepository.delete(documentoNaoAssinado);
	}

}
