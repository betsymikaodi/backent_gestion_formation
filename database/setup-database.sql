-- ========================================
-- SCRIPT DE CRÉATION DE LA BASE DE DONNÉES
-- Système de Gestion des Apprenants et Formations
-- ========================================

-- 1️⃣ Création de la base de données
CREATE DATABASE centre_formation;

-- Note: La connexion à la base se fera lors de l'exécution séparée
-- \c centre_formation;

-- ========================================
-- Script à exécuter APRÈS connexion à centre_formation
-- ========================================

-- 3️⃣ Table des Apprenants
CREATE TABLE apprenant (
    id_apprenant SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telephone VARCHAR(20),
    adresse TEXT,
    date_naissance DATE,
    cin VARCHAR(20) UNIQUE NOT NULL
);

-- 4️⃣ Table des Formations
CREATE TABLE formation (
    id_formation SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    description TEXT,
    duree INT NOT NULL, -- en heures
    frais NUMERIC(10,2) NOT NULL
);

-- 5️⃣ Table des Inscriptions
CREATE TABLE inscription (
    id_inscription SERIAL PRIMARY KEY,
    id_apprenant INT NOT NULL,
    id_formation INT NOT NULL,
    date_inscription DATE DEFAULT CURRENT_DATE,
    statut VARCHAR(20) DEFAULT 'En attente', -- En attente / Confirmé / Annulé
    droit_inscription NUMERIC(10,2) DEFAULT 0, -- Droit d'inscription
    FOREIGN KEY (id_apprenant) REFERENCES apprenant(id_apprenant) ON DELETE CASCADE,
    FOREIGN KEY (id_formation) REFERENCES formation(id_formation) ON DELETE CASCADE
);

-- 6️⃣ Table des Paiements
CREATE TABLE paiement (
    id_paiement SERIAL PRIMARY KEY,
    id_inscription INT NOT NULL,
    date_paiement DATE DEFAULT CURRENT_DATE,
    montant NUMERIC(10,2) NOT NULL,
    mode_paiement VARCHAR(20) DEFAULT 'Espèce', -- Espèce / Virement / Mobile Money
    module VARCHAR(20) DEFAULT 'Module 1', -- Module 1 à Module 4
    FOREIGN KEY (id_inscription) REFERENCES inscription(id_inscription) ON DELETE CASCADE
);

-- 7️⃣ Index pour améliorer les recherches
CREATE INDEX idx_apprenant_email ON apprenant(email);
CREATE INDEX idx_apprenant_cin ON apprenant(cin);
CREATE INDEX idx_formation_nom ON formation(nom);
CREATE INDEX idx_inscription_apprenant ON inscription(id_apprenant);
CREATE INDEX idx_paiement_inscription ON paiement(id_inscription);

-- ========================================
-- INSERTION DE DONNÉES DE TEST
-- ========================================

-- Apprenants de test
INSERT INTO apprenant (nom, prenom, email, telephone, adresse, date_naissance, cin) VALUES
('RAKOTO', 'Jean', 'jean.rakoto@email.com', '+261 34 12 345 67', 'Lot ABC Antananarivo', '1995-05-15', '123456789012'),
('RABE', 'Marie', 'marie.rabe@email.com', '+261 33 11 222 33', 'Antsirabe', '1998-03-20', '234567890123'),
('ANDRY', 'Paul', 'paul.andry@email.com', '+261 32 44 555 66', 'Fianarantsoa', '1992-11-08', '345678901234'),
('HERY', 'Sophie', 'sophie.hery@email.com', '+261 34 77 888 99', 'Mahajanga', '1996-07-12', '456789012345'),
('RINA', 'David', 'david.rina@email.com', '+261 33 66 777 88', 'Toamasina', '1994-09-25', '567890123456');

-- Formations de test
INSERT INTO formation (nom, description, duree, frais) VALUES
('Développement Web Full Stack', 'Formation complète en développement web avec React et Spring Boot', 120, 500000.00),
('Python pour Data Science', 'Analyse de données et machine learning avec Python', 80, 350000.00),
('Marketing Digital', 'Stratégies de marketing en ligne et réseaux sociaux', 60, 250000.00),
('Comptabilité et Gestion', 'Bases de la comptabilité et gestion d''entreprise', 100, 300000.00),
('Anglais Commercial', 'Anglais professionnel pour le monde des affaires', 40, 150000.00);

-- Inscriptions de test
INSERT INTO inscription (id_apprenant, id_formation, date_inscription, statut, droit_inscription) VALUES
(1, 1, '2024-01-15', 'Confirmé', 25000.00),
(2, 1, '2024-01-18', 'Confirmé', 25000.00),
(3, 2, '2024-01-20', 'Confirmé', 20000.00),
(4, 3, '2024-01-25', 'En attente', 15000.00),
(5, 4, '2024-02-01', 'Confirmé', 20000.00),
(1, 5, '2024-02-05', 'Confirmé', 10000.00); -- Jean suit aussi l'anglais

-- Paiements de test
INSERT INTO paiement (id_inscription, date_paiement, montant, mode_paiement, module) VALUES
-- Paiements pour Jean (inscription 1 - Développement Web)
(1, '2024-01-16', 125000.00, 'Virement', 'Module 1'),
(1, '2024-02-15', 125000.00, 'Virement', 'Module 2'),
(1, '2024-03-15', 125000.00, 'Virement', 'Module 3'),

-- Paiements pour Marie (inscription 2 - Développement Web)
(2, '2024-01-20', 150000.00, 'Mobile Money', 'Module 1'),
(2, '2024-02-20', 150000.00, 'Mobile Money', 'Module 2'),

-- Paiements pour Paul (inscription 3 - Python Data Science)
(3, '2024-01-22', 100000.00, 'Espèce', 'Module 1'),
(3, '2024-02-22', 100000.00, 'Espèce', 'Module 2'),

-- Paiements pour David (inscription 5 - Comptabilité)
(5, '2024-02-03', 100000.00, 'Virement', 'Module 1'),
(5, '2024-03-03', 100000.00, 'Virement', 'Module 2'),

-- Paiements pour Jean (inscription 6 - Anglais)
(6, '2024-02-07', 50000.00, 'Mobile Money', 'Module 1'),
(6, '2024-03-07', 50000.00, 'Mobile Money', 'Module 2');

-- ========================================
-- VÉRIFICATIONS FINALES
-- ========================================

-- Afficher le nombre d'enregistrements dans chaque table
SELECT 'Apprenants' as Table_Name, COUNT(*) as Count FROM apprenant
UNION ALL
SELECT 'Formations', COUNT(*) FROM formation
UNION ALL
SELECT 'Inscriptions', COUNT(*) FROM inscription
UNION ALL
SELECT 'Paiements', COUNT(*) FROM paiement;

-- Afficher un résumé des données
SELECT 
    a.nom || ' ' || a.prenom as "Apprenant",
    f.nom as "Formation",
    i.statut as "Statut",
    COUNT(p.id_paiement) as "Nb_Paiements",
    COALESCE(SUM(p.montant), 0) as "Total_Payé"
FROM apprenant a
JOIN inscription i ON a.id_apprenant = i.id_apprenant
JOIN formation f ON i.id_formation = f.id_formation
LEFT JOIN paiement p ON i.id_inscription = p.id_inscription
GROUP BY a.id_apprenant, a.nom, a.prenom, f.nom, i.statut, i.id_inscription
ORDER BY a.nom;

-- Message de succès
SELECT '✅ Base de données centre_formation créée avec succès!' as "Status";
SELECT '📊 Tables créées: apprenant, formation, inscription, paiement' as "Info";
SELECT '🔍 Index créés pour optimiser les performances' as "Performance";
SELECT '📝 Données de test insérées pour les tests' as "Data";