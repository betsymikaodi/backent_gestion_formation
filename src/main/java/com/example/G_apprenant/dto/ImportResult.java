package com.example.G_apprenant.dto;

import java.util.ArrayList;
import java.util.List;

public class ImportResult {
    private int total;
    private int inserted;
    private int skipped;
    private List<RowError> errors = new ArrayList<>();

    public ImportResult() {}

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getInserted() {
        return inserted;
    }

    public void setInserted(int inserted) {
        this.inserted = inserted;
    }

    public int getSkipped() {
        return skipped;
    }

    public void setSkipped(int skipped) {
        this.skipped = skipped;
    }

    public List<RowError> getErrors() {
        return errors;
    }

    public void setErrors(List<RowError> errors) {
        this.errors = errors;
    }

    public void addError(int rowNumber, String message) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(new RowError(rowNumber, message));
    }
}
