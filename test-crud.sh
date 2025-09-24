#!/usr/bin/env bash
set -euo pipefail
BASE=http://localhost:8080/api
EMAIL="admin.crud.$(date +%s)@example.com"
PASS="Pass123!"

# Register admin
HTTP_REG=$(curl -s -o reg.json -w "%{http_code}" -X POST "$BASE/auth/register" -H "Content-Type: application/json" -d "{\"email\":\"$EMAIL\",\"motDePasse\":\"$PASS\",\"prenom\":\"Admin\",\"nom\":\"Crud\",\"role\":\"ADMIN\"}")
# Get email back (echoed by API) or fallback to generated
REG_EMAIL=$(grep -o '"email":"[^"]*"' reg.json | head -n1 | cut -d '"' -f4 || true)
if [ -z "$REG_EMAIL" ]; then REG_EMAIL="$EMAIL"; fi
# Login admin
HTTP_LOGIN=$(curl -s -o login.json -w "%{http_code}" -X POST "$BASE/auth/login" -H "Content-Type: application/json" -d "{\"email\":\"$REG_EMAIL\",\"motDePasse\":\"$PASS\"}")
TOKEN=$(grep -o '"token":"[^"]*"' login.json | head -n1 | cut -d '"' -f4)
AUTH=( -H "Authorization: Bearer $TOKEN" )

printf "Register %s\nLogin    %s\n" "$HTTP_REG" "$HTTP_LOGIN"

print_res() { printf "%-28s %3s  %s\n" "$1" "$2" "$3"; }

# Formation CRUD
FORM_CREATE='{"nom":"CRUD Test","description":"desc","duree":10,"frais":1000.50}'
code=$(curl -s -o form-create.json -w "%{http_code}" -X POST "$BASE/formations" -H "Content-Type: application/json" "${AUTH[@]}" -d "$FORM_CREATE"); print_res "Formation CREATE" "$code" /formations
FORM_ID=$(grep -o '"idFormation"[[:space:]]*:[[:space:]]*[0-9]*' form-create.json | head -n1 | cut -d: -f2 | tr -d ' ')
code=$(curl -s -o /dev/null -w "%{http_code}" "$BASE/formations/$FORM_ID"); print_res "Formation READ" "$code" "/formations/$FORM_ID"
code=$(curl -s -o form-update.json -w "%{http_code}" -X PUT "$BASE/formations/$FORM_ID" -H "Content-Type: application/json" "${AUTH[@]}" -d '{"nom":"CRUD Test 2","description":"d","duree":12,"frais":2000.00}'); print_res "Formation UPDATE" "$code" "/formations/$FORM_ID"

# Apprenant CRUD
APPR_CREATE='{"nom":"DOE","prenom":"John","email":"john.crud.'"'"$RANDOM"'"'@example.com","telephone":"+261340000001","adresse":"Antananarivo","cin":"CIN'"'"$RANDOM"'"'","dateNaissance":"1990-01-01"}'
code=$(curl -s -o appr-create.json -w "%{http_code}" -X POST "$BASE/apprenants" -H "Content-Type: application/json" "${AUTH[@]}" -d "$APPR_CREATE"); print_res "Apprenant CREATE" "$code" /apprenants
APPR_ID=$(grep -o '"idApprenant"[[:space:]]*:[[:space:]]*[0-9]*' appr-create.json | head -n1 | cut -d: -f2 | tr -d ' ')
code=$(curl -s -o /dev/null -w "%{http_code}" "$BASE/apprenants/$APPR_ID"); print_res "Apprenant READ" "$code" "/apprenants/$APPR_ID"
code=$(curl -s -o appr-update.json -w "%{http_code}" -X PUT "$BASE/apprenants/$APPR_ID" -H "Content-Type: application/json" "${AUTH[@]}" -d '{"nom":"DOE2","prenom":"John","email":"john2.crud@example.com","telephone":"+26134","adresse":"Tana","cin":"CINX","dateNaissance":"1991-01-01"}'); print_res "Apprenant UPDATE" "$code" "/apprenants/$APPR_ID"

# Inscription
INS_CREATE='{"apprenantId":'"$APPR_ID"',"formationId":'"$FORM_ID"',"droitInscription":25000.00}'
code=$(curl -s -o ins-create.json -w "%{http_code}" -X POST "$BASE/inscriptions" -H "Content-Type: application/json" "${AUTH[@]}" -d "$INS_CREATE"); print_res "Inscription CREATE" "$code" /inscriptions
INS_ID=$(grep -o '"idInscription"[[:space:]]*:[[:space:]]*[0-9]*' ins-create.json | head -n1 | cut -d: -f2 | tr -d ' ')
code=$(curl -s -o /dev/null -w "%{http_code}" "$BASE/inscriptions/$INS_ID"); print_res "Inscription READ" "$code" "/inscriptions/$INS_ID"

# Paiement
PAY_CREATE='{"inscriptionId":'"$INS_ID"',"montant":50000.00,"modePaiement":"Virement","module":"Module 1"}'
code=$(curl -s -o pay-create.json -w "%{http_code}" -X POST "$BASE/paiements" -H "Content-Type: application/json" "${AUTH[@]}" -d "$PAY_CREATE"); print_res "Paiement CREATE" "$code" /paiements
PAY_ID=$(grep -o '"idPaiement"[[:space:]]*:[[:space:]]*[0-9]*' pay-create.json | head -n1 | cut -d: -f2 | tr -d ' ')
code=$(curl -s -o /dev/null -w "%{http_code}" "$BASE/paiements/$PAY_ID"); print_res "Paiement READ" "$code" "/paiements/$PAY_ID"

# Utilisateur via endpoints admin
USR_EMAIL="user.crud.$(date +%s)@example.com"
code=$(curl -s -o usr-create.json -w "%{http_code}" -X POST "$BASE/utilisateurs" -H "Content-Type: application/json" "${AUTH[@]}" -d "{\"email\":\"$USR_EMAIL\",\"motDePasse\":\"UserPass1!\",\"prenom\":\"U\",\"nom\":\"Crud\",\"role\":\"USER\"}"); print_res "Utilisateur CREATE" "$code" /utilisateurs
USR_ID=$(grep -o '"idUtilisateur"[[:space:]]*:[[:space:]]*[0-9]*' usr-create.json | head -n1 | cut -d: -f2 | tr -d ' ')
code=$(curl -s -o /dev/null -w "%{http_code}" -H "Authorization: Bearer $TOKEN" "$BASE/utilisateurs/$USR_ID"); print_res "Utilisateur READ" "$code" "/utilisateurs/$USR_ID"

# Deletes (cleanup)
code=$(curl -s -o /dev/null -w "%{http_code}" -X DELETE "$BASE/paiements/$PAY_ID" "${AUTH[@]}"); print_res "Paiement DELETE" "$code" "/paiements/$PAY_ID"
code=$(curl -s -o /dev/null -w "%{http_code}" -X DELETE "$BASE/inscriptions/$INS_ID" "${AUTH[@]}"); print_res "Inscription DELETE" "$code" "/inscriptions/$INS_ID"
code=$(curl -s -o /dev/null -w "%{http_code}" -X DELETE "$BASE/apprenants/$APPR_ID" "${AUTH[@]}"); print_res "Apprenant DELETE" "$code" "/apprenants/$APPR_ID"
code=$(curl -s -o /dev/null -w "%{http_code}" -X DELETE "$BASE/formations/$FORM_ID" "${AUTH[@]}"); print_res "Formation DELETE" "$code" "/formations/$FORM_ID"
