package com.sgs.util.report;

import com.sgs.model.report.VisitReportBean;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisitReportGenerator {

    private JasperReport compiledReport;

    public void export(List<VisitReportBean> visits, String outputPath, String filterDescription) {
        try {
            if (compiledReport == null) {
                loadCompiledReport();
            }

            URL imageUrl = getClass().getResource("/images/imgMain.png");

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(visits);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("LOGO_PATH", imageUrl.toString());
            parameters.put("FILTER_DESCRIPTION", filterDescription);

            JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, parameters, dataSource);

            try (FileOutputStream outputStream = new FileOutputStream(outputPath)) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            }

            System.out.println("Reporte de visitas generado exitosamente en: " + outputPath);

        } catch (Exception e) {
            System.err.println("Error al generar el reporte de visitas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadCompiledReport() throws JRException {
        try {
            URL resourceUrl = getClass().getResource("/reports/visits-report.jasper");
            if (resourceUrl != null) {
                System.out.println("URL del reporte encontrada: " + resourceUrl);
                try (InputStream jasperStream = resourceUrl.openStream()) {
                    compiledReport = (JasperReport) JRLoader.loadObject(jasperStream);
                    System.out.println("Reporte compilado de visitas cargado exitosamente.");
                }
            } else {
                throw new RuntimeException("No se encontró el archivo visits-report.jasper");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el reporte de visitas: " + e.getMessage());
            throw new JRException("No se pudo cargar visits-report.jasper", e);
        }
    }
}
