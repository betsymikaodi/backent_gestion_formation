#!/bin/bash

# Script de test pour l'API des activités récentes
BASE_URL="http://localhost:8080/api"
ACTIVITIES_URL="$BASE_URL/activities"

echo "=== Test de l'API des activités récentes ==="
echo "URL de base: $ACTIVITIES_URL"
echo

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
                echo "   📊 Nombre d'éléments: $count"
            elif echo "$body" | jq -e '.[]?' > /dev/null 2>&1; then
                count=$(echo "$body" | jq '. | length')
                echo "   📊 Nombre d'éléments: $count"
            else
                echo "   📊 Réponse: $(echo "$body" | jq -c . | cut -c1-100)..."
            fi
        else
            echo "   ⚠️  Response: Non-JSON ou vide"
        fi
    else
        echo "   ❌ Status: $http_code"
        echo "   📝 Response: $(echo "$body" | head -c 200)"
    fi
    echo
}

# Vérifier que l'application est en cours d'exécution
echo "🔍 Vérification de l'état de l'application..."
if ! curl -s "$BASE_URL/actuator/health" > /dev/null 2>&1; then
    echo "❌ L'application n'est pas accessible à $BASE_URL"
    echo "   Assurez-vous que l'application Spring Boot est démarrée avec:"
    echo "   ./mvnw spring-boot:run"
    echo
    exit 1
else
    echo "✅ L'application est accessible"
    echo
fi

# Tests des endpoints principaux
echo "=== TESTS DES ENDPOINTS ==="

# 1. Toutes les activités
test_endpoint "$ACTIVITIES_URL?page=0&size=5" "Obtenir toutes les activités (paginées)"

# 2. Activités récentes
test_endpoint "$ACTIVITIES_URL/recent?hours=24" "Activités récentes (24h)"

# 3. Statistiques globales
test_endpoint "$ACTIVITIES_URL/stats/count?hours=24" "Statistiques générales"

# 4. Statistiques par type d'entité
test_endpoint "$ACTIVITIES_URL/stats/entity-types?hours=24" "Statistiques par type d'entité"

# 5. Statistiques par action
test_endpoint "$ACTIVITIES_URL/stats/actions?hours=24" "Statistiques par action"

# 6. Dashboard des activités
test_endpoint "$ACTIVITIES_URL/dashboard?hours=24" "Dashboard des activités"

# 7. Activités CRUD des apprenants
test_endpoint "$ACTIVITIES_URL/crud/apprenants?page=0&size=5" "Activités CRUD des apprenants"

# 8. Activités CRUD des formations
test_endpoint "$ACTIVITIES_URL/crud/formations?page=0&size=5" "Activités CRUD des formations"

# 9. Activités CRUD des paiements
test_endpoint "$ACTIVITIES_URL/crud/paiements?page=0&size=5" "Activités CRUD des paiements"

# 10. Activités CRUD des inscriptions
test_endpoint "$ACTIVITIES_URL/crud/inscriptions?page=0&size=5" "Activités CRUD des inscriptions"

echo "=== RÉSUMÉ ==="
echo "✅ Tests des endpoints terminés"
echo "📖 Pour plus de détails, consultez la documentation Swagger à:"
echo "   http://localhost:8080/api/swagger-ui.html"
echo
echo "🔍 Pour générer des activités de test, utilisez les autres endpoints CRUD:"
echo "   - POST $BASE_URL/apprenants (créer un apprenant)"
echo "   - GET  $BASE_URL/apprenants (lister les apprenants)"
echo "   - POST $BASE_URL/formations (créer une formation)"
echo "   - etc."

