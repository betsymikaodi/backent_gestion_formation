package com.example.G_apprenant.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaginatedResponse<T> {
    @JsonProperty("data")
    private List<T> data;
    
    @JsonProperty("pagination")
    private PaginationMetadata pagination;
    
    public PaginatedResponse() {}
    
    public PaginatedResponse(List<T> data, PaginationMetadata pagination) {
        this.data = data;
        this.pagination = pagination;
    }
    
    // Getters and Setters
    public List<T> getData() {
        return data;
    }
    
    public void setData(List<T> data) {
        this.data = data;
    }
    
    public PaginationMetadata getPagination() {
        return pagination;
    }
    
    public void setPagination(PaginationMetadata pagination) {
        this.pagination = pagination;
    }
    
    public static class PaginationMetadata {
        @JsonProperty("current_page")
        private int currentPage;
        
        @JsonProperty("page_size")
        private int pageSize;
        
        @JsonProperty("total_elements")
        private long totalElements;
        
        @JsonProperty("total_pages")
        private int totalPages;
        
        @JsonProperty("has_next")
        private boolean hasNext;
        
        @JsonProperty("has_previous")
        private boolean hasPrevious;
        
        @JsonProperty("first_page")
        private boolean firstPage;
        
        @JsonProperty("last_page")
        private boolean lastPage;
        
        public PaginationMetadata() {}
        
        public PaginationMetadata(int currentPage, int pageSize, long totalElements, int totalPages) {
            this.currentPage = currentPage;
            this.pageSize = pageSize;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
            this.hasNext = currentPage < totalPages - 1;
            this.hasPrevious = currentPage > 0;
            this.firstPage = currentPage == 0;
            this.lastPage = currentPage == totalPages - 1;
        }
        
        // Getters and Setters
        public int getCurrentPage() {
            return currentPage;
        }
        
        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }
        
        public int getPageSize() {
            return pageSize;
        }
        
        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
        
        public long getTotalElements() {
            return totalElements;
        }
        
        public void setTotalElements(long totalElements) {
            this.totalElements = totalElements;
        }
        
        public int getTotalPages() {
            return totalPages;
        }
        
        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }
        
        public boolean isHasNext() {
            return hasNext;
        }
        
        public void setHasNext(boolean hasNext) {
            this.hasNext = hasNext;
        }
        
        public boolean isHasPrevious() {
            return hasPrevious;
        }
        
        public void setHasPrevious(boolean hasPrevious) {
            this.hasPrevious = hasPrevious;
        }
        
        public boolean isFirstPage() {
            return firstPage;
        }
        
        public void setFirstPage(boolean firstPage) {
            this.firstPage = firstPage;
        }
        
        public boolean isLastPage() {
            return lastPage;
        }
        
        public void setLastPage(boolean lastPage) {
            this.lastPage = lastPage;
        }
    }
}
