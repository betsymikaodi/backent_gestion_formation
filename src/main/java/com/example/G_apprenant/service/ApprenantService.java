package com.example.G_apprenant.service;

import java.util.List;
import com.example.G_apprenant.entity.Apprenant;

public interface ApprenantService {
    Apprenant create(Apprenant a);
    Apprenant getById(Long id);
    List<Apprenant> getAll();
    Apprenant update(Long id, Apprenant a);
    void delete(Long id);
}
