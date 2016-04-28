package br.jus.stf.plataforma.documento.infra;

import java.io.IOException;

import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

import br.jus.stf.plataforma.documento.domain.ContadorPaginas;
import br.jus.stf.plataforma.documento.domain.model.DocumentoTemporario;

public class ContadorPaginasPdf implements ContadorPaginas {

	@Override
	public Integer contarPaginas(DocumentoTemporario docTemp) {
		try {
			RandomAccessFileOrArray pdfFile = new RandomAccessFileOrArray(
			        new RandomAccessSourceFactory().createSource(docTemp.randomAccessFile()));
			PdfReader reader = new PdfReader(pdfFile, new byte[0]);
			int paginas = reader.getNumberOfPages();
			reader.close();
			return paginas;
		} catch (IOException e) {
			throw new RuntimeException("Erro ao contar a quantidade de páginas do documento.", e);
		}
	}

}