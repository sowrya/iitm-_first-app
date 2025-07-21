import React from 'react';
import { KPIStats } from '../types/wafer';

interface KPIDashboardProps {
  stats: KPIStats;
  loading?: boolean;
}

const KPIDashboard: React.FC<KPIDashboardProps> = ({ stats, loading }) => {
  if (loading) {
    return (
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-6 gap-4 mb-8">
        {Array.from({ length: 6 }).map((_, index) => (
          <div key={index} className="bg-white rounded-lg shadow-md p-6">
            <div className="animate-pulse">
              <div className="h-4 bg-gray-200 rounded w-3/4 mb-2"></div>
              <div className="h-8 bg-gray-200 rounded w-1/2"></div>
            </div>
          </div>
        ))}
      </div>
    );
  }

  const kpiCards = [
    {
      title: 'Total Wafers',
      value: stats.totalWafers,
      color: 'bg-blue-50 border-blue-200',
      textColor: 'text-blue-600',
      icon: '📊'
    },
    {
      title: 'Wafers to Review',
      value: stats.watersToReview,
      color: 'bg-yellow-50 border-yellow-200',
      textColor: 'text-yellow-600',
      icon: '⏳'
    },
    {
      title: 'SendOn Precision',
      value: `${stats.sendOnPrecision}%`,
      color: 'bg-green-50 border-green-200',
      textColor: 'text-green-600',
      icon: '✅'
    },
    {
      title: 'Remeasure Precision',
      value: `${stats.remeasurePrecision}%`,
      color: 'bg-yellow-50 border-yellow-200',
      textColor: 'text-yellow-600',
      icon: '🔄'
    },
    {
      title: 'Rework Precision',
      value: `${stats.reworkPrecision}%`,
      color: 'bg-red-50 border-red-200',
      textColor: 'text-red-600',
      icon: '🔧'
    },
    {
      title: 'Reviewed Today',
      value: stats.reviewedToday,
      color: 'bg-purple-50 border-purple-200',
      textColor: 'text-purple-600',
      icon: '📋'
    }
  ];

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-6 gap-4 mb-8">
      {kpiCards.map((card, index) => (
        <div
          key={index}
          className={`${card.color} border-2 rounded-lg p-6 transition-all duration-200 hover:shadow-md`}
        >
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-gray-600 mb-1">
                {card.title}
              </p>
              <p className={`text-2xl font-bold ${card.textColor}`}>
                {card.value}
              </p>
            </div>
            <div className="text-2xl">
              {card.icon}
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};

export default KPIDashboard;