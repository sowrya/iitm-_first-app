import { Wafer, MeasurementPoint, DispositionClass, KPIStats } from '../types/wafer';

// Generate mock measurement points
const generateMeasurementPoints = (count: number): MeasurementPoint[] => {
  return Array.from({ length: count }, (_, index) => ({
    id: `point-${index + 1}`,
    pointNumber: index + 1,
    imageUrl: `https://picsum.photos/400/400?random=${index + Math.random()}`,
    ivl: Math.round((Math.random() * 100 + 50) * 100) / 100,
    evl: Math.round((Math.random() * 80 + 40) * 100) / 100,
    mscc: Math.round((Math.random() * 90 + 30) * 100) / 100,
  }));
};

// Generate mock wafer data
const generateMockWafers = (): Wafer[] => {
  const operations = ['OP-001', 'OP-002', 'OP-003', 'OP-004'];
  const productTypes = ['Product-A', 'Product-B', 'Product-C'];
  const dispositions: DispositionClass[] = ['SendOn', 'Remeasure', 'Rework'];
  
  return Array.from({ length: 50 }, (_, index) => {
    const pointCount = Math.random() > 0.5 ? 12 : 118;
    const mlPrediction = dispositions[Math.floor(Math.random() * dispositions.length)];
    const reviewed = Math.random() > 0.6;
    
    return {
      id: `wafer-${index + 1}`,
      waferID: `W-${(index + 1).toString().padStart(4, '0')}`,
      operationID: operations[Math.floor(Math.random() * operations.length)],
      productType: productTypes[Math.floor(Math.random() * productTypes.length)],
      testDate: new Date(Date.now() - Math.random() * 30 * 24 * 60 * 60 * 1000).toISOString().split('T')[0],
      mlPrediction,
      smeDisposition: reviewed ? dispositions[Math.floor(Math.random() * dispositions.length)] : undefined,
      feedback: reviewed ? (Math.random() > 0.5 ? 'Looks good based on historical data' : 'Need to check measurement accuracy') : undefined,
      measurementPoints: generateMeasurementPoints(pointCount),
      confidence: Math.round((Math.random() * 30 + 70) * 100) / 100,
      reviewed,
      reviewedAt: reviewed ? new Date(Date.now() - Math.random() * 5 * 24 * 60 * 60 * 1000).toISOString() : undefined,
      reviewedBy: reviewed ? 'SME-' + Math.ceil(Math.random() * 5) : undefined,
    };
  });
};

let mockWafers = generateMockWafers();

export const mockDataService = {
  // Get all wafers with optional filtering
  getWafers: async (filters?: any): Promise<Wafer[]> => {
    await new Promise(resolve => setTimeout(resolve, 500)); // Simulate API delay
    
    let filteredWafers = mockWafers;
    
    if (filters) {
      if (filters.operationID) {
        filteredWafers = filteredWafers.filter(w => w.operationID === filters.operationID);
      }
      if (filters.productType) {
        filteredWafers = filteredWafers.filter(w => w.productType === filters.productType);
      }
      if (filters.waferID) {
        filteredWafers = filteredWafers.filter(w => w.waferID.toLowerCase().includes(filters.waferID.toLowerCase()));
      }
      if (filters.testDate) {
        filteredWafers = filteredWafers.filter(w => w.testDate === filters.testDate);
      }
      if (filters.reviewed !== undefined) {
        filteredWafers = filteredWafers.filter(w => w.reviewed === filters.reviewed);
      }
    }
    
    return filteredWafers;
  },

  // Get single wafer by ID
  getWafer: async (id: string): Promise<Wafer | null> => {
    await new Promise(resolve => setTimeout(resolve, 300));
    return mockWafers.find(w => w.id === id) || null;
  },

  // Update wafer with SME feedback
  updateWafer: async (id: string, updates: Partial<Wafer>): Promise<Wafer> => {
    await new Promise(resolve => setTimeout(resolve, 500));
    
    const waferIndex = mockWafers.findIndex(w => w.id === id);
    if (waferIndex === -1) {
      throw new Error('Wafer not found');
    }
    
    mockWafers[waferIndex] = {
      ...mockWafers[waferIndex],
      ...updates,
      reviewed: true,
      reviewedAt: new Date().toISOString(),
      reviewedBy: 'Current SME'
    };
    
    return mockWafers[waferIndex];
  },

  // Get KPI statistics
  getKPIStats: async (): Promise<KPIStats> => {
    await new Promise(resolve => setTimeout(resolve, 200));
    
    const totalWafers = mockWafers.length;
    const reviewedWafers = mockWafers.filter(w => w.reviewed);
    const unreviewed = mockWafers.filter(w => !w.reviewed);
    
    // Calculate precision for each disposition class
    const calculatePrecision = (disposition: DispositionClass) => {
      const predicted = reviewedWafers.filter(w => w.mlPrediction === disposition);
      const correct = predicted.filter(w => w.smeDisposition === disposition);
      return predicted.length > 0 ? Math.round((correct.length / predicted.length) * 100) : 0;
    };
    
    const today = new Date().toISOString().split('T')[0];
    const reviewedToday = reviewedWafers.filter(w => w.reviewedAt?.startsWith(today)).length;
    
    return {
      totalWafers,
      watersToReview: unreviewed.length,
      sendOnPrecision: calculatePrecision('SendOn'),
      remeasurePrecision: calculatePrecision('Remeasure'),
      reworkPrecision: calculatePrecision('Rework'),
      reviewedToday
    };
  },

  // Get unique filter options
  getFilterOptions: async () => {
    await new Promise(resolve => setTimeout(resolve, 100));
    
    return {
      operationIDs: Array.from(new Set(mockWafers.map(w => w.operationID))),
      productTypes: Array.from(new Set(mockWafers.map(w => w.productType))),
      testDates: Array.from(new Set(mockWafers.map(w => w.testDate))).sort().reverse()
    };
  }
};