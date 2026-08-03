import React, { useState, useEffect } from 'react';
import MapView from './components/MapView';
import StatusPanel from './components/StatusPanel';
import './App.css';

function App() {
  const [gridStatus, setGridStatus] = useState(null);
  const [connectedDevices, setConnectedDevices] = useState([]);
  const [connectionCount, setConnectionCount] = useState(0);

  useEffect(() => {
    // Fetch initial grid status
    fetchGridStatus();

    // Set up WebSocket connection
    const ws = new WebSocket('ws://localhost:8081/ws/iot');

    ws.onopen = () => {
      console.log('WebSocket connected');
    };

    ws.onmessage = (event) => {
      const data = JSON.parse(event.data);
      
      if (data.status === 'CONNECTED') {
        setConnectionCount(data.connectionCount);
      } else if (data.deviceId) {
        // Device update
        setConnectedDevices(prev => {
          const existing = prev.findIndex(d => d.deviceId === data.deviceId);
          if (existing >= 0) {
            const updated = [...prev];
            updated[existing] = data;
            return updated;
          }
          return [...prev, data];
        });
      }
    };

    ws.onerror = (error) => {
      console.error('WebSocket error:', error);
    };

    ws.onclose = () => {
      console.log('WebSocket disconnected');
    };

    return () => {
      ws.close();
    };
  }, []);

  const fetchGridStatus = async () => {
    try {
      const response = await fetch('http://localhost:8081/api/grid/status');
      const data = await response.json();
      setGridStatus(data);
      setConnectionCount(data.activeConnections);
    } catch (error) {
      console.error('Error fetching grid status:', error);
    }
  };

  return (
    <div className="app">
      <header className="app-header">
        <h1>⚡ GridWeaver</h1>
        <p>Virtual Thread IoT Microgrid State Engine</p>
      </header>
      
      <div className="app-content">
        <StatusPanel 
          gridStatus={gridStatus}
          connectionCount={connectionCount}
          deviceCount={connectedDevices.length}
        />
        <MapView devices={connectedDevices} />
      </div>
    </div>
  );
}

export default App;
