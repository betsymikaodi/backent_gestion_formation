import React, { useState, useEffect } from 'react';
import ExportButtons from './ExportButtons';
import './ExportButtons.css';

/**
 * Exemple d'intégration du composant ExportButtons
 * dans une page de gestion des apprenants
 */
const ApprenantManagementPage = () => {
  const [apprenants, setApprenants] = useState([]);
  const [pagination, setPagination] = useState({
    current_page: 0,
    page_size: 20,
    total_elements: 0,
    total_pages: 0
  });
  const [loading, setLoading] = useState(true);

  // Simulation du chargement des données
  useEffect(() => {
    fetchApprenants();
  }, [pagination.current_page, pagination.page_size]);

  const fetchApprenants = async () => {
    setLoading(true);
    try {
      const response = await fetch(
        `/api/apprenants?page=${pagination.current_page}&size=${pagination.page_size}`
      );
      const data = await response.json();
      
      setApprenants(data.data || []);
      setPagination(data.pagination || pagination);
    } catch (error) {
      console.error('Erreur lors du chargement des apprenants:', error);
    }
    setLoading(false);
  };

  const handlePageChange = (newPage) => {
    setPagination(prev => ({ ...prev, current_page: newPage }));
  };

  const handlePageSizeChange = (newSize) => {
    setPagination(prev => ({ ...prev, page_size: newSize, current_page: 0 }));
  };

  return (
    <div className="apprenant-management">
      <div className="page-header">
        <h1>Gestion des Apprenants</h1>
        <p>Total : {pagination.total_elements} apprenants</p>
      </div>

      {/* Section d'exportation */}
      <div className="export-section">
        <ExportButtons
          currentPage={pagination.current_page}
          pageSize={pagination.page_size}
          totalElements={pagination.total_elements}
        />
      </div>

      {/* Table des apprenants */}
      <div className="apprenants-table">
        {loading ? (
          <div className="loading">Chargement...</div>
        ) : (
          <>
            <table className="table">
              <thead>
                <tr>
                  <th>Nom</th>
                  <th>Prénom</th>
                  <th>Email</th>
                  <th>CIN</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {apprenants.map(apprenant => (
                  <tr key={apprenant.idApprenant}>
                    <td>{apprenant.nom}</td>
                    <td>{apprenant.prenom}</td>
                    <td>{apprenant.email}</td>
                    <td>{apprenant.cin}</td>
                    <td>
                      <button className="btn btn-sm">Modifier</button>
                      <button className="btn btn-sm btn-danger">Supprimer</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>

            {/* Pagination */}
            <div className="pagination-controls">
              <div className="pagination-info">
                Page {pagination.current_page + 1} sur {pagination.total_pages}
              </div>
              
              <div className="pagination-buttons">
                <button 
                  onClick={() => handlePageChange(pagination.current_page - 1)}
                  disabled={pagination.current_page === 0}
                  className="btn btn-secondary"
                >
                  Précédent
                </button>
                
                <button 
                  onClick={() => handlePageChange(pagination.current_page + 1)}
                  disabled={pagination.current_page >= pagination.total_pages - 1}
                  className="btn btn-secondary"
                >
                  Suivant
                </button>
              </div>

              <select 
                value={pagination.page_size} 
                onChange={(e) => handlePageSizeChange(parseInt(e.target.value))}
                className="form-select"
              >
                <option value={10}>10 par page</option>
                <option value={20}>20 par page</option>
                <option value={50}>50 par page</option>
                <option value={100}>100 par page</option>
              </select>
            </div>
          </>
        )}
      </div>

      {/* Note d'utilisation */}
      <div className="usage-note">
        <h3>💡 Utilisation des exports</h3>
        <ul>
          <li><strong>Export complet</strong> : Télécharge toutes les données de la base</li>
          <li><strong>Export page courante</strong> : Télécharge uniquement les données affichées</li>
          <li><strong>Format CSV</strong> : Compatible avec Excel, Google Sheets, etc.</li>
          <li><strong>Format Excel</strong> : Format natif Microsoft Excel (.xlsx)</li>
        </ul>
      </div>
    </div>
  );
};

export default ApprenantManagementPage;
