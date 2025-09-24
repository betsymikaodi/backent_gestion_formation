403 Forbidden vient de deux choses dans ton cas:
- Tu as POST sur la mauvaise URL (tu as posté sur /api alors que l’endpoint est /api/apprenants).
- Et/ou tu n’as pas envoyé de jeton JWT: les POST sont protégés par Spring Security (GET publics, POST nécessitent Authorization: Bearer …).

Fais comme suit dans Thunder Client:

1) Obtenir un jeton JWT
- Si tu n’as pas encore de compte:
  - POST http://localhost:8080/api/auth/register
    - Headers: Content-Type: application/json
    - Body:
```json
    {
      "email": "admin.thunder@example.com",
      "motDePasse": "Pass123!",
      "prenom": "Admin",
      "nom": "Thunder",
      "role": "ADMIN"
    }
```
- Se connecter:
  - POST http://localhost:8080/api/auth/login
    - Headers: Content-Type: application/json
    - Body:
```json
    {
      "email": "admin.thunder@example.com",
      "motDePasse": "Pass123!"
    }
```
  - Récupère la valeur du champ token dans la réponse.

2) Configurer l’auth dans Thunder Client
- Dans ta requête de création d’étudiant:
  - Méthode: POST
  - URL: http://localhost:8080/api/apprenants
  - Onglet Auth: Type = Bearer, Token = colle le token JWT obtenu
  - Headers: Content-Type: application/json
  - Body (raw JSON) — n’envoie QUE les champs attendus par ApprenantRequest:
```json
  {
    "nom": "NOUVEAU 2",
    "prenom": "Etudiant",
    "email": "nouveau2.etudiant@example.com",
    "telephone": "+26134998877",
    "adresse": "Antananarivo, Madagascar",
    "dateNaissance": "1995-06-15",
    "cin": "999888777667"
  }
```
- Important:
  - N’envoie pas inscriptions ni nomComplet: ce sont des champs de réponse, pas d’entrée.
  - Assure-toi que email et cin sont uniques (sinon 409/400 selon validations).

3) Réessayer
- En envoyant vers /apprenants (pas la racine /api) et avec l’Authorization Bearer, tu dois obtenir 201 Created avec l’objet apprenant en réponse.

Option dév (si tu veux éviter le JWT juste pour tester)
- On peut rendre POST /apprenants public en dev (permitAll) dans la configuration de sécurité, mais ce n’est pas recommandé pour la prod. Dis-moi si tu veux que je te prépare le patch de config pour l’environnement local.
