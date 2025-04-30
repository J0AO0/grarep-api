package com.grarep.grarep.service.rels;

import com.grarep.grarep.domain.dto.rel.PedidoRelatorioDTO;
import com.grarep.grarep.repository.PedidoRepository;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class RelatorioPedidoService {

    @Autowired
    private PedidoRepository repoPed;

    public byte[] pedidosPorRepresentante(Integer ano, Integer mes) throws Exception {
        // Buscar dados filtrados por ano e mês para o relatório
        List<PedidoRelatorioDTO> dados = repoPed.findByAnoMesForRelatorio(ano, mes);

        // Configurar parâmetros do relatório
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("MES", mes);
        parametros.put("ANO", ano);
        parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

        // Carregar o template do relatório
        InputStream inputStream = this.getClass().getResourceAsStream(
                "/rel/relPedidos.jasper");

        // Criar fonte de dados a partir da lista de DTOs
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dados, false);

        // Preencher o relatório
        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, dataSource);

        // Exportar para PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}