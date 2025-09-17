-- Script d'insertion de données de test pour le système de gestion des apprenants
-- À exécuter après la création automatique des tables par Hibernate

-- ========================================
-- DONNÉES DE TEST - APPRENANTS
-- ========================================
INSERT INTO apprenant (nom, prenom, email, telephone, adresse, date_naissance, cin) VALUES
('RAKOTO', 'Jean', 'jean.rakoto@email.com', '+261 34 12 345 67', 'Lot ABC Antananarivo', '1995-05-15', '123456789012'),
('RABE', 'Marie', 'marie.rabe@email.com', '+261 33 11 222 33', 'Antsirabe', '1998-03-20', '234567890123'),
('ANDRY', 'Paul', 'paul.andry@email.com', '+261 32 44 555 66', 'Fianarantsoa', '1992-11-08', '345678901234'),
('HERY', 'Sophie', 'sophie.hery@email.com', '+261 34 77 888 99', 'Mahajanga', '1996-07-12', '456789012345'),
('RINA', 'David', 'david.rina@email.com', '+261 33 66 777 88', 'Toamasina', '1994-09-25', '567890123456'),
('SANDA', 'Lucia', 'lucia.sanda@email.com', '+261 32 55 444 33', 'Antananarivo', '1997-12-03', '678901234567'),
('TAHINA', 'Michel', 'michel.tahina@email.com', '+261 34 88 999 00', 'Antsirabe', '1993-04-18', '789012345678'),
('VOLA', 'Emma', 'emma.vola@email.com', '+261 33 22 111 44', 'Fianarantsoa', '1999-01-30', '890123456789');

-- ========================================
-- DONNÉES DE TEST - FORMATIONS
-- ========================================
INSERT INTO formation (nom, description, duree, frais) VALUES
('Développement Web Full Stack', 'Formation complète en développement web avec React et Spring Boot', 120, 500000.00),
('Python pour Data Science', 'Analyse de données et machine learning avec Python', 80, 350000.00),
('Marketing Digital', 'Stratégies de marketing en ligne et réseaux sociaux', 60, 250000.00),
('Comptabilité et Gestion', 'Bases de la comptabilité et gestion d''entreprise', 100, 300000.00),
('Anglais Commercial', 'Anglais professionnel pour le monde des affaires', 40, 150000.00),
('Design Graphique', 'Création graphique avec Photoshop et Illustrator', 90, 400000.00),
('Gestion de Projet', 'Méthodologies Agile et gestion d''équipe', 50, 200000.00),
('Développement Mobile', 'Applications Android et iOS avec Flutter', 110, 450000.00);

-- ========================================
-- DONNÉES DE TEST - INSCRIPTIONS
-- ========================================
INSERT INTO inscription (id_apprenant, id_formation, date_inscription, statut, droit_inscription) VALUES
(1, 1, '2024-01-15', 'Confirmé', 25000.00),
(2, 1, '2024-01-18', 'Confirmé', 25000.00),
(3, 2, '2024-01-20', 'Confirmé', 20000.00),
(4, 3, '2024-01-25', 'En attente', 15000.00),
(5, 4, '2024-02-01', 'Confirmé', 20000.00),
(6, 5, '2024-02-05', 'Confirmé', 10000.00),
(7, 6, '2024-02-10', 'Confirmé', 25000.00),
(8, 7, '2024-02-15', 'En attente', 15000.00),
(1, 5, '2024-02-20', 'Confirmé', 10000.00), -- Jean suit aussi l'anglais
(2, 3, '2024-02-25', 'Confirmé', 15000.00), -- Marie suit aussi le marketing
(3, 8, '2024-03-01', 'Confirmé', 25000.00), -- Paul suit le développement mobile
(4, 6, '2024-03-05', 'En attente', 25000.00); -- Sophie intéressée par le design

-- ========================================
-- DONNÉES DE TEST - PAIEMENTS
-- ========================================
INSERT INTO paiement (id_inscription, date_paiement, montant, mode_paiement, module) VALUES
-- Paiements pour Jean (inscription 1 - Développement Web)
(1, '2024-01-16', 125000.00, 'Virement', 'Module 1'),
(1, '2024-02-15', 125000.00, 'Virement', 'Module 2'),
(1, '2024-03-15', 125000.00, 'Virement', 'Module 3'),
(1, '2024-04-15', 125000.00, 'Virement', 'Module 4'),

-- Paiements pour Marie (inscription 2 - Développement Web)
(2, '2024-01-20', 150000.00, 'Mobile Money', 'Module 1'),
(2, '2024-02-20', 150000.00, 'Mobile Money', 'Module 2'),
(2, '2024-03-20', 150000.00, 'Mobile Money', 'Module 3'),

-- Paiements pour Paul (inscription 3 - Python Data Science)
(3, '2024-01-22', 100000.00, 'Espèce', 'Module 1'),
(3, '2024-02-22', 100000.00, 'Espèce', 'Module 2'),
(3, '2024-03-22', 100000.00, 'Espèce', 'Module 3'),

-- Paiements pour Hery (inscription 5 - Comptabilité)
(5, '2024-02-03', 80000.00, 'Virement', 'Module 1'),
(5, '2024-03-03', 80000.00, 'Virement', 'Module 2'),
(5, '2024-04-03', 80000.00, 'Virement', 'Module 3'),

-- Paiements pour Rina (inscription 6 - Anglais)
(6, '2024-02-07', 40000.00, 'Mobile Money', 'Module 1'),
(6, '2024-03-07', 40000.00, 'Mobile Money', 'Module 2'),
(6, '2024-04-07', 40000.00, 'Mobile Money', 'Module 3'),

-- Paiements pour Sanda (inscription 7 - Design Graphique)
(7, '2024-02-12', 120000.00, 'Espèce', 'Module 1'),
(7, '2024-03-12', 120000.00, 'Espèce', 'Module 2'),

-- Paiements partiels pour Jean (inscription 9 - Anglais)
(9, '2024-02-22', 50000.00, 'Virement', 'Module 1'),
(9, '2024-03-22', 50000.00, 'Virement', 'Module 2'),

-- Paiements pour Marie (inscription 10 - Marketing)
(10, '2024-02-27', 80000.00, 'Mobile Money', 'Module 1'),
(10, '2024-03-27', 80000.00, 'Mobile Money', 'Module 2'),

-- Paiements pour Paul (inscription 11 - Développement Mobile)
(11, '2024-03-03', 150000.00, 'Virement', 'Module 1'),
(11, '2024-04-03', 150000.00, 'Virement', 'Module 2');

-- ========================================
-- VÉRIFICATION DES DONNÉES INSÉRÉES
-- ========================================
-- Commandes pour vérifier les données (à exécuter séparément)
-- SELECT COUNT(*) FROM apprenant; -- Devrait retourner 8
-- SELECT COUNT(*) FROM formation; -- Devrait retourner 8  
-- SELECT COUNT(*) FROM inscription; -- Devrait retourner 12
-- SELECT COUNT(*) FROM paiement; -- Devrait retourner 22

-- ========================================
-- REQUÊTES DE TEST UTILES
-- ========================================
/*
-- Voir tous les apprenants avec leurs inscriptions
SELECT 
    a.nom, a.prenom, a.email,
    f.nom as formation,
    i.statut, i.date_inscription
FROM apprenant a
JOIN inscription i ON a.id_apprenant = i.id_apprenant
JOIN formation f ON i.id_formation = f.id_formation
ORDER BY a.nom, i.date_inscription;

-- Voir les paiements par apprenant
SELECT 
    a.nom, a.prenom,
    f.nom as formation,
    p.montant, p.mode_paiement, p.module, p.date_paiement
FROM apprenant a
JOIN inscription i ON a.id_apprenant = i.id_apprenant
JOIN formation f ON i.id_formation = f.id_formation
JOIN paiement p ON i.id_inscription = p.id_inscription
ORDER BY a.nom, p.date_paiement;

-- Formations les plus populaires
SELECT 
    f.nom,
    COUNT(i.id_inscription) as nombre_inscrits
FROM formation f
LEFT JOIN inscription i ON f.id_formation = i.id_formation
GROUP BY f.id_formation, f.nom
ORDER BY nombre_inscrits DESC;

-- Total des paiements par mode
SELECT 
    p.mode_paiement,
    COUNT(*) as nombre_paiements,
    SUM(p.montant) as total_montant
FROM paiement p
GROUP BY p.mode_paiement
ORDER BY total_montant DESC;
*/