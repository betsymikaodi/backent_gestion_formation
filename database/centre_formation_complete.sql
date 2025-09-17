-- ========================================
-- EXPORT COMPLET DE LA BASE DE DONNÉES
-- Projet: Système de Gestion des Apprenants et Formations
-- Base de données: centre_formation
-- Date d'export: 2025-09-17
-- ========================================

-- Nettoyage préalable (si nécessaire)
DROP DATABASE IF EXISTS centre_formation;
CREATE DATABASE centre_formation;

-- Connexion à la base de données
\c centre_formation;

-- ========================================
-- CRÉATION DES TABLES
-- ========================================

-- Table des Apprenants
CREATE TABLE apprenant (
    id_apprenant SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telephone VARCHAR(20),
    adresse TEXT,
    date_naissance DATE,
    cin VARCHAR(20) UNIQUE NOT NULL,
    CONSTRAINT email_format CHECK (email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$')
);

-- Table des Formations
CREATE TABLE formation (
    id_formation SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    description TEXT,
    duree INT NOT NULL CHECK (duree > 0), -- en heures
    frais NUMERIC(10,2) NOT NULL CHECK (frais >= 0)
);

-- Table des Inscriptions
CREATE TABLE inscription (
    id_inscription SERIAL PRIMARY KEY,
    id_apprenant INT NOT NULL,
    id_formation INT NOT NULL,
    date_inscription DATE DEFAULT CURRENT_DATE,
    statut VARCHAR(20) DEFAULT 'En attente' CHECK (statut IN ('En attente', 'Confirmé', 'Annulé')),
    droit_inscription NUMERIC(10,2) DEFAULT 0 CHECK (droit_inscription >= 0),
    FOREIGN KEY (id_apprenant) REFERENCES apprenant(id_apprenant) ON DELETE CASCADE,
    FOREIGN KEY (id_formation) REFERENCES formation(id_formation) ON DELETE CASCADE,
    UNIQUE(id_apprenant, id_formation) -- Un apprenant ne peut s'inscrire qu'une fois à une formation
);

-- Table des Paiements
CREATE TABLE paiement (
    id_paiement SERIAL PRIMARY KEY,
    id_inscription INT NOT NULL,
    date_paiement DATE DEFAULT CURRENT_DATE,
    montant NUMERIC(10,2) NOT NULL CHECK (montant > 0),
    mode_paiement VARCHAR(20) DEFAULT 'Espèce' CHECK (mode_paiement IN ('Espèce', 'Virement', 'Mobile Money')),
    module VARCHAR(20) DEFAULT 'Module 1' CHECK (module IN ('Module 1', 'Module 2', 'Module 3', 'Module 4')),
    FOREIGN KEY (id_inscription) REFERENCES inscription(id_inscription) ON DELETE CASCADE
);

-- ========================================
-- CRÉATION DES INDEX POUR LES PERFORMANCES
-- ========================================

-- Index pour les recherches fréquentes
CREATE INDEX idx_apprenant_email ON apprenant(email);
CREATE INDEX idx_apprenant_cin ON apprenant(cin);
CREATE INDEX idx_apprenant_nom ON apprenant(nom);
CREATE INDEX idx_apprenant_prenom ON apprenant(prenom);

CREATE INDEX idx_formation_nom ON formation(nom);
CREATE INDEX idx_formation_frais ON formation(frais);

CREATE INDEX idx_inscription_apprenant ON inscription(id_apprenant);
CREATE INDEX idx_inscription_formation ON inscription(id_formation);
CREATE INDEX idx_inscription_statut ON inscription(statut);
CREATE INDEX idx_inscription_date ON inscription(date_inscription);

CREATE INDEX idx_paiement_inscription ON paiement(id_inscription);
CREATE INDEX idx_paiement_date ON paiement(date_paiement);
CREATE INDEX idx_paiement_mode ON paiement(mode_paiement);

-- ========================================
-- INSERTION DES DONNÉES DE BASE
-- ========================================

-- Apprenants existants
INSERT INTO apprenant (nom, prenom, email, telephone, adresse, date_naissance, cin) VALUES
('RAKOTO', 'Jean', 'jean.rakoto@email.com', '+261 34 12 345 67', 'Lot ABC Antananarivo', '1995-05-15', '123456789012'),
('RABE', 'Marie', 'marie.rabe@email.com', '+261 33 11 222 33', 'Antsirabe', '1998-03-20', '234567890123'),
('ANDRY', 'Paul', 'paul.andry@email.com', '+261 32 44 555 66', 'Fianarantsoa', '1992-11-08', '345678901234'),
('HERY', 'Sophie', 'sophie.hery@email.com', '+261 34 77 888 99', 'Mahajanga', '1996-07-12', '456789012345'),
('RINA', 'David', 'david.rina@email.com', '+261 33 66 777 88', 'Toamasina', '1994-09-25', '567890123456'),
('NOFY', 'Elson', 'elsonnofy@gmail.com', '+261 34 00 11 22', 'Ambodirano', '2003-01-01', '201032120421'),
('RAZAFY', 'Mialy', 'mialy.razafy@test.com', '+261 34 99 88 77', 'Antananarivo Madagascar', '2001-06-15', '301061523456');

-- Formations disponibles
INSERT INTO formation (nom, description, duree, frais) VALUES
('Développement Web Full Stack', 'Formation complète en développement web avec React et Spring Boot', 120, 500000.00),
('Python pour Data Science', 'Analyse de données et machine learning avec Python', 80, 350000.00),
('Marketing Digital', 'Stratégies de marketing en ligne et réseaux sociaux', 60, 250000.00),
('Comptabilité et Gestion', 'Bases de la comptabilité et gestion d''entreprise', 100, 300000.00),
('Anglais Commercial', 'Anglais professionnel pour le monde des affaires', 40, 150000.00),
('Design Graphique', 'Création visuelle avec Adobe Creative Suite', 90, 400000.00),
('Gestion de Projet', 'Méthodologies Agile et gestion d''équipe', 50, 200000.00);

-- Inscriptions des apprenants aux formations
INSERT INTO inscription (id_apprenant, id_formation, date_inscription, statut, droit_inscription) VALUES
(1, 1, '2024-01-15', 'Confirmé', 25000.00),  -- Jean -> Développement Web
(2, 1, '2024-01-18', 'Confirmé', 25000.00),  -- Marie -> Développement Web
(3, 2, '2024-01-20', 'Confirmé', 20000.00),  -- Paul -> Python Data Science
(4, 3, '2024-01-25', 'En attente', 15000.00), -- Sophie -> Marketing Digital
(5, 4, '2024-02-01', 'Confirmé', 20000.00),  -- David -> Comptabilité
(1, 5, '2024-02-05', 'Confirmé', 10000.00),  -- Jean -> Anglais (2ème formation)
(6, 1, '2024-02-10', 'Confirmé', 25000.00),  -- Elson -> Développement Web
(7, 2, '2024-02-15', 'En attente', 20000.00); -- Mialy -> Python Data Science

-- Paiements effectués
INSERT INTO paiement (id_inscription, date_paiement, montant, mode_paiement, module) VALUES
-- Paiements pour Jean (Développement Web - inscription 1)
(1, '2024-01-16', 125000.00, 'Virement', 'Module 1'),
(1, '2024-02-15', 125000.00, 'Virement', 'Module 2'),
(1, '2024-03-15', 125000.00, 'Virement', 'Module 3'),

-- Paiements pour Marie (Développement Web - inscription 2)
(2, '2024-01-20', 150000.00, 'Mobile Money', 'Module 1'),
(2, '2024-02-20', 150000.00, 'Mobile Money', 'Module 2'),

-- Paiements pour Paul (Python Data Science - inscription 3)
(3, '2024-01-22', 100000.00, 'Espèce', 'Module 1'),
(3, '2024-02-22', 100000.00, 'Espèce', 'Module 2'),

-- Paiements pour David (Comptabilité - inscription 5)
(5, '2024-02-03', 100000.00, 'Virement', 'Module 1'),
(5, '2024-03-03', 100000.00, 'Virement', 'Module 2'),

-- Paiements pour Jean (Anglais - inscription 6)
(6, '2024-02-07', 50000.00, 'Mobile Money', 'Module 1'),
(6, '2024-03-07', 50000.00, 'Mobile Money', 'Module 2'),

-- Paiements pour Elson (Développement Web - inscription 7)
(7, '2024-02-12', 200000.00, 'Virement', 'Module 1');

-- ========================================
-- VUES UTILES POUR LES RAPPORTS
-- ========================================

-- Vue: Résumé des inscriptions avec informations complètes
CREATE VIEW vue_inscriptions_complete AS
SELECT 
    i.id_inscription,
    a.nom || ' ' || a.prenom as nom_complet,
    a.email,
    a.telephone,
    f.nom as formation_nom,
    f.duree as formation_duree,
    f.frais as formation_frais,
    i.date_inscription,
    i.statut,
    i.droit_inscription,
    (f.frais + i.droit_inscription) as montant_total,
    COALESCE(SUM(p.montant), 0) as montant_paye,
    (f.frais + i.droit_inscription - COALESCE(SUM(p.montant), 0)) as montant_restant,
    COUNT(p.id_paiement) as nombre_paiements
FROM inscription i
JOIN apprenant a ON i.id_apprenant = a.id_apprenant
JOIN formation f ON i.id_formation = f.id_formation
LEFT JOIN paiement p ON i.id_inscription = p.id_inscription
GROUP BY i.id_inscription, a.id_apprenant, f.id_formation
ORDER BY i.date_inscription DESC;

-- Vue: Statistiques par formation
CREATE VIEW vue_stats_formations AS
SELECT 
    f.nom as formation,
    f.frais,
    f.duree,
    COUNT(i.id_inscription) as nombre_inscrits,
    COUNT(CASE WHEN i.statut = 'Confirmé' THEN 1 END) as inscrits_confirmes,
    COUNT(CASE WHEN i.statut = 'En attente' THEN 1 END) as inscrits_attente,
    COALESCE(SUM(p.montant), 0) as revenus_total
FROM formation f
LEFT JOIN inscription i ON f.id_formation = i.id_formation
LEFT JOIN paiement p ON i.id_inscription = p.id_inscription
GROUP BY f.id_formation, f.nom, f.frais, f.duree
ORDER BY nombre_inscrits DESC;

-- ========================================
-- FONCTIONS UTILES
-- ========================================

-- Fonction pour calculer l'âge d'un apprenant
CREATE OR REPLACE FUNCTION calculer_age(date_naissance DATE)
RETURNS INTEGER AS $$
BEGIN
    RETURN EXTRACT(YEAR FROM AGE(CURRENT_DATE, date_naissance));
END;
$$ LANGUAGE plpgsql;

-- Fonction pour obtenir le statut de paiement d'une inscription
CREATE OR REPLACE FUNCTION statut_paiement(p_id_inscription INTEGER)
RETURNS TEXT AS $$
DECLARE
    montant_total NUMERIC(10,2);
    montant_paye NUMERIC(10,2);
    pourcentage NUMERIC(5,2);
BEGIN
    -- Calculer le montant total (formation + droit d'inscription)
    SELECT (f.frais + i.droit_inscription)
    INTO montant_total
    FROM inscription i
    JOIN formation f ON i.id_formation = f.id_formation
    WHERE i.id_inscription = p_id_inscription;
    
    -- Calculer le montant payé
    SELECT COALESCE(SUM(montant), 0)
    INTO montant_paye
    FROM paiement
    WHERE id_inscription = p_id_inscription;
    
    -- Calculer le pourcentage
    IF montant_total > 0 THEN
        pourcentage := (montant_paye / montant_total) * 100;
        
        IF pourcentage >= 100 THEN
            RETURN 'Entièrement payé';
        ELSIF pourcentage >= 50 THEN
            RETURN 'Partiellement payé (' || pourcentage::INTEGER || '%)';
        ELSIF pourcentage > 0 THEN
            RETURN 'Peu payé (' || pourcentage::INTEGER || '%)';
        ELSE
            RETURN 'Non payé';
        END IF;
    ELSE
        RETURN 'Montant invalide';
    END IF;
END;
$$ LANGUAGE plpgsql;

-- ========================================
-- REQUÊTES DE VÉRIFICATION
-- ========================================

-- Vérification des données insérées
SELECT 'Apprenants' as Table_Name, COUNT(*) as Count FROM apprenant
UNION ALL
SELECT 'Formations', COUNT(*) FROM formation
UNION ALL
SELECT 'Inscriptions', COUNT(*) FROM inscription
UNION ALL
SELECT 'Paiements', COUNT(*) FROM paiement;

-- Résumé des inscriptions avec statut de paiement
SELECT 
    nom_complet,
    formation_nom,
    statut,
    montant_total,
    montant_paye,
    montant_restant,
    statut_paiement(id_inscription) as statut_paiement
FROM vue_inscriptions_complete
ORDER BY date_inscription;

-- ========================================
-- PERMISSIONS ET SÉCURITÉ
-- ========================================

-- Créer un utilisateur pour l'application (optionnel)
-- CREATE USER app_user WITH PASSWORD 'secure_password';
-- GRANT CONNECT ON DATABASE centre_formation TO app_user;
-- GRANT USAGE ON SCHEMA public TO app_user;
-- GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO app_user;
-- GRANT USAGE ON ALL SEQUENCES IN SCHEMA public TO app_user;

-- ========================================
-- COMMENTAIRES SUR LES TABLES
-- ========================================

COMMENT ON TABLE apprenant IS 'Table des apprenants inscrits au centre de formation';
COMMENT ON TABLE formation IS 'Catalogue des formations proposées';
COMMENT ON TABLE inscription IS 'Liens entre apprenants et formations';
COMMENT ON TABLE paiement IS 'Historique des paiements par module';

COMMENT ON COLUMN apprenant.cin IS 'Carte d''identité nationale - identifiant unique';
COMMENT ON COLUMN formation.duree IS 'Durée de la formation en heures';
COMMENT ON COLUMN formation.frais IS 'Frais de formation en Ariary malgache';
COMMENT ON COLUMN inscription.droit_inscription IS 'Frais d''inscription supplémentaires';
COMMENT ON COLUMN paiement.module IS 'Module pour lequel le paiement est effectué';

-- ========================================
-- TRIGGERS POUR L'AUDIT (OPTIONNEL)
-- ========================================

-- Table d'audit pour les modifications importantes
CREATE TABLE audit_log (
    id SERIAL PRIMARY KEY,
    table_name VARCHAR(50) NOT NULL,
    operation VARCHAR(10) NOT NULL,
    record_id INTEGER,
    old_values JSONB,
    new_values JSONB,
    user_name VARCHAR(50) DEFAULT CURRENT_USER,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Exemple de trigger pour auditer les modifications d'apprenants
CREATE OR REPLACE FUNCTION audit_apprenant_changes()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO audit_log (table_name, operation, record_id, old_values)
        VALUES ('apprenant', 'DELETE', OLD.id_apprenant, to_jsonb(OLD));
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO audit_log (table_name, operation, record_id, old_values, new_values)
        VALUES ('apprenant', 'UPDATE', NEW.id_apprenant, to_jsonb(OLD), to_jsonb(NEW));
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO audit_log (table_name, operation, record_id, new_values)
        VALUES ('apprenant', 'INSERT', NEW.id_apprenant, to_jsonb(NEW));
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Activer le trigger d'audit (décommenté si nécessaire)
-- CREATE TRIGGER trigger_audit_apprenant
--     AFTER INSERT OR UPDATE OR DELETE ON apprenant
--     FOR EACH ROW EXECUTE FUNCTION audit_apprenant_changes();

-- ========================================
-- MESSAGE DE SUCCÈS
-- ========================================

SELECT '🎉 Base de données centre_formation créée avec succès !' as "Status";
SELECT '📊 ' || COUNT(*) || ' apprenants insérés' as "Apprenants" FROM apprenant;
SELECT '📚 ' || COUNT(*) || ' formations disponibles' as "Formations" FROM formation;
SELECT '📝 ' || COUNT(*) || ' inscriptions enregistrées' as "Inscriptions" FROM inscription;
SELECT '💰 ' || COUNT(*) || ' paiements effectués' as "Paiements" FROM paiement;
SELECT '🔍 Vues et fonctions utilitaires créées' as "Outils";

-- ========================================
-- FIN DU SCRIPT
-- ========================================