import React, { useState, useEffect } from 'react';
import { Wafer, DispositionClass } from '../types/wafer';

interface WaferDetailModalProps {
  wafer: Wafer | null;
  isOpen: boolean;
  onClose: () => void;
  onSave: (updates: Partial<Wafer>) => Promise<void>;
}

const WaferDetailModal: React.FC<WaferDetailModalProps> = ({
  wafer,
  isOpen,
  onClose,
  onSave
}) => {
  const [currentPointIndex, setCurrentPointIndex] = useState(0);
  const [smeDisposition, setSmeDisposition] = useState<DispositionClass>('SendOn');
  const [feedback, setFeedback] = useState('');
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (wafer) {
      setSmeDisposition(wafer.smeDisposition || wafer.mlPrediction);
      setFeedback(wafer.feedback || '');
      setCurrentPointIndex(0);
    }
  }, [wafer]);

  if (!isOpen || !wafer) return null;

  const currentPoint = wafer.measurementPoints[currentPointIndex];

  const handleSave = async () => {
    setSaving(true);
    try {
      await onSave({
        smeDisposition,
        feedback: feedback.trim() || undefined
      });
      onClose();
    } catch (error) {
      console.error('Failed to save wafer review:', error);
    } finally {
      setSaving(false);
    }
  };

  const nextPoint = () => {
    if (currentPointIndex < wafer.measurementPoints.length - 1) {
      setCurrentPointIndex(currentPointIndex + 1);
    }
  };

  const prevPoint = () => {
    if (currentPointIndex > 0) {
      setCurrentPointIndex(currentPointIndex - 1);
    }
  };

  const getDispositionColor = (disposition: DispositionClass) => {
    switch (disposition) {
      case 'SendOn':
        return 'border-green-500 bg-green-50 text-green-700';
      case 'Remeasure':
        return 'border-yellow-500 bg-yellow-50 text-yellow-700';
      case 'Rework':
        return 'border-red-500 bg-red-50 text-red-700';
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg shadow-xl max-w-6xl w-full max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="px-6 py-4 border-b border-gray-200 flex items-center justify-between">
          <h2 className="text-xl font-semibold text-gray-800">
            Wafer Review - {wafer.waferID}
          </h2>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600 transition-colors"
          >
            <span className="text-2xl">&times;</span>
          </button>
        </div>

        <div className="p-6">
          {/* Wafer Info Section */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
            <div className="bg-gray-50 p-4 rounded-lg">
              <h3 className="text-sm font-medium text-gray-600">Operation ID</h3>
              <p className="text-lg font-semibold text-gray-800">{wafer.operationID}</p>
            </div>
            <div className="bg-gray-50 p-4 rounded-lg">
              <h3 className="text-sm font-medium text-gray-600">Product Type</h3>
              <p className="text-lg font-semibold text-gray-800">{wafer.productType}</p>
            </div>
            <div className="bg-gray-50 p-4 rounded-lg">
              <h3 className="text-sm font-medium text-gray-600">Test Date</h3>
              <p className="text-lg font-semibold text-gray-800">
                {new Date(wafer.testDate).toLocaleDateString()}
              </p>
            </div>
            <div className="bg-gray-50 p-4 rounded-lg">
              <h3 className="text-sm font-medium text-gray-600">ML Prediction</h3>
              <div className="flex items-center space-x-2">
                <span className={`px-2 py-1 text-sm font-semibold rounded-full border ${getDispositionColor(wafer.mlPrediction)}`}>
                  {wafer.mlPrediction}
                </span>
                {wafer.confidence && (
                  <span className="text-sm text-gray-600">
                    ({wafer.confidence}%)
                  </span>
                )}
              </div>
            </div>
          </div>

          {/* Measurement Points Section */}
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6">
            {/* Image and Navigation */}
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <h3 className="text-lg font-semibold text-gray-800">
                  Measurement Point {currentPoint.pointNumber}
                </h3>
                <div className="flex items-center space-x-2">
                  <button
                    onClick={prevPoint}
                    disabled={currentPointIndex === 0}
                    className="px-3 py-1 text-sm bg-gray-100 text-gray-600 rounded hover:bg-gray-200 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                  >
                    ← Prev
                  </button>
                  <span className="text-sm text-gray-600">
                    {currentPointIndex + 1} of {wafer.measurementPoints.length}
                  </span>
                  <button
                    onClick={nextPoint}
                    disabled={currentPointIndex === wafer.measurementPoints.length - 1}
                    className="px-3 py-1 text-sm bg-gray-100 text-gray-600 rounded hover:bg-gray-200 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                  >
                    Next →
                  </button>
                </div>
              </div>

              {/* Image */}
              <div className="bg-gray-100 rounded-lg overflow-hidden">
                <img
                  src={currentPoint.imageUrl}
                  alt={`Measurement point ${currentPoint.pointNumber}`}
                  className="w-full h-64 object-cover"
                  onError={(e) => {
                    const target = e.target as HTMLImageElement;
                    target.src = 'https://via.placeholder.com/400x400/e5e7eb/9ca3af?text=Image+Not+Available';
                  }}
                />
              </div>

              {/* Quick Navigation */}
              <div className="flex flex-wrap gap-2">
                {wafer.measurementPoints.map((_, index) => (
                  <button
                    key={index}
                    onClick={() => setCurrentPointIndex(index)}
                    className={`px-2 py-1 text-xs rounded transition-colors ${
                      index === currentPointIndex
                        ? 'bg-blue-500 text-white'
                        : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
                    }`}
                  >
                    {index + 1}
                  </button>
                ))}
              </div>
            </div>

            {/* Measurement Data */}
            <div className="space-y-4">
              <h3 className="text-lg font-semibold text-gray-800">Measurement Data</h3>
              
              <div className="grid grid-cols-1 gap-4">
                <div className="bg-blue-50 p-4 rounded-lg border border-blue-200">
                  <h4 className="text-sm font-medium text-blue-600 mb-1">IVL</h4>
                  <p className="text-2xl font-bold text-blue-800">{currentPoint.ivl}</p>
                </div>
                
                <div className="bg-green-50 p-4 rounded-lg border border-green-200">
                  <h4 className="text-sm font-medium text-green-600 mb-1">EVL</h4>
                  <p className="text-2xl font-bold text-green-800">{currentPoint.evl}</p>
                </div>
                
                <div className="bg-purple-50 p-4 rounded-lg border border-purple-200">
                  <h4 className="text-sm font-medium text-purple-600 mb-1">MSCC</h4>
                  <p className="text-2xl font-bold text-purple-800">{currentPoint.mscc}</p>
                </div>
              </div>

              {/* Point Summary */}
              <div className="bg-gray-50 p-4 rounded-lg">
                <h4 className="text-sm font-medium text-gray-600 mb-2">Point Summary</h4>
                <div className="text-sm text-gray-700 space-y-1">
                  <p>Point ID: {currentPoint.id}</p>
                  <p>Position: {currentPoint.pointNumber}</p>
                  <p>Quality indicators within expected ranges</p>
                </div>
              </div>
            </div>
          </div>

          {/* SME Review Section */}
          <div className="space-y-4">
            <h3 className="text-lg font-semibold text-gray-800">SME Review</h3>
            
            {/* Disposition Selection */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Your Disposition
              </label>
              <div className="flex space-x-4">
                {(['SendOn', 'Remeasure', 'Rework'] as DispositionClass[]).map((disposition) => (
                  <label
                    key={disposition}
                    className={`flex items-center space-x-2 px-4 py-2 rounded-lg border-2 cursor-pointer transition-all ${
                      smeDisposition === disposition
                        ? getDispositionColor(disposition)
                        : 'border-gray-200 bg-white text-gray-700 hover:bg-gray-50'
                    }`}
                  >
                    <input
                      type="radio"
                      name="disposition"
                      value={disposition}
                      checked={smeDisposition === disposition}
                      onChange={(e) => setSmeDisposition(e.target.value as DispositionClass)}
                      className="sr-only"
                    />
                    <span className="font-medium">{disposition}</span>
                  </label>
                ))}
              </div>
            </div>

            {/* Feedback */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Feedback (Optional)
              </label>
              <textarea
                value={feedback}
                onChange={(e) => setFeedback(e.target.value)}
                rows={4}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="Add any notes or observations about this wafer..."
              />
            </div>
          </div>

          {/* Action Buttons */}
          <div className="flex justify-end space-x-4 mt-6 pt-6 border-t border-gray-200">
            <button
              onClick={onClose}
              disabled={saving}
              className="px-6 py-2 text-gray-600 border border-gray-300 rounded-md hover:bg-gray-50 transition-colors disabled:opacity-50"
            >
              Cancel
            </button>
            <button
              onClick={handleSave}
              disabled={saving}
              className="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors disabled:opacity-50 flex items-center space-x-2"
            >
              {saving && (
                <div className="animate-spin w-4 h-4 border-2 border-white border-t-transparent rounded-full"></div>
              )}
              <span>{saving ? 'Saving...' : 'Save Review'}</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default WaferDetailModal;