import React from 'react';
import { Wafer, DispositionClass } from '../types/wafer';

interface WaferListProps {
  wafers: Wafer[];
  onWaferSelect: (wafer: Wafer) => void;
  loading?: boolean;
}

const WaferList: React.FC<WaferListProps> = ({ wafers, onWaferSelect, loading }) => {
  const getDispositionColor = (disposition: DispositionClass) => {
    switch (disposition) {
      case 'SendOn':
        return 'bg-green-100 text-green-800 border-green-200';
      case 'Remeasure':
        return 'bg-yellow-100 text-yellow-800 border-yellow-200';
      case 'Rework':
        return 'bg-red-100 text-red-800 border-red-200';
      default:
        return 'bg-gray-100 text-gray-800 border-gray-200';
    }
  };

  const getConfidenceColor = (confidence: number) => {
    if (confidence >= 90) return 'text-green-600';
    if (confidence >= 75) return 'text-yellow-600';
    return 'text-red-600';
  };

  if (loading) {
    return (
      <div className="bg-white rounded-lg shadow-md overflow-hidden">
        <div className="px-6 py-4 border-b border-gray-200">
          <h3 className="text-lg font-semibold text-gray-800">Wafer List</h3>
        </div>
        <div className="p-6">
          <div className="animate-pulse space-y-4">
            {Array.from({ length: 5 }).map((_, index) => (
              <div key={index} className="flex items-center space-x-4 p-4 border rounded-lg">
                <div className="flex-1 space-y-2">
                  <div className="h-4 bg-gray-200 rounded w-1/4"></div>
                  <div className="h-3 bg-gray-200 rounded w-1/2"></div>
                </div>
                <div className="h-8 bg-gray-200 rounded w-20"></div>
                <div className="h-8 bg-gray-200 rounded w-24"></div>
              </div>
            ))}
          </div>
        </div>
      </div>
    );
  }

  if (wafers.length === 0) {
    return (
      <div className="bg-white rounded-lg shadow-md p-8 text-center">
        <div className="text-gray-400 text-4xl mb-4">📋</div>
        <h3 className="text-lg font-semibold text-gray-600 mb-2">No wafers found</h3>
        <p className="text-gray-500">Try adjusting your filters to see more results.</p>
      </div>
    );
  }

  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden">
      <div className="px-6 py-4 border-b border-gray-200">
        <h3 className="text-lg font-semibold text-gray-800">
          Wafer List ({wafers.length} wafers)
        </h3>
      </div>
      
      <div className="overflow-x-auto">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Wafer Info
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Operation & Product
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                ML Prediction
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                SME Review
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Measurement Points
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Actions
              </th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {wafers.map((wafer) => (
              <tr
                key={wafer.id}
                className="hover:bg-gray-50 transition-colors cursor-pointer"
                onClick={() => onWaferSelect(wafer)}
              >
                <td className="px-6 py-4 whitespace-nowrap">
                  <div>
                    <div className="text-sm font-medium text-gray-900">
                      {wafer.waferID}
                    </div>
                    <div className="text-sm text-gray-500">
                      {new Date(wafer.testDate).toLocaleDateString()}
                    </div>
                  </div>
                </td>
                
                <td className="px-6 py-4 whitespace-nowrap">
                  <div>
                    <div className="text-sm font-medium text-gray-900">
                      {wafer.operationID}
                    </div>
                    <div className="text-sm text-gray-500">
                      {wafer.productType}
                    </div>
                  </div>
                </td>
                
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="flex items-center space-x-2">
                    <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full border ${getDispositionColor(wafer.mlPrediction)}`}>
                      {wafer.mlPrediction}
                    </span>
                    {wafer.confidence && (
                      <span className={`text-xs font-medium ${getConfidenceColor(wafer.confidence)}`}>
                        {wafer.confidence}%
                      </span>
                    )}
                  </div>
                </td>
                
                <td className="px-6 py-4 whitespace-nowrap">
                  {wafer.reviewed ? (
                    <div>
                      <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full border ${getDispositionColor(wafer.smeDisposition!)}`}>
                        {wafer.smeDisposition}
                      </span>
                      <div className="text-xs text-gray-500 mt-1">
                        by {wafer.reviewedBy}
                      </div>
                    </div>
                  ) : (
                    <span className="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-gray-100 text-gray-800 border border-gray-200">
                      Pending
                    </span>
                  )}
                </td>
                
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  <div className="flex items-center">
                    <span className="text-lg mr-2">📷</span>
                    {wafer.measurementPoints.length} points
                  </div>
                </td>
                
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <button
                    onClick={(e) => {
                      e.stopPropagation();
                      onWaferSelect(wafer);
                    }}
                    className="text-blue-600 hover:text-blue-900 transition-colors"
                  >
                    {wafer.reviewed ? 'Edit Review' : 'Review'}
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default WaferList;