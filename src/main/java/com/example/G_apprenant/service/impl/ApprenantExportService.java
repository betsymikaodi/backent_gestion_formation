package com.example.G_apprenant.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.G_apprenant.entity.Apprenant;
import com.example.G_apprenant.repository.ApprenantRepository;

@Service
public class ApprenantExportService {
    private final ApprenantRepository repository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ApprenantExportService(ApprenantRepository repository) {
        this.repository = repository;
    }

    /**
     * Exporte tous les apprenants au format CSV
     */
    public byte[] exportAllToCsv() throws IOException {
        List<Apprenant> apprenants = repository.findAll();
        return exportToCsv(apprenants);
    }

    /**
     * Exporte une page d'apprenants au format CSV
     */
    public byte[] exportPageToCsv(Pageable pageable) throws IOException {
        Page<Apprenant> page = repository.findAll(pageable);
        return exportToCsv(page.getContent());
    }

    /**
     * Exporte tous les apprenants au format Excel
     */
    public byte[] exportAllToExcel() throws IOException {
        List<Apprenant> apprenants = repository.findAll();
        return exportToExcel(apprenants);
    }

    /**
     * Exporte une page d'apprenants au format Excel
     */
    public byte[] exportPageToExcel(Pageable pageable) throws IOException {
        Page<Apprenant> page = repository.findAll(pageable);
        return exportToExcel(page.getContent());
    }

    /**
     * Méthode privée pour exporter une liste d'apprenants en CSV
     */
    private byte[] exportToCsv(List<Apprenant> apprenants) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withHeader("nom", "prenom", "email", "telephone", "adresse", "dateNaissance", "cin"))) {
            
            for (Apprenant apprenant : apprenants) {
                csvPrinter.printRecord(
                    apprenant.getNom(),
                    apprenant.getPrenom(), 
                    apprenant.getEmail(),
                    apprenant.getTelephone(),
                    apprenant.getAdresse(),
                    apprenant.getDateNaissance() != null ? apprenant.getDateNaissance().format(DATE_FORMATTER) : "",
                    apprenant.getCin()
                );
            }
        }
        return out.toByteArray();
    }

    /**
     * Méthode privée pour exporter une liste d'apprenants en Excel
     */
    private byte[] exportToExcel(List<Apprenant> apprenants) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Apprenants");
            
            // Créer la ligne d'en-tête
            Row headerRow = sheet.createRow(0);
            String[] headers = {"nom", "prenom", "email", "telephone", "adresse", "dateNaissance", "cin"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }
            
            // Ajouter les données
            int rowIndex = 1;
            for (Apprenant apprenant : apprenants) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(apprenant.getNom());
                row.createCell(1).setCellValue(apprenant.getPrenom());
                row.createCell(2).setCellValue(apprenant.getEmail());
                row.createCell(3).setCellValue(apprenant.getTelephone() != null ? apprenant.getTelephone() : "");
                row.createCell(4).setCellValue(apprenant.getAdresse() != null ? apprenant.getAdresse() : "");
                row.createCell(5).setCellValue(apprenant.getDateNaissance() != null ? apprenant.getDateNaissance().format(DATE_FORMATTER) : "");
                row.createCell(6).setCellValue(apprenant.getCin());
            }
            
            // Ajuster automatiquement la largeur des colonnes
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }
}
