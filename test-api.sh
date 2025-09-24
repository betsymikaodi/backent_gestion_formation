#!/bin/bash

# Script de test rapide pour l'API Spring Boot
# Usage: ./test-api.sh

echo "🧪 TEST DE L'API G_APPRENANT"
echo "============================="

# URL de base
BASE_URL="http://localhost:8080/api"

# Couleurs pour les logs
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Test d'un endpoint
test_endpoint() {
    local method=$1
    local endpoint=$2
    local description=$3
    
    echo -e "\n${BLUE}Testing: $description${NC}"
    echo "Method: $method | URL: $BASE_URL$endpoint"
    
    if [ "$method" = "GET" ]; then
        response=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL$endpoint")
    else
        response=$(curl -s -o /dev/null -w "%{http_code}" -X "$method" "$BASE_URL$endpoint" -H "Content-Type: application/json")
    fi
    
    if [ "$response" = "200" ]; then
        echo -e "${GREEN}✅ SUCCESS - HTTP $response${NC}"
    else
        echo -e "${RED}❌ FAILED - HTTP $response${NC}"
    fi
}

# Vérifier si l'application est lancée
echo "🔍 Vérification de l'état de l'application..."
if ! curl -s "$BASE_URL" &>/dev/null; then
    echo -e "${RED}❌ L'application n'est pas accessible à $BASE_URL${NC}"
    echo "Veuillez démarrer l'application avec : ./start-app.sh"
    exit 1
fi
echo -e "${GREEN}✅ Application accessible${NC}"

echo -e "\n🚀 TESTS DES ENDPOINTS..."

# Tests des endpoints principaux
test_endpoint "GET" "/apprenants" "Liste des apprenants"
test_endpoint "GET" "/formations" "Liste des formations"
test_endpoint "GET" "/inscriptions" "Liste des inscriptions"

# Tests des endpoints spécifiques
test_endpoint "GET" "/apprenants/1" "Apprenant par ID"
test_endpoint "GET" "/formations/1" "Formation par ID"
test_endpoint "GET" "/formations/search/nom?nom=Web" "Recherche formation par nom"
test_endpoint "GET" "/formations/populaires" "Formations populaires"
test_endpoint "GET" "/formations/moins-cheres" "Formations moins chères"

# Test endpoint paiement
test_endpoint "GET" "/paiements/inscription/1" "Paiements par inscription"

# Test Swagger
echo -e "\n${BLUE}Testing: Documentation Swagger${NC}"
swagger_response=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/swagger-ui/index.html")
if [ "$swagger_response" = "200" ]; then
    echo -e "${GREEN}✅ SUCCESS - Swagger UI accessible${NC}"
    echo "   📚 URL: $BASE_URL/swagger-ui/index.html"
else
    echo -e "${RED}❌ FAILED - Swagger UI non accessible (HTTP $swagger_response)${NC}"
fi

# Test API docs
api_docs_response=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/v3/api-docs")
if [ "$api_docs_response" = "200" ]; then
    echo -e "${GREEN}✅ SUCCESS - API docs accessible${NC}"
    echo "   📋 URL: $BASE_URL/v3/api-docs"
else
    echo -e "${RED}❌ FAILED - API docs non accessible (HTTP $api_docs_response)${NC}"
fi

# Résumé
echo -e "\n📊 RÉSUMÉ"
echo "==========="
echo -e "🌐 API Base URL: $BASE_URL"
echo -e "📚 Swagger UI: $BASE_URL/swagger-ui/index.html"
echo -e "📋 API Docs: $BASE_URL/v3/api-docs"
echo -e "⚡ Thunder Client: Importez thunder-collection.json"

echo -e "\n${GREEN}✨ Tests terminés !${NC}"
echo -e "💡 Pour des tests détaillés, utilisez Thunder Client avec la collection fournie."