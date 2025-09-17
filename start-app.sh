#!/bin/bash

# ========================================
# SCRIPT DE DÉMARRAGE DE L'APPLICATION
# Système de Gestion des Apprenants et Formations
# ========================================

echo "🚀 Démarrage de l'application Spring Boot..."
echo "📊 Base de données PostgreSQL : centre_formation"
echo "🌐 L'application sera accessible sur : http://localhost:8080/api"
echo "📚 Documentation Swagger : http://localhost:8080/api/swagger-ui.html"
echo ""
echo "⏰ Démarrage en cours..."
echo ""

# Vérifier si PostgreSQL est en cours d'exécution
if ! pgrep -x "postgres" > /dev/null; then
    echo "❌ PostgreSQL n'est pas en cours d'exécution. Démarrage..."
    sudo systemctl start postgresql
    echo "✅ PostgreSQL démarré"
fi

# Démarrer l'application Spring Boot
./mvnw spring-boot:run

echo ""
echo "👋 Application arrêtée"