#!/bin/bash

# Script de test corrigé pour l'API des activités
BASE_URL="http://localhost:8080/api"
ACTIVITIES_URL="$BASE_URL/activities"

echo "=== Test de l'API des activités récentes (VERSION CORRIGÉE) ==="
echo "URL de base: $ACTIVITIES_URL"
echo

# Démarrer l'application en arrière-plan
echo "🚀 Démarrage de l'application..."
./mvnw spring-boot:run > server.log 2>&1 &
SERVER_PID=$!

# Attendre que l'application démarre
echo "⏳ Attente du démarrage (30 secondes)..."
sleep 30

# Fonction pour tester un endpoint
test_endpoint() {
    local endpoint="$1"
    local description="$2"
    
    echo "🔍 Test: $description"
    echo "   Endpoint: $endpoint"
    
    response=$(curl -s -w "\n%{http_code}" "$endpoint" 2>/dev/null)
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | head -n -1)
    
    if [ "$http_code" = "200" ]; then
        echo "   ✅ Status: $http_code (OK)"
        if echo "$body" | jq . > /dev/null 2>&1; then
            echo "   ✅ Response: JSON valide"
            # Afficher un résumé de la réponse
            if echo "$body" | jq -e '.content[]?' > /dev/null 2>&1; then
                count=$(echo "$body" | jq '.content | length')
                total=$(echo "$body" | jq '.totalElements // "N/A"')
                echo "   📊 Page: $count éléments, Total: $total"
            elif echo "$body" | jq -e '.[]?' > /dev/null 2>&1; then
                count=$(echo "$body" | jq '. | length')
                echo "   📊 Nombre d'éléments: $count"
            else
                echo "   📊 Réponse: $(echo "$body" | jq -c . | cut -c1-150)..."
            fi
        else
            echo "   ⚠️  Response: Non-JSON ou vide"
        fi
    else
        echo "   ❌ Status: $http_code"
        if [ "$http_code" = "403" ]; then
            echo "   🚫 Erreur 403: Accès interdit (problème de sécurité)"
        elif [ "$http_code" = "500" ]; then
            echo "   💥 Erreur 500: Erreur serveur interne"
        fi
        echo "   📝 Response: $(echo "$body" | head -c 200)"
    fi
    echo
}

# Vérifier que l'application est accessible
echo "🔍 Vérification de l'état de l'application..."
max_attempts=10
attempt=1

while [ $attempt -le $max_attempts ]; do
    if curl -s "$BASE_URL/actuator/health" > /dev/null 2>&1; then
        echo "✅ L'application est accessible (tentative $attempt)"
        break
    else
        echo "⏳ Tentative $attempt/$max_attempts - En attente..."
        sleep 3
        attempt=$((attempt + 1))
    fi
done

if [ $attempt -gt $max_attempts ]; then
    echo "❌ L'application n'est pas accessible après $max_attempts tentatives"
    echo "📋 Vérifiez les logs du serveur:"
    tail -20 server.log
    kill $SERVER_PID 2>/dev/null
    exit 1
fi

echo
echo "=== TESTS DES ENDPOINTS ACTIVITIES ==="

# Tests principaux des activités
test_endpoint "$ACTIVITIES_URL" "Obtenir toutes les activités (paginées)"
test_endpoint "$ACTIVITIES_URL/recent?hours=24" "Activités récentes (24h)"
test_endpoint "$ACTIVITIES_URL/stats/count?hours=24" "Statistiques générales"
test_endpoint "$ACTIVITIES_URL/stats/entity-types?hours=24" "Statistiques par type d'entité"
test_endpoint "$ACTIVITIES_URL/stats/actions?hours=24" "Statistiques par action"
test_endpoint "$ACTIVITIES_URL/dashboard?hours=24" "Dashboard des activités"

# Tests CRUD spécialisés
test_endpoint "$ACTIVITIES_URL/crud/apprenants?page=0&size=5" "Activités CRUD des apprenants"
test_endpoint "$ACTIVITIES_URL/crud/formations?page=0&size=5" "Activités CRUD des formations"
test_endpoint "$ACTIVITIES_URL/crud/paiements?page=0&size=5" "Activités CRUD des paiements"
test_endpoint "$ACTIVITIES_URL/crud/inscriptions?page=0&size=5" "Activités CRUD des inscriptions"

echo "=== GÉNÉRATION D'ACTIVITÉS DE TEST ==="

# Générer quelques activités en utilisant les APIs CRUD existantes
echo "🔄 Génération d'activités via les APIs CRUD..."

# Créer un apprenant (génère une activité CREATE)
echo "1. Création d'un apprenant de test:"
response=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/apprenants" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "TEST_ACTIVITY",
    "prenom": "User",
    "email": "test.activity.'$(date +%s)'@example.com",
    "cin": "TEST'$(date +%s)'",
    "telephone": "+261340000000",
    "adresse": "Test Address",
    "dateNaissance": "1995-01-01"
  }' 2>/dev/null)

http_code=$(echo "$response" | tail -n1)
if [ "$http_code" = "201" ] || [ "$http_code" = "200" ]; then
    echo "   ✅ Apprenant créé (Status: $http_code)"
else
    echo "   ❌ Erreur création apprenant (Status: $http_code)"
fi

# Lister les apprenants (génère une activité READ)
echo "2. Consultation des apprenants:"
response=$(curl -s -w "\n%{http_code}" "$BASE_URL/apprenants" 2>/dev/null)
http_code=$(echo "$response" | tail -n1)
if [ "$http_code" = "200" ]; then
    echo "   ✅ Apprenants consultés (Status: $http_code)"
else
    echo "   ❌ Erreur consultation apprenants (Status: $http_code)"
fi

echo
echo "⏳ Attente pour que les activités soient enregistrées (3 secondes)..."
sleep 3

# Re-tester les activités pour voir les nouvelles données
echo "=== VÉRIFICATION DES NOUVELLES ACTIVITÉS ==="
test_endpoint "$ACTIVITIES_URL/recent?hours=1" "Activités de la dernière heure"
test_endpoint "$ACTIVITIES_URL/stats/count?hours=1" "Statistiques de la dernière heure"

# Arrêter le serveur
echo "=== NETTOYAGE ==="
echo "🛑 Arrêt du serveur..."
kill $SERVER_PID 2>/dev/null
wait $SERVER_PID 2>/dev/null

echo "✅ Tests terminés !"
echo "📋 Pour plus de détails, consultez:"
echo "   - Logs du serveur: server.log"
echo "   - Documentation: http://localhost:8080/api/swagger-ui.html"
echo
echo "🎉 Si tous les tests sont OK, l'API des activités récentes fonctionne correctement !"
