#!/bin/bash

# Script de test pour l'API d'exportation des apprenants
API_BASE="http://localhost:8080/api"
EXPORT_DIR="exports"

echo "=== Test de l'API d'exportation des apprenants ==="
echo

# Créer le dossier d'export s'il n'existe pas
mkdir -p $EXPORT_DIR

echo "1. Export CSV complet (tous les apprenants)"
echo "-------------------------------------------"
curl -X GET "$API_BASE/apprenants/export/csv/all" \
  -H "Accept: text/csv" \
  -o "$EXPORT_DIR/test_export_complet.csv" \
  -w "Status: %{http_code}, Taille: %{size_download} bytes\n"
echo "Aperçu (5 premières lignes):"
head -n 5 "$EXPORT_DIR/test_export_complet.csv"
echo

echo "2. Export Excel complet (tous les apprenants)"
echo "---------------------------------------------"
curl -X GET "$API_BASE/apprenants/export/excel/all" \
  -H "Accept: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" \
  -o "$EXPORT_DIR/test_export_complet.xlsx" \
  -w "Status: %{http_code}, Taille: %{size_download} bytes\n"
echo "Type de fichier: $(file "$EXPORT_DIR/test_export_complet.xlsx")"
echo

echo "3. Export CSV paginé (page 1, 10 éléments, tri par nom)"
echo "-------------------------------------------------------"
curl -X GET "$API_BASE/apprenants/export/csv/page?page=0&size=10&sortBy=nom&sortDir=asc" \
  -H "Accept: text/csv" \
  -o "$EXPORT_DIR/test_export_page1.csv" \
  -w "Status: %{http_code}, Taille: %{size_download} bytes\n"
echo "Contenu (page 1):"
cat "$EXPORT_DIR/test_export_page1.csv"
echo

echo "4. Export Excel paginé (page 2, 5 éléments, tri par ID desc)"
echo "------------------------------------------------------------"
curl -X GET "$API_BASE/apprenants/export/excel/page?page=1&size=5&sortBy=idApprenant&sortDir=desc" \
  -H "Accept: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" \
  -o "$EXPORT_DIR/test_export_page2.xlsx" \
  -w "Status: %{http_code}, Taille: %{size_download} bytes\n"
echo "Type de fichier: $(file "$EXPORT_DIR/test_export_page2.xlsx")"
echo

echo "5. Vérification des fichiers générés"
echo "------------------------------------"
ls -lh $EXPORT_DIR/test_export_*
echo

echo "6. Statistiques globales"
echo "------------------------"
echo "Nombre total d'apprenants dans la base:"
curl -s -X GET "$API_BASE/apprenants" | jq '.pagination.total_elements'
echo "Lignes dans export CSV complet (sans en-tête):"
wc -l < "$EXPORT_DIR/test_export_complet.csv" | awk '{print $1-1}'
echo

echo "=== Tests d'exportation terminés ==="
echo "Fichiers générés dans le dossier: $EXPORT_DIR/"
