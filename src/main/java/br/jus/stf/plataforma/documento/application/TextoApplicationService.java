package br.jus.stf.plataforma.documento.application;

import java.util.Arrays;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import br.jus.stf.core.framework.component.command.Command;
import br.jus.stf.core.framework.domaindrivendesign.ApplicationService;
import br.jus.stf.core.shared.documento.DocumentoId;
import br.jus.stf.core.shared.documento.ModeloId;
import br.jus.stf.core.shared.documento.TextoId;
import br.jus.stf.plataforma.documento.application.command.AssinarTextoCommand;
import br.jus.stf.plataforma.documento.application.command.ConcluirTextoCommand;
import br.jus.stf.plataforma.documento.application.command.GerarDocumentoComTagsCommand;
import br.jus.stf.plataforma.documento.application.command.GerarDocumentoFinalCommand;
import br.jus.stf.plataforma.documento.application.command.GerarTextoCommand;
import br.jus.stf.plataforma.documento.application.command.SalvarDocumentosCommand;
import br.jus.stf.plataforma.documento.domain.model.Documento;
import br.jus.stf.plataforma.documento.domain.model.DocumentoRepository;
import br.jus.stf.plataforma.documento.domain.model.Modelo;
import br.jus.stf.plataforma.documento.domain.model.ModeloRepository;
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
	private DocumentoApplicationService documentoApplicationService;
	
	@Command(description = "Editar Conteúdo do Modelo")
	public Texto handle(GerarTextoCommand command) {
		Modelo modelo = modeloRepository.findOne(new ModeloId(command.getModeloId()));
		GerarDocumentoComTagsCommand gerarDocumentoCommand = new GerarDocumentoComTagsCommand(modelo.documento().toLong(), command.getSubstituicoes());
		DocumentoId documentoId = documentoApplicationService.handle(gerarDocumentoCommand);
		
		TextoId textoId = textoRepository.nextId();
		Texto texto = new Texto(textoId, documentoId);
		
		texto = textoRepository.save(texto);
		
		return texto;
	}
	
	@Command(description = "Concluir Texto")
	public void handle(ConcluirTextoCommand command) {
		Texto texto = textoRepository.findOne(new TextoId(command.getTextoId()));
		GerarDocumentoFinalCommand gdfc = new GerarDocumentoFinalCommand(texto.documento().toLong());
		DocumentoId documentoFinal = documentoApplicationService.handle(gdfc);
		texto.associarDocumentoFinal(documentoFinal);
	}

	public void handle(AssinarTextoCommand command) {
		Texto texto = textoRepository.findOne(new TextoId(command.getTextoId()));
		SalvarDocumentosCommand salvarCommand = new SalvarDocumentosCommand();
		salvarCommand.setIdsDocumentosTemporarios(Arrays.asList(command.getDocumentoTemporarioId()));
		Map<String, DocumentoId> documentosSalvos = documentoApplicationService.handle(salvarCommand);
		Documento documentoNaoAssinado = documentoRepository.findOne(texto.documentoFinal());
		texto.associarDocumentoFinal(documentosSalvos.get(command.getDocumentoTemporarioId()));
		textoRepository.save(texto);
		documentoRepository.delete(documentoNaoAssinado);
	}

}
