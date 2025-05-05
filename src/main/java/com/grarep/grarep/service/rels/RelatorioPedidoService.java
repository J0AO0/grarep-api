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
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class RelatorioPedidoService {

    @Autowired
    private PedidoRepository repoPed;

    public byte[] pedidosPorRepresentante(Integer ano, Integer mes) throws Exception {
        // Log dos parâmetros recebidos
        System.out.println("RelatorioPedidoService: Processando com mes=" + mes + ", ano=" + ano);

        // Validação interna
        if (mes < 1 || mes > 12 || ano < 2000) {
            System.out.println("Validação interna falhou: mês=" + mes + ", ano=" + ano);
            throw new IllegalArgumentException("Mês (1-12) ou ano (>=2000) inválidos");
        }

        // Buscar dados brutos
        List<Object[]> rawDados = repoPed.findByAnoMesForRelatorioRaw(ano, mes);
        System.out.println("Dados brutos retornados: " + rawDados.size());

        // Log detalhado dos dados brutos
        for (Object[] row : rawDados) {
            System.out.println("Raw Data: " +
                    "representante=" + (row[0] != null ? row[0] : "NULL") +
                    ", pedidoId=" + (row[1] != null ? row[1] : "NULL") +
                    ", nf=" + (row[2] != null ? row[2] : "NULL") +
                    ", arquiteto=" + (row[3] != null ? row[3] : "NULL") +
                    ", quantidade=" + (row[4] != null ? row[4] : "NULL") +
                    ", nomeProduto=" + (row[5] != null ? row[5] : "NULL") +
                    ", skuProduto=" + (row[6] != null ? row[6] : "NULL") +
                    ", dataGravacao=" + (row[7] != null ? row[7] : "NULL"));
        }

        // Mapear dados brutos para PedidoRelatorioDTO
        List<PedidoRelatorioDTO> dados = new ArrayList<>();
        for (Object[] row : rawDados) {
            PedidoRelatorioDTO dto = new PedidoRelatorioDTO(
                    row[0] != null ? row[0].toString() : null, // representante
                    row[1] != null ? ((Number) row[1]).intValue() : null, // pedidoId
                    row[2] != null ? ((Number) row[2]).intValue() : null, // nf
                    row[3] != null ? row[3].toString() : null, // arquiteto
                    row[4] != null ? new BigDecimal(row[4].toString()) : null, // quantidade
                    row[5] != null ? row[5].toString() : null, // nomeProduto
                    row[6] != null ? row[6].toString() : null, // skuProduto
                    row[7] != null ? Timestamp.valueOf(row[7].toString()) : null // dataGravacao
            );
            dados.add(dto);
        }

        // Log detalhado dos dados mapeados
        System.out.println("Dados mapeados (DTO) retornados: " + dados.size());
        if (dados.isEmpty()) {
            System.out.println("Nenhum dado encontrado para ano=" + ano + " e mês=" + mes);
            // Criar um DTO fictício para indicar que não há dados
            dados.add(new PedidoRelatorioDTO(
                    "Nenhum dado encontrado",
                    null,
                    null,
                    null,
                    BigDecimal.ZERO,
                    null,
                    null,
                    null
            ));
        } else {
            dados.forEach(dto -> System.out.println(
                    "DTO: representante=" + dto.getRepresentante() +
                            ", pedidoId=" + dto.getPedidoId() +
                            ", nf=" + dto.getNf() +
                            ", arquiteto=" + dto.getArquiteto() +
                            ", quantidade=" + dto.getQuantidade() +
                            ", nomeProduto=" + dto.getNomeProduto() +
                            ", skuProduto=" + dto.getSkuProduto() +
                            ", dataGravacao=" + dto.getDataGravacao()
            ));
        }

        // Configurar parâmetros do relatório
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("MES", mes);
        parametros.put("ANO", ano);
        parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

        // Carregar o template do relatório
        InputStream inputStream = this.getClass().getResourceAsStream("/rel/relPedidos.jasper");
        if (inputStream == null) {
            System.out.println("Erro: Arquivo relPedidos.jasper não encontrado no caminho /rel/");
            throw new Exception("Arquivo relPedidos.jasper não encontrado no caminho /rel/");
        }

        // Criar fonte de dados a partir da lista de DTOs
        System.out.println("Criando JRBeanCollectionDataSource com " + dados.size() + " registros");
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dados, false);

        // Preencher o relatório
        System.out.println("Preenchendo relatório Jasper...");
        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, dataSource);

        // Exportar para PDF
        System.out.println("Exportando relatório para PDF...");
        byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
        System.out.println("Relatório PDF gerado, tamanho: " + pdf.length + " bytes");

        return pdf;
    }
}