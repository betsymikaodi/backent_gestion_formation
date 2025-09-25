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
