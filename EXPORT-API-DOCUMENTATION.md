# API d'Exportation des Apprenants

## Vue d'ensemble

L'API d'exportation permet d'exporter les données des apprenants dans deux formats :
- **CSV** : Format texte compatible avec Excel et autres outils
- **Excel (.xlsx)** : Format Microsoft Excel natif

## Endpoints disponibles

### 1. Exportation CSV complète

```http
GET /api/apprenants/export/csv/all
```

**Description** : Exporte tous les apprenants de la base de données au format CSV.

**Réponse** :
- **Content-Type** : `text/csv`
- **Filename** : `apprenants_complet_YYYYMMDD_HHMMSS.csv`

### 2. Exportation Excel complète

```http
GET /api/apprenants/export/excel/all
```

**Description** : Exporte tous les apprenants de la base de données au format Excel.

**Réponse** :
- **Content-Type** : `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet`
- **Filename** : `apprenants_complet_YYYYMMDD_HHMMSS.xlsx`

### 3. Exportation CSV paginée

```http
GET /api/apprenants/export/csv/page?page={page}&size={size}&sortBy={field}&sortDir={direction}
```

**Paramètres** :
- `page` (int, défaut: 0) : Numéro de page (commence à 0)
- `size` (int, défaut: 20) : Nombre d'éléments par page
- `sortBy` (string, défaut: "idApprenant") : Champ de tri
- `sortDir` (string, défaut: "asc") : Direction du tri ("asc" ou "desc")

**Réponse** :
- **Content-Type** : `text/csv`
- **Filename** : `apprenants_page_N_YYYYMMDD_HHMMSS.csv`

### 4. Exportation Excel paginée

```http
GET /api/apprenants/export/excel/page?page={page}&size={size}&sortBy={field}&sortDir={direction}
```

**Paramètres** : Identiques à l'exportation CSV paginée.

**Réponse** :
- **Content-Type** : `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet`
- **Filename** : `apprenants_page_N_YYYYMMDD_HHMMSS.xlsx`

## Structure des données exportées

Les fichiers exportés contiennent les colonnes suivantes :

| Colonne        | Type   | Description                    |
|----------------|--------|--------------------------------|
| nom            | string | Nom de l'apprenant            |
| prenom         | string | Prénom de l'apprenant         |
| email          | string | Adresse email                 |
| telephone      | string | Numéro de téléphone           |
| adresse        | string | Adresse postale               |
| dateNaissance  | string | Date de naissance (yyyy-MM-dd) |
| cin            | string | Numéro CIN                    |

## Exemples d'utilisation

### Avec curl

```bash
# Export CSV complet
curl -X GET "http://localhost:8080/api/apprenants/export/csv/all" \
  -H "Accept: text/csv" \
  -o "export_complet.csv"

# Export Excel d'une page spécifique
curl -X GET "http://localhost:8080/api/apprenants/export/excel/page?page=0&size=10&sortBy=nom&sortDir=asc" \
  -H "Accept: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" \
  -o "export_page1.xlsx"
```

### Avec JavaScript (Frontend React)

```javascript
// Export complet CSV
const exportAllCSV = async () => {
  const response = await fetch('/api/apprenants/export/csv/all');
  const blob = await response.blob();
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = 'apprenants_complet.csv';
  a.click();
};

// Export page courante Excel
const exportCurrentPageExcel = async (page, size) => {
  const response = await fetch(`/api/apprenants/export/excel/page?page=${page}&size=${size}`);
  const blob = await response.blob();
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = `apprenants_page_${page + 1}.xlsx`;
  a.click();
};
```

## Intégration avec l'interface React

### Boutons d'exportation suggérés

```jsx
// Composant d'exportation pour interface React
const ExportButtons = ({ currentPage, pageSize, totalElements }) => {
  return (
    <div className="export-buttons">
      <button onClick={exportAllCSV}>
        📄 Exporter tout (CSV)
      </button>
      <button onClick={exportAllExcel}>
        📊 Exporter tout (Excel)
      </button>
      <button onClick={() => exportCurrentPageCSV(currentPage, pageSize)}>
        📄 Exporter page courante (CSV)
      </button>
      <button onClick={() => exportCurrentPageExcel(currentPage, pageSize)}>
        📊 Exporter page courante (Excel)
      </button>
    </div>
  );
};
```

## Gestion des erreurs

- **200 OK** : Export réussi
- **500 Internal Server Error** : Erreur lors de la génération du fichier

## Performance

- **Export complet** : Recommandé pour < 10 000 apprenants
- **Export paginé** : Recommandé pour les grandes bases de données
- Les fichiers sont générés en mémoire et transmis directement au client

## Sécurité

- Mêmes permissions que l'endpoint de lecture des apprenants
- Pas de données sensibles exposées (mots de passe, etc.)

## Tests

Utilisez le script `./test_export_api.sh` pour tester toutes les fonctionnalités d'exportation.
