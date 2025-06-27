package com.sgs.util.report;

import com.sgs.model.User;
import com.sgs.repository.UserRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserReportGenerator {
    private JasperReport compiledReport;
    String outputPath;

    public void export(String outputDirectory, UserRepository userRepository) {
        try {
            List<User> users = userRepository.findAll();

            if (compiledReport == null) {
                loadCompiledReport();
            }
            URL imageUrl = getClass().getResource("/images/imgMain.png");
            if (imageUrl == null) {
                throw new RuntimeException("No se encontró la imagen");
            }
            System.out.println("LOGO_PATH => " + imageUrl);


            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(users);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("LOGO_PATH", imageUrl.toString());

            JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, parameters, dataSource);
            outputPath = outputDirectory + "/users-report.pdf";

            try (FileOutputStream outputStream = new FileOutputStream(outputPath)) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            }

            System.out.println("Reporte generado exitosamente en: " + outputPath);
        } catch (Exception e) {
            System.err.println("Error al generar el reporte: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadCompiledReport() throws JRException {
        try {
            URL resourceUrl = getClass().getResource("/reports/users-report.jasper");
            if (resourceUrl != null) {
                System.out.println("URL encontrada: " + resourceUrl);
                try (InputStream jasperStream = resourceUrl.openStream()) {
                    compiledReport = (JasperReport) JRLoader.loadObject(jasperStream);
                    System.out.println("Reporte compilado cargado exitosamente");
                }
            } else {
                throw new RuntimeException("No se encontró el archivo users-report.jasper");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el reporte: " + e.getMessage());
            throw new JRException("No se pudo cargar el archivo users-report.jasper", e);
        }
    }
}