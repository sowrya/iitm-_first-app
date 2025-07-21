import React from 'react';
import { FilterOptions } from '../types/wafer';

interface FiltersProps {
  filters: FilterOptions;
  onFiltersChange: (filters: FilterOptions) => void;
  filterOptions: {
    operationIDs: string[];
    productTypes: string[];
    testDates: string[];
  };
  loading?: boolean;
}

const Filters: React.FC<FiltersProps> = ({
  filters,
  onFiltersChange,
  filterOptions,
  loading
}) => {
  const handleFilterChange = (key: keyof FilterOptions, value: string) => {
    onFiltersChange({
      ...filters,
      [key]: value === '' ? undefined : value
    });
  };

  const handleReviewedChange = (value: string) => {
    const reviewedValue = value === '' ? undefined : value === 'true';
    onFiltersChange({
      ...filters,
      reviewed: reviewedValue
    });
  };

  const clearFilters = () => {
    onFiltersChange({});
  };

  const hasActiveFilters = Object.values(filters).some(value => value !== undefined);

  if (loading) {
    return (
      <div className="bg-white rounded-lg shadow-md p-6 mb-6">
        <div className="animate-pulse">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-6 gap-4">
            {Array.from({ length: 6 }).map((_, index) => (
              <div key={index}>
                <div className="h-4 bg-gray-200 rounded w-1/2 mb-2"></div>
                <div className="h-10 bg-gray-200 rounded"></div>
              </div>
            ))}
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="bg-white rounded-lg shadow-md p-6 mb-6">
      <div className="flex items-center justify-between mb-4">
        <h3 className="text-lg font-semibold text-gray-800">Filters</h3>
        {hasActiveFilters && (
          <button
            onClick={clearFilters}
            className="text-sm text-blue-600 hover:text-blue-800 transition-colors"
          >
            Clear All
          </button>
        )}
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-6 gap-4">
        {/* Operation ID Filter */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Operation ID
          </label>
          <select
            value={filters.operationID || ''}
            onChange={(e) => handleFilterChange('operationID', e.target.value)}
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          >
            <option value="">All Operations</option>
            {filterOptions.operationIDs.map((id) => (
              <option key={id} value={id}>
                {id}
              </option>
            ))}
          </select>
        </div>

        {/* Product Type Filter */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Product Type
          </label>
          <select
            value={filters.productType || ''}
            onChange={(e) => handleFilterChange('productType', e.target.value)}
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          >
            <option value="">All Products</option>
            {filterOptions.productTypes.map((type) => (
              <option key={type} value={type}>
                {type}
              </option>
            ))}
          </select>
        </div>

        {/* Test Date Filter */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Test Date
          </label>
          <select
            value={filters.testDate || ''}
            onChange={(e) => handleFilterChange('testDate', e.target.value)}
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          >
            <option value="">All Dates</option>
            {filterOptions.testDates.map((date) => (
              <option key={date} value={date}>
                {new Date(date).toLocaleDateString()}
              </option>
            ))}
          </select>
        </div>

        {/* Wafer ID Filter */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Wafer ID
          </label>
          <input
            type="text"
            value={filters.waferID || ''}
            onChange={(e) => handleFilterChange('waferID', e.target.value)}
            placeholder="Search Wafer ID"
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          />
        </div>

        {/* Review Status Filter */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Review Status
          </label>
          <select
            value={filters.reviewed === undefined ? '' : filters.reviewed.toString()}
            onChange={(e) => handleReviewedChange(e.target.value)}
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          >
            <option value="">All Status</option>
            <option value="false">Needs Review</option>
            <option value="true">Reviewed</option>
          </select>
        </div>

        {/* Active Filters Count */}
        <div className="flex items-end">
          <div className="text-sm text-gray-600">
            {hasActiveFilters && (
              <div className="bg-blue-50 border border-blue-200 rounded-md px-3 py-2">
                <span className="font-medium text-blue-800">
                  {Object.values(filters).filter(v => v !== undefined).length} filter(s) active
                </span>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Filters;