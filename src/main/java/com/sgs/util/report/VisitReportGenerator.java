package com.sgs.util.report;

import com.sgs.model.Visit;
import com.sgs.repository.VisitRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisitReportGenerator {

    private JasperReport compiledReport;

    public void exportByMaintenanceType(String outputDir, String typeName, VisitRepository repo) {
        List<Visit> visits = repo.findByMaintenanceType(typeName);
        generatePdf(outputDir, visits, "visits-by-type.pdf");
    }

    public void exportByResult(String outputDir, String result, VisitRepository repo) {
        List<Visit> visits = repo.findByResult(result);
        generatePdf(outputDir, visits, "visits-by-result.pdf");
    }

    public void exportByDate(String outputDir, LocalDate date, VisitRepository repo) {
        List<Visit> visits = repo.findByDate(date);
        generatePdf(outputDir, visits, "visits-by-date.pdf");
    }

    private void generatePdf(String outputDir, List<Visit> visits, String filename) {
        try {
            if (compiledReport == null) {
                loadCompiledReport();
            }

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(visits);
            Map<String, Object> params = new HashMap<>();
            URL logoUrl = getClass().getResource("/images/imgMain.png");
            if (logoUrl != null) {
                params.put("LOGO_PATH", logoUrl.toString());
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, params, dataSource);
            String outputPath = outputDir + "/" + filename;
            try (FileOutputStream out = new FileOutputStream(outputPath)) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            }

            System.out.println("Reporte generado en: " + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCompiledReport() throws JRException {
        try {
            URL resourceUrl = getClass().getResource("/reports/visits-report.jasper");
            if (resourceUrl != null) {
                try (InputStream stream = resourceUrl.openStream()) {
                    compiledReport = (JasperReport) JRLoader.loadObject(stream);
                    System.out.println("Reporte de visitas cargado correctamente.");
                }
            } else {
                throw new RuntimeException("No se encontró el archivo visits-report.jasper");
            }
        } catch (Exception e) {
            throw new JRException("Error al cargar el reporte.", e);
        }
    }

    public void exportByDateRange(String outputDir, LocalDate start, LocalDate end, VisitRepository repo) {
        List<Visit> visits = repo.findByDateRange(start, end);
        generatePdf(outputDir, visits, "visits-by-date-range.pdf");
    }
}
