# GridWeaver Frontend

React-based GIS dashboard for real-time microgrid monitoring with Leaflet maps.

## Features

- **Interactive Map**: Leaflet-based geographic visualization of IoT devices
- **Real-time Updates**: WebSocket integration for live device state changes
- **Status Panel**: Live metrics for grid load, connections, and device counts
- **Device Visualization**: Color-coded markers based on device state and type
- **Responsive Design**: Works on desktop and mobile devices

## Prerequisites

- Node.js 18+
- npm or yarn

## Installation

```bash
npm install
```

## Development

```bash
npm run dev
```

The frontend will start on `http://localhost:3000`

## Building for Production

```bash
npm run build
```

## Preview Production Build

```bash
npm run preview
```

## Components

### App.jsx
Main application component that manages WebSocket connection and state.

### MapView.jsx
Leaflet map component displaying IoT device locations with custom markers.

### StatusPanel.jsx
Dashboard panel showing grid metrics and device legends.

## Device States

- **Charging** (Green): Device is storing energy
- **Discharging** (Red): Device is releasing energy to grid
- **Idle** (Blue): Device is in standby mode
- **Fault** (Dark Red): Device has an error condition

## Device Types

- **Solar Panel** (Yellow): Photovoltaic energy generation
- **Battery** (Green): Energy storage systems
- **Wind Turbine** (Blue): Wind energy generation

## WebSocket Integration

The frontend connects to `ws://localhost:8080/ws/iot` and receives:

1. **Connection Status**: Initial message confirming connection
2. **Device Updates**: Real-time telemetry from IoT devices

## Configuration

Edit `vite.config.js` to configure:
- Development server port
- API proxy settings
- WebSocket proxy settings

## Styling

Uses TailwindCSS for utility-first styling. Customize in:
- `tailwind.config.js`: Theme configuration
- `src/index.css`: Global styles
- Component CSS files: Component-specific styles

## Mock Data

When no real devices are connected, the map displays 50 mock devices distributed around New York City coordinates (40.7128, -74.0060).

## Performance

The map efficiently handles:
- 1,000+ markers with smooth rendering
- High-frequency updates (multiple per second)
- Real-time marker color changes based on state transitions
