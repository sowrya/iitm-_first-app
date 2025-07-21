# 🔬 Wafer Review System

A modern React-based application designed for Subject Matter Experts (SMEs) to review and validate ML classification predictions for semiconductor wafers.

## 🌟 Features

### Key Capabilities
- **ML Prediction Review**: View ML classification predictions (SendOn, Remeasure, Rework) with confidence scores
- **Interactive Data Points**: Review individual measurement points with images and numerical data (IVL, EVL, MSCC)
- **Comprehensive Filtering**: Filter by Operation ID, Product Type, Test Date, Wafer ID, and Review Status
- **KPI Dashboard**: Real-time metrics showing precision rates, pending reviews, and daily progress
- **SME Feedback System**: Provide disposition feedback and notes for each wafer
- **Edit Capability**: Modify previously submitted reviews

### Advanced Features
- **Image Navigation**: Navigate through multiple measurement points (12-118 points per wafer)
- **Responsive Design**: Modern, mobile-friendly interface built with Tailwind CSS
- **Real-time Updates**: Dynamic KPI calculations based on SME feedback
- **Loading States**: Smooth loading animations and progress indicators

## 🚀 Getting Started

### Prerequisites
- Node.js (v14 or higher)
- npm or yarn

### Installation

1. **Clone the repository** (or navigate to the project directory)
```bash
cd wafer-review-app
```

2. **Install dependencies**
```bash
npm install
```

3. **Start the development server**
```bash
npm start
```

4. **Open your browser** and navigate to `http://localhost:3000`

## 🏗️ Project Structure

```
src/
├── components/           # React components
│   ├── KPIDashboard.tsx # Top-level metrics display
│   ├── Filters.tsx      # Multi-criteria filtering
│   ├── WaferList.tsx    # Table view of wafers
│   └── WaferDetailModal.tsx # Review interface
├── services/            # Data services
│   └── mockData.ts      # Mock API service
├── types/               # TypeScript definitions
│   └── wafer.ts         # Data models
├── App.tsx              # Main application component
├── index.tsx            # Application entry point
└── index.css            # Global styles with Tailwind
```

## 🎛️ Application Components

### KPI Dashboard
- **Total Wafers**: Count of all wafers in system
- **Wafers to Review**: Pending SME reviews
- **Precision Metrics**: SendOn/Remeasure/Rework accuracy rates
- **Daily Progress**: Reviews completed today

### Filtering System
- **Operation ID**: Filter by specific operations
- **Product Type**: Filter by product categories
- **Test Date**: Filter by measurement dates
- **Wafer ID**: Search by wafer identifier
- **Review Status**: Show reviewed/pending items

### Wafer Review Interface
- **Basic Info**: Operation ID, Product Type, Test Date
- **ML Prediction**: Model classification with confidence
- **Measurement Points**: 
  - Navigate through all measurement points
  - View associated images
  - Review IVL, EVL, MSCC values
- **SME Review**:
  - Select disposition (SendOn/Remeasure/Rework)
  - Add optional feedback notes
  - Save or edit existing reviews

## 📊 Data Model

### Wafer Object
```typescript
interface Wafer {
  id: string;
  waferID: string;           // Unique wafer identifier
  operationID: string;       // Manufacturing operation
  productType: string;       // Product category
  testDate: string;          // ISO date string
  mlPrediction: DispositionClass;  // ML classification
  smeDisposition?: DispositionClass; // SME review
  feedback?: string;         // SME notes
  measurementPoints: MeasurementPoint[]; // 12-118 points
  confidence?: number;       // ML confidence (0-100)
  reviewed: boolean;         // Review status
  reviewedAt?: string;       // Review timestamp
  reviewedBy?: string;       // Reviewer identifier
}
```

### Measurement Point
```typescript
interface MeasurementPoint {
  id: string;
  pointNumber: number;       // Point position
  imageUrl: string;          // Associated image
  ivl: number;               // IVL measurement
  evl: number;               // EVL measurement
  mscc: number;              // MSCC measurement
}
```

## 🎨 UI/UX Features

### Modern Design
- **Tailwind CSS**: Utility-first styling framework
- **Responsive Layout**: Works on desktop, tablet, and mobile
- **Consistent Colors**: Status-based color coding throughout
- **Loading States**: Skeleton screens and progress indicators

### User Experience
- **One-Click Review**: Quick access to wafer details
- **Keyboard Navigation**: Arrow keys for measurement points
- **Visual Feedback**: Color-coded status indicators
- **Error Handling**: Graceful handling of missing images/data

## 🔧 Customization

### Adding New Filters
1. Update `FilterOptions` interface in `types/wafer.ts`
2. Add filter UI in `Filters.tsx`
3. Implement filter logic in `mockData.ts`

### Extending Measurement Data
1. Update `MeasurementPoint` interface
2. Modify display in `WaferDetailModal.tsx`
3. Update mock data generation

### Custom KPIs
1. Extend `KPIStats` interface
2. Add calculation logic in `mockData.ts`
3. Update dashboard display in `KPIDashboard.tsx`

## 🚀 Production Deployment

### Build for Production
```bash
npm run build
```

### Environment Setup
- Configure API endpoints (replace mock service)
- Set up authentication if required
- Configure image storage/CDN

### Performance Optimizations
- Image lazy loading implemented
- Component memoization for large lists
- Efficient filtering and pagination ready

## 🤝 Contributing

1. Follow the existing code structure
2. Use TypeScript for type safety
3. Maintain responsive design principles
4. Add appropriate loading states
5. Test on multiple screen sizes

## 📝 Future Enhancements

### Planned Features
- **Advanced Analytics**: Trend analysis and reporting
- **Batch Operations**: Review multiple wafers simultaneously
- **Export Functionality**: PDF/Excel export of reviews
- **User Management**: Role-based access control
- **Audit Trail**: Complete review history tracking

### Technical Improvements
- **Real API Integration**: Replace mock service
- **Offline Support**: PWA capabilities
- **Advanced Search**: Full-text search across all fields
- **Performance**: Virtual scrolling for large datasets

## 📄 License

This project is for demonstration purposes. Adapt according to your organization's requirements.

---

Built with ❤️ using React, TypeScript, and Tailwind CSS
