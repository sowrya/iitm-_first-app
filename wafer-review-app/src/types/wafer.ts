export type DispositionClass = 'SendOn' | 'Remeasure' | 'Rework';

export interface MeasurementPoint {
  id: string;
  pointNumber: number;
  imageUrl: string;
  ivl: number;
  evl: number;
  mscc: number;
}

export interface Wafer {
  id: string;
  waferID: string;
  operationID: string;
  productType: string;
  testDate: string;
  mlPrediction: DispositionClass;
  smeDisposition?: DispositionClass;
  feedback?: string;
  measurementPoints: MeasurementPoint[];
  confidence?: number;
  reviewed: boolean;
  reviewedAt?: string;
  reviewedBy?: string;
}

export interface FilterOptions {
  operationID?: string;
  productType?: string;
  testDate?: string;
  waferID?: string;
  reviewed?: boolean;
}

export interface KPIStats {
  totalWafers: number;
  watersToReview: number;
  sendOnPrecision: number;
  remeasurePrecision: number;
  reworkPrecision: number;
  reviewedToday: number;
}