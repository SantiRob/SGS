package com.sgs.util.report;

import com.sgs.model.User;
import com.sgs.repository.UserRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserReportGenerator {

    private final UserRepository userRepository = new UserRepository();
    private JasperReport compiledReport;

    public void export(String outputDirectory) {
        try {
            List<User> users = userRepository.findAll();

            if (compiledReport == null) {
                InputStream jasperStream = getClass()
                        .getClassLoader()
                        .getResourceAsStream("reports/users-report.jasper");

                if (jasperStream == null) {
                    throw new RuntimeException("No se encontró el archivo users-report.jasper en /resources/reports");
                }
                System.out.println(getClass().getClassLoader().getResource("reports/users-report.jasper"));

                compiledReport = (JasperReport) JRLoader.loadObject(jasperStream);
            }

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(users);
            Map<String, Object> parameters = new HashMap<>();

            JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, parameters, dataSource);
            String outputPath = outputDirectory + "/users-report.pdf";

            try (FileOutputStream outputStream = new FileOutputStream(outputPath)) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            }

            System.out.println("Reporte generado exitosamente en: " + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
