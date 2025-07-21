import React, { useState, useEffect } from 'react';
import { Wafer, FilterOptions, KPIStats } from './types/wafer';
import { mockDataService } from './services/mockData';
import KPIDashboard from './components/KPIDashboard';
import Filters from './components/Filters';
import WaferList from './components/WaferList';
import WaferDetailModal from './components/WaferDetailModal';
import './App.css';

function App() {
  // State management
  const [wafers, setWafers] = useState<Wafer[]>([]);
  const [filteredWafers, setFilteredWafers] = useState<Wafer[]>([]);
  const [kpiStats, setKpiStats] = useState<KPIStats>({
    totalWafers: 0,
    watersToReview: 0,
    sendOnPrecision: 0,
    remeasurePrecision: 0,
    reworkPrecision: 0,
    reviewedToday: 0
  });
  const [filters, setFilters] = useState<FilterOptions>({});
  const [filterOptions, setFilterOptions] = useState({
    operationIDs: [] as string[],
    productTypes: [] as string[],
    testDates: [] as string[]
  });
  const [selectedWafer, setSelectedWafer] = useState<Wafer | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [loading, setLoading] = useState(true);
  const [updating, setUpdating] = useState(false);

  // Load initial data
  useEffect(() => {
    const loadInitialData = async () => {
      setLoading(true);
      try {
        const [wafersData, filterOptionsData, kpiData] = await Promise.all([
          mockDataService.getWafers(),
          mockDataService.getFilterOptions(),
          mockDataService.getKPIStats()
        ]);
        
        setWafers(wafersData);
        setFilteredWafers(wafersData);
        setFilterOptions(filterOptionsData);
        setKpiStats(kpiData);
      } catch (error) {
        console.error('Failed to load initial data:', error);
      } finally {
        setLoading(false);
      }
    };

    loadInitialData();
  }, []);

  // Apply filters whenever filters change
  useEffect(() => {
    const applyFilters = async () => {
      try {
        const filtered = await mockDataService.getWafers(filters);
        setFilteredWafers(filtered);
      } catch (error) {
        console.error('Failed to apply filters:', error);
      }
    };

    applyFilters();
  }, [filters]);

  // Handle filter changes
  const handleFiltersChange = (newFilters: FilterOptions) => {
    setFilters(newFilters);
  };

  // Handle wafer selection for review
  const handleWaferSelect = (wafer: Wafer) => {
    setSelectedWafer(wafer);
    setIsModalOpen(true);
  };

  // Handle wafer review save
  const handleWaferSave = async (updates: Partial<Wafer>) => {
    if (!selectedWafer) return;

    setUpdating(true);
    try {
      const updatedWafer = await mockDataService.updateWafer(selectedWafer.id, updates);
      
      // Update the wafer in our local state
      setWafers(prev => prev.map(w => w.id === selectedWafer.id ? updatedWafer : w));
      setFilteredWafers(prev => prev.map(w => w.id === selectedWafer.id ? updatedWafer : w));
      
      // Refresh KPI stats
      const newKpiStats = await mockDataService.getKPIStats();
      setKpiStats(newKpiStats);
      
      setIsModalOpen(false);
      setSelectedWafer(null);
    } catch (error) {
      console.error('Failed to save wafer review:', error);
      throw error; // Re-throw to be handled by the modal
    } finally {
      setUpdating(false);
    }
  };

  // Handle modal close
  const handleModalClose = () => {
    setIsModalOpen(false);
    setSelectedWafer(null);
  };

  // Refresh data
  const handleRefresh = async () => {
    setLoading(true);
    try {
      const [wafersData, kpiData] = await Promise.all([
        mockDataService.getWafers(filters),
        mockDataService.getKPIStats()
      ]);
      
      setWafers(wafersData);
      setFilteredWafers(wafersData);
      setKpiStats(kpiData);
    } catch (error) {
      console.error('Failed to refresh data:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      {/* Header */}
      <header className="bg-white shadow-sm border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            <div className="flex items-center">
              <h1 className="text-2xl font-bold text-gray-900">
                🔬 Wafer Review System
              </h1>
            </div>
            <div className="flex items-center space-x-4">
              <button
                onClick={handleRefresh}
                disabled={loading}
                className="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50 transition-colors"
              >
                {loading ? (
                  <div className="animate-spin w-4 h-4 border-2 border-gray-600 border-t-transparent rounded-full mr-2"></div>
                ) : (
                  <span className="mr-2">🔄</span>
                )}
                Refresh
              </button>
              <div className="text-sm text-gray-500">
                SME Dashboard
              </div>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* KPI Dashboard */}
        <KPIDashboard stats={kpiStats} loading={loading} />

        {/* Filters */}
        <Filters
          filters={filters}
          onFiltersChange={handleFiltersChange}
          filterOptions={filterOptions}
          loading={loading}
        />

        {/* Wafer List */}
        <WaferList
          wafers={filteredWafers}
          onWaferSelect={handleWaferSelect}
          loading={loading}
        />

        {/* Summary Footer */}
        {!loading && (
          <div className="mt-8 text-center text-sm text-gray-500">
            Showing {filteredWafers.length} of {wafers.length} wafers
            {Object.values(filters).some(v => v !== undefined) && ' (filtered)'}
          </div>
        )}
      </main>

      {/* Wafer Detail Modal */}
      <WaferDetailModal
        wafer={selectedWafer}
        isOpen={isModalOpen}
        onClose={handleModalClose}
        onSave={handleWaferSave}
      />

      {/* Global Loading Overlay */}
      {updating && (
        <div className="fixed inset-0 bg-black bg-opacity-25 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 shadow-xl">
            <div className="flex items-center space-x-3">
              <div className="animate-spin w-6 h-6 border-2 border-blue-600 border-t-transparent rounded-full"></div>
              <span className="text-gray-700">Updating wafer review...</span>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default App;
