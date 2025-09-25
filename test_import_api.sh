#!/bin/bash

# Script de test pour l'API d'import des apprenants
API_BASE="http://localhost:8080/api"

echo "=== Test de l'API d'import des apprenants ==="
echo

echo "1. Test import CSV valide"
echo "------------------------"
response=$(curl -s -X POST $API_BASE/apprenants/import \
  -H "Content-Type: multipart/form-data" \
  -F "file=@test_data/apprenants_valides.csv")
echo "Réponse: $response"
echo

echo "2. Test import CSV avec erreurs"
echo "-------------------------------"
response=$(curl -s -X POST $API_BASE/apprenants/import \
  -H "Content-Type: multipart/form-data" \
  -F "file=@test_data/apprenants_erreurs.csv")
echo "Réponse: $response"
echo

echo "3. Test import Excel valide (sans dates)"
echo "----------------------------------------"
response=$(curl -s -X POST $API_BASE/apprenants/import \
  -H "Content-Type: multipart/form-data" \
  -F "file=@test_data/apprenants_excel_no_date.xlsx")
echo "Réponse: $response"
echo

echo "4. Test import Excel avec dates (problème connu)"
echo "------------------------------------------------"
response=$(curl -s -X POST $API_BASE/apprenants/import \
  -H "Content-Type: multipart/form-data" \
  -F "file=@test_data/apprenants_excel.xlsx")
echo "Réponse: $response"
echo

echo "5. Vérification des données importées"
echo "-------------------------------------"
echo "Apprenants CSV (CIN001-CIN004):"
curl -s -X GET $API_BASE/apprenants | jq '.data[] | select(.cin | test("CIN00[1-4]")) | {nom, prenom, email, cin}'
echo
echo "Apprenants Excel (CIN201-CIN203):"
curl -s -X GET $API_BASE/apprenants | jq '.data[] | select(.cin | test("CIN20[1-3]")) | {nom, prenom, email, cin}'
echo

echo "=== Tests terminés ==="
