#!/usr/bin/env bash
set -euo pipefail
BASE=http://localhost:8080/api
EMAIL="admin.test.$(date +%s)@example.com"
PASS="$(openssl rand -hex 12 2>/dev/null || echo tempPass123!)"

REG_BODY="{\"email\":\"$EMAIL\",\"motDePasse\":\"$PASS\",\"prenom\":\"Admin\",\"nom\":\"Test\",\"role\":\"ADMIN\",\"telephone\":\"+261340000000\",\"preferencesLangue\":\"fr\",\"themePreference\":\"LIGHT\"}"

HTTP_REG=$(curl -s -o reg.json -w "%{http_code}" -X POST "$BASE/auth/register" -H "Content-Type: application/json" -d "$REG_BODY")
USER_ID=$(python3 - <<'PY' || true
import json
try:
  d=json.load(open(reg.json))
  print(d.get(userId) or d.get(idUtilisateur) or )
except Exception:
  print()
PY
)

LOGIN_BODY="{\"email\":\"$EMAIL\",\"motDePasse\":\"$PASS\"}"
HTTP_LOGIN=$(curl -s -o login.json -w "%{http_code}" -X POST "$BASE/auth/login" -H "Content-Type: application/json" -d "$LOGIN_BODY")
TOKEN=$(python3 - <<'PY' || true
import json
try:
  d=json.load(open(login.json))
  print(d.get(token) or )
except Exception:
  print()
PY
)

printf "\nTests endpoints utilisateur (protégés)\n"
list_code=$(curl -s -o /dev/null -w "%{http_code}" -H "Authorization: Bearer $TOKEN" "$BASE/utilisateurs")
byid_code=$(curl -s -o /dev/null -w "%{http_code}" -H "Authorization: Bearer $TOKEN" "$BASE/utilisateurs/$USER_ID")
prof_code=$(curl -s -o /dev/null -w "%{http_code}" -H "Authorization: Bearer $TOKEN" "$BASE/utilisateurs/profile")
role_code=$(curl -s -o /dev/null -w "%{http_code}" -H "Authorization: Bearer $TOKEN" "$BASE/utilisateurs/by-role/ADMIN")
stats_code=$(curl -s -o /dev/null -w "%{http_code}" -H "Authorization: Bearer $TOKEN" "$BASE/utilisateurs/stats/summary")
lock_code=$(curl -s -o /dev/null -w "%{http_code}" -X POST -H "Authorization: Bearer $TOKEN" "$BASE/utilisateurs/admin/$USER_ID/lock")
unlock_code=$(curl -s -o /dev/null -w "%{http_code}" -X POST -H "Authorization: Bearer $TOKEN" "$BASE/utilisateurs/admin/$USER_ID/unlock")
verify_code=$(curl -s -o /dev/null -w "%{http_code}" -X POST -H "Authorization: Bearer $TOKEN" "$BASE/utilisateurs/admin/$USER_ID/verify-email")
cp_code=$(curl -s -o /dev/null -w "%{http_code}" -X POST -H "Authorization: Bearer $TOKEN" --data-urlencode "oldPassword=$PASS" --data-urlencode "newPassword=$PASS-new" "$BASE/utilisateurs/change-password")

printf "Register (ADMIN)                 %3s  %s\n" "$HTTP_REG" "/auth/register"
printf "Login                           %3s  %s\n" "$HTTP_LOGIN" "/auth/login"
printf "Liste paginée (ADMIN)          %3s  %s\n" "$list_code" "/utilisateurs"
printf "Utilisateur par ID             %3s  %s\n" "$byid_code" "/utilisateurs/$USER_ID"
printf "Mon profil                     %3s  %s\n" "$prof_code" "/utilisateurs/profile"
printf "Par rôle ADMIN                 %3s  %s\n" "$role_code" "/utilisateurs/by-role/ADMIN"
printf "Stats utilisateurs             %3s  %s\n" "$stats_code" "/utilisateurs/stats/summary"
printf "Admin: lock compte             %3s  %s\n" "$lock_code" "/utilisateurs/admin/$USER_ID/lock"
printf "Admin: unlock compte           %3s  %s\n" "$unlock_code" "/utilisateurs/admin/$USER_ID/unlock"
printf "Admin: verify email            %3s  %s\n" "$verify_code" "/utilisateurs/admin/$USER_ID/verify-email"
printf "Change password                %3s  %s\n" "$cp_code" "/utilisateurs/change-password"
