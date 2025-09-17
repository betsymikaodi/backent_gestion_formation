package com.example.G_apprenant.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.G_apprenant.entity.Apprenant;
import com.example.G_apprenant.exception.ResourceNotFoundException;
import com.example.G_apprenant.repository.ApprenantRepository;
import com.example.G_apprenant.service.ApprenantService;

@Service
@Transactional
public class ApprenantServiceImpl implements ApprenantService {
    private final ApprenantRepository repo;

    public ApprenantServiceImpl(ApprenantRepository repo) {
        this.repo = repo;
    }

    @Override
    public Apprenant create(Apprenant a) {
        return repo.save(a);
    }

    @Override
    public Apprenant getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Apprenant non trouvé id=" + id));
    }

    @Override
    public List<Apprenant> getAll() {
        return repo.findAll();
    }

    @Override
    public Apprenant update(Long id, Apprenant a) {
        Apprenant exist = getById(id);
        exist.setNom(a.getNom());
        exist.setPrenom(a.getPrenom());
        exist.setEmail(a.getEmail());
        exist.setTelephone(a.getTelephone());
        exist.setAdresse(a.getAdresse());
        exist.setDateNaissance(a.getDateNaissance());
        exist.setCin(a.getCin());
        return repo.save(exist);
    }

    @Override
    public void delete(Long id) {
        Apprenant exist = getById(id);
        repo.delete(exist);
    }
}
