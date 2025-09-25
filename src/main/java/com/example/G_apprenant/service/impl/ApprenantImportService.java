package com.example.G_apprenant.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.example.G_apprenant.dto.ImportResult;
import com.example.G_apprenant.entity.Apprenant;
import com.example.G_apprenant.repository.ApprenantRepository;

// CSV
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

// Excel
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class ApprenantImportService {
    private final ApprenantRepository repo;

    public ApprenantImportService(ApprenantRepository repo) {
        this.repo = repo;
    }

    public ImportResult importFile(InputStream inputStream, String filename) {
        ImportResult result = new ImportResult();
        List<Apprenant> toInsert = new ArrayList<>();
        try {
            if (filename != null && filename.toLowerCase().endsWith(".csv")) {
                parseCsv(inputStream, result, toInsert);
            } else if (filename != null && (filename.toLowerCase().endsWith(".xlsx") || filename.toLowerCase().endsWith(".xls"))) {
                parseXlsx(inputStream, result, toInsert);
            } else {
                result.addError(0, "Format de fichier non supporté. Utilisez .csv ou .xlsx");
                return result;
            }
            if (!toInsert.isEmpty()) {
                repo.saveAll(toInsert);
            }
        } catch (IOException e) {
            result.addError(0, "Erreur lecture du fichier: " + e.getMessage());
        }
        return result;
    }

    private void parseCsv(InputStream is, ImportResult result, List<Apprenant> toInsert) throws IOException {
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreEmptyLines()
                .withTrim()
                .parse(new java.io.InputStreamReader(is));

        int rowNum = 1; // header is row 0
        for (CSVRecord rec : records) {
            rowNum++;
            result.setTotal(result.getTotal() + 1);
            processRow(rec.toMap(), rowNum, result, toInsert);
        }
    }

    private void processRow(Map<String,String> row, int rowNumber, ImportResult result, List<Apprenant> toInsert) {
        String nom = get(row, "nom");
        String prenom = get(row, "prenom");
        String email = get(row, "email");
        String telephone = get(row, "telephone");
        String adresse = get(row, "adresse");
        String dateNaissanceStr = firstNonNull(get(row, "dateNaissance"), get(row, "date_naissance"), get(row, "date-naissance"));
        String cin = get(row, "cin");

        if (isBlank(nom) || isBlank(prenom) || isBlank(email) || isBlank(cin)) {
            result.addError(rowNumber, "Champs obligatoires manquants (nom, prenom, email, cin)");
            result.setSkipped(result.getSkipped() + 1);
            return;
        }
        if (repo.existsByEmail(email) || repo.existsByCin(cin)) {
            result.addError(rowNumber, "Enregistrement ignoré (email ou CIN déjà existant)");
            result.setSkipped(result.getSkipped() + 1);
            return;
        }
        Apprenant a = new Apprenant();
        a.setNom(nom);
        a.setPrenom(prenom);
        a.setEmail(email);
        a.setTelephone(emptyToNull(telephone));
        a.setAdresse(emptyToNull(adresse));
        if (!isBlank(dateNaissanceStr)) {
            try {
                a.setDateNaissance(LocalDate.parse(dateNaissanceStr));
            } catch (Exception e) {
                result.addError(rowNumber, "Format dateNaissance invalide (yyyy-MM-dd)");
                result.setSkipped(result.getSkipped() + 1);
                return;
            }
        }
        a.setCin(cin);
        toInsert.add(a);
        result.setInserted(result.getInserted() + 1);
    }

    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
    private static String emptyToNull(String s) { return isBlank(s) ? null : s.trim(); }
    private static String get(Map<String,String> map, String key) {
        if (map == null) return null;
        for (Map.Entry<String,String> e : map.entrySet()) {
            if (e.getKey() != null && e.getKey().trim().equalsIgnoreCase(key)) return e.getValue();
        }
        return null;
    }
    private void parseXlsx(InputStream is, ImportResult result, java.util.List<Apprenant> toInsert) throws java.io.IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getNumberOfSheets() > 0 ? workbook.getSheetAt(0) : null;
            if (sheet == null) {
                result.addError(0, "Aucune feuille dans le fichier XLSX");
                return;
            }
            Row header = sheet.getRow(0);
            if (header == null) {
                result.addError(0, "Ligne d'en-tête manquante (row 1)");
                return;
            }
            java.util.Map<Integer, String> idxToHeader = new java.util.HashMap<>();
            for (int i = 0; i < header.getLastCellNum(); i++) {
                Cell c = header.getCell(i);
                if (c != null) {
                    String h = c.getStringCellValue();
                    if (h != null) idxToHeader.put(i, h.trim());
                }
            }
            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;
                java.util.Map<String,String> map = new java.util.HashMap<>();
                for (java.util.Map.Entry<Integer,String> e : idxToHeader.entrySet()) {
                    Cell cell = row.getCell(e.getKey());
                    String val = null;
                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case STRING -> val = cell.getStringCellValue();
                            case NUMERIC -> val = String.valueOf((long)cell.getNumericCellValue());
                            case BOOLEAN -> val = String.valueOf(cell.getBooleanCellValue());
                            default -> val = null;
                        }
                    }
                    map.put(e.getValue(), val != null ? val.trim() : null);
                }
                int rowNumber = r + 1; // human-readable index
                result.setTotal(result.getTotal() + 1);
                processRow(map, rowNumber, result, toInsert);
            }
        }
    }

    private static String firstNonNull(String... values) {
        if (values == null) return null;
        for (String v : values) {
            if (v != null && !v.trim().isEmpty()) return v;
        }
        return null;
    }
}
