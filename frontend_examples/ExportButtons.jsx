import React from 'react';
import { Download, FileSpreadsheet, FileText } from 'lucide-react';

/**
 * Composant React pour l'exportation des apprenants
 * Intégration suggérée dans votre interface d'administration
 */
const ExportButtons = ({ 
  currentPage = 0, 
  pageSize = 20, 
  totalElements = 0,
  className = "export-buttons" 
}) => {
  const API_BASE = "/api"; // Ajustez selon votre configuration

  /**
   * Télécharge un fichier blob avec le nom spécifié
   */
  const downloadFile = (blob, filename) => {
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
  };

  /**
   * Gère les erreurs d'export
   */
  const handleExportError = (error) => {
    console.error('Erreur lors de l\'export:', error);
    alert('Erreur lors de l\'exportation. Veuillez réessayer.');
  };

  /**
   * Export CSV complet
   */
  const exportAllCSV = async () => {
    try {
      const response = await fetch(`${API_BASE}/apprenants/export/csv/all`, {
        method: 'GET',
        headers: {
          'Accept': 'text/csv',
        }
      });
      
      if (!response.ok) throw new Error('Échec de l\'export');
      
      const blob = await response.blob();
      const filename = response.headers.get('Content-Disposition')?.split('filename=')[1] || 
                      `apprenants_complet_${new Date().toISOString().slice(0,10)}.csv`;
      
      downloadFile(blob, filename);
    } catch (error) {
      handleExportError(error);
    }
  };

  /**
   * Export Excel complet
   */
  const exportAllExcel = async () => {
    try {
      const response = await fetch(`${API_BASE}/apprenants/export/excel/all`, {
        method: 'GET',
        headers: {
          'Accept': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        }
      });
      
      if (!response.ok) throw new Error('Échec de l\'export');
      
      const blob = await response.blob();
      const filename = response.headers.get('Content-Disposition')?.split('filename=')[1] || 
                      `apprenants_complet_${new Date().toISOString().slice(0,10)}.xlsx`;
      
      downloadFile(blob, filename);
    } catch (error) {
      handleExportError(error);
    }
  };

  /**
   * Export CSV de la page courante
   */
  const exportCurrentPageCSV = async () => {
    try {
      const response = await fetch(
        `${API_BASE}/apprenants/export/csv/page?page=${currentPage}&size=${pageSize}`,
        {
          method: 'GET',
          headers: {
            'Accept': 'text/csv',
          }
        }
      );
      
      if (!response.ok) throw new Error('Échec de l\'export');
      
      const blob = await response.blob();
      const filename = `apprenants_page_${currentPage + 1}_${new Date().toISOString().slice(0,10)}.csv`;
      
      downloadFile(blob, filename);
    } catch (error) {
      handleExportError(error);
    }
  };

  /**
   * Export Excel de la page courante
   */
  const exportCurrentPageExcel = async () => {
    try {
      const response = await fetch(
        `${API_BASE}/apprenants/export/excel/page?page=${currentPage}&size=${pageSize}`,
        {
          method: 'GET',
          headers: {
            'Accept': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
          }
        }
      );
      
      if (!response.ok) throw new Error('Échec de l\'export');
      
      const blob = await response.blob();
      const filename = `apprenants_page_${currentPage + 1}_${new Date().toISOString().slice(0,10)}.xlsx`;
      
      downloadFile(blob, filename);
    } catch (error) {
      handleExportError(error);
    }
  };

  return (
    <div className={className}>
      <div className="export-buttons-group">
        <h3>📤 Exporter les données</h3>
        
        {/* Exports complets */}
        <div className="export-section">
          <h4>Exportation complète ({totalElements} apprenants)</h4>
          <div className="button-group">
            <button 
              onClick={exportAllCSV}
              className="btn btn-export btn-csv"
              title="Exporter tous les apprenants en CSV"
            >
              <FileText size={16} />
              CSV Complet
            </button>
            
            <button 
              onClick={exportAllExcel}
              className="btn btn-export btn-excel"
              title="Exporter tous les apprenants en Excel"
            >
              <FileSpreadsheet size={16} />
              Excel Complet
            </button>
          </div>
        </div>

        {/* Exports de la page courante */}
        <div className="export-section">
          <h4>Page courante ({Math.min(pageSize, totalElements - currentPage * pageSize)} apprenants)</h4>
          <div className="button-group">
            <button 
              onClick={exportCurrentPageCSV}
              className="btn btn-export btn-csv"
              title={`Exporter la page ${currentPage + 1} en CSV`}
            >
              <FileText size={16} />
              CSV Page {currentPage + 1}
            </button>
            
            <button 
              onClick={exportCurrentPageExcel}
              className="btn btn-export btn-excel"
              title={`Exporter la page ${currentPage + 1} en Excel`}
            >
              <FileSpreadsheet size={16} />
              Excel Page {currentPage + 1}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ExportButtons;
