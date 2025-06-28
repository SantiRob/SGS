package com.sgs.util.report;

import com.sgs.model.Station;
import com.sgs.repository.StationRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StationReportGenerator {
    private JasperReport compiledReport;
    String outputPath;

    public void export(String outputDirectory, StationRepository stationRepository) {
        try {
            List<Station> stations = stationRepository.findAll();
            if (compiledReport == null) {
                try {
                    loadCompiledReport();
                } catch (JRException e) {
                    throw new RuntimeException(e);
                }
            }
            URL imageUrl = getClass().getResource("/images/imgMain.png");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(stations);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("LOGO_PATH", imageUrl.toString());

            JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, parameters, dataSource);
            outputPath = outputDirectory + "/stations-report.pdf";

            try (FileOutputStream outputStream = new FileOutputStream(outputPath)) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            }
        } catch (Exception e) {
            System.err.println("Error al generar el reporte: " + e.getMessage());
            e.printStackTrace();
        }

    }

    private void loadCompiledReport() throws JRException {
        try {
            URL resourceUrl = getClass().getResource("/reports/stations-report.jasper");
            if (resourceUrl != null) {
                System.out.println("URL encontrada: " + resourceUrl);
                try (InputStream jasperStream = resourceUrl.openStream()) {
                    compiledReport = (JasperReport) JRLoader.loadObject(jasperStream);
                    System.out.println("Reporte compilado cargado exitosamente");
                }
            } else {
                throw new RuntimeException("No se encontró el archivo stations-report.jasper");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el reporte: " + e.getMessage());
            throw new JRException("No se pudo cargar el archivo stations-report.jasper", e);
        }
    }
}
