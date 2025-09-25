package com.example.G_apprenant.service;

import java.util.List;
import java.io.InputStream;
import com.example.G_apprenant.dto.ImportResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.G_apprenant.entity.Apprenant;
import com.example.G_apprenant.dto.ApprenantSearchCriteria;
import com.example.G_apprenant.dto.PaginatedResponse;

public interface ApprenantService {
    // Méthodes existantes
    Apprenant create(Apprenant a);
    Apprenant getById(Long id);
    List<Apprenant> getAll();
    Apprenant update(Long id, Apprenant a);
    void delete(Long id);
    
    // Nouvelles méthodes pour pagination et recherche avancée
    Page<Apprenant> searchApprenants(ApprenantSearchCriteria criteria);
    Page<Apprenant> findAllPaginated(Pageable pageable);
    PaginatedResponse<Apprenant> searchApprenantsWithMetadata(ApprenantSearchCriteria criteria);
    
    // Méthodes de recherche spécifiques
    List<Apprenant> findByNomContaining(String nom);
    List<Apprenant> findByEmailContaining(String email);
    Page<Apprenant> findByNomContaining(String nom, Pageable pageable);
    Page<Apprenant> findByEmailContaining(String email, Pageable pageable);
    
    // Statistiques
    long count();
    long countBySearchCriteria(ApprenantSearchCriteria criteria);

    // Import CSV / Excel
    ImportResult importApprenants(InputStream inputStream, String filename);
}