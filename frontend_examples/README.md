# Intégration Frontend - Exportation des Apprenants

Ce dossier contient les composants React et les exemples d'intégration pour l'API d'exportation des apprenants.

## Fichiers fournis

### `ExportButtons.jsx`
Composant React principal pour l'exportation des données.

**Fonctionnalités :**
- ✅ Export CSV complet
- ✅ Export Excel complet  
- ✅ Export CSV de la page courante
- ✅ Export Excel de la page courante
- ✅ Gestion des erreurs
- ✅ Noms de fichiers avec timestamps
- ✅ Interface utilisateur intuitive

### `ExportButtons.css`
Styles CSS pour les boutons d'exportation avec :
- Design moderne et responsive
- États hover et actifs
- Support des états de loading
- Animations fluides

### `usage-example.jsx`
Exemple complet d'intégration dans une page de gestion des apprenants.

## Installation

1. Copiez les fichiers dans votre projet React
2. Installez les dépendances nécessaires :
   ```bash
   npm install lucide-react
   ```
3. Importez le composant et les styles dans votre page :
   ```jsx
   import ExportButtons from './ExportButtons';
   import './ExportButtons.css';
   ```

## Utilisation de base

```jsx
import ExportButtons from './ExportButtons';

const MyPage = () => {
  const [pagination, setPagination] = useState({
    current_page: 0,
    page_size: 20,
    total_elements: 150
  });

  return (
    <div>
      <ExportButtons
        currentPage={pagination.current_page}
        pageSize={pagination.page_size}
        totalElements={pagination.total_elements}
      />
      {/* Votre contenu ici */}
    </div>
  );
};
```

## Props du composant

| Prop | Type | Défaut | Description |
|------|------|--------|-------------|
| `currentPage` | number | 0 | Page courante (commence à 0) |
| `pageSize` | number | 20 | Nombre d'éléments par page |
| `totalElements` | number | 0 | Nombre total d'apprenants |
| `className` | string | "export-buttons" | Classe CSS personnalisée |

## Configuration

### URL de l'API
Modifiez la constante `API_BASE` dans le composant selon votre environnement :

```javascript
const API_BASE = process.env.NODE_ENV === 'production' 
  ? '/api'  // Production
  : 'http://localhost:8080/api'; // Développement
```

### Gestion des erreurs
Le composant affiche des alertes par défaut. Vous pouvez personnaliser la fonction `handleExportError` :

```javascript
const handleExportError = (error) => {
  // Utiliser votre système de notification
  toast.error('Erreur lors de l\'exportation');
  console.error(error);
};
```

## Personnalisation CSS

### Variables CSS
Vous pouvez personnaliser les couleurs en redéfinissant ces variables :

```css
:root {
  --export-csv-color: #28a745;
  --export-excel-color: #17a2b8;
  --export-bg: #f8f9fa;
  --export-border: #e9ecef;
}
```

### Classes principales
- `.export-buttons` : Container principal
- `.btn-csv` : Boutons CSV
- `.btn-excel` : Boutons Excel
- `.button-group` : Groupe de boutons

## Fonctionnalités avancées

### Ajout d'état de loading
```jsx
const [isExporting, setIsExporting] = useState(false);

const exportAllCSV = async () => {
  setIsExporting(true);
  try {
    // ... logique d'export
  } finally {
    setIsExporting(false);
  }
};

// Dans le JSX
<button 
  className={`btn btn-csv ${isExporting ? 'loading' : ''}`}
  disabled={isExporting}
>
  {isExporting ? 'Export en cours...' : 'CSV Complet'}
</button>
```

### Notification de succès
```jsx
const downloadFile = (blob, filename) => {
  // ... logique de téléchargement
  
  // Notification de succès
  toast.success(`Fichier ${filename} téléchargé avec succès`);
};
```

## Tests

Pour tester les fonctionnalités d'export, utilisez le script backend :
```bash
./test_export_api.sh
```

## Support des navigateurs

Le composant est compatible avec :
- Chrome 80+
- Firefox 75+
- Safari 13+
- Edge 80+

## Dépendances

- React 16.8+ (hooks)
- lucide-react (icônes)
- Navigateur supportant Fetch API et Blob

## Contribution

Pour contribuer :
1. Suivez les conventions de nommage React
2. Ajoutez des PropTypes si nécessaire
3. Testez sur différents navigateurs
4. Documentez les nouvelles fonctionnalités
