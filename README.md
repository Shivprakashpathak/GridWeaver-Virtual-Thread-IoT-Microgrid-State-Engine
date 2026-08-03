# GridWeaver: Virtual Thread IoT Microgrid State Engine

A cutting-edge demonstration of modern Java (Project Loom) handling massive scale IoT logistics for decentralized energy microgrids.

## Problem Statement

Managing decentralized energy microgrids (e.g., thousands of solar panels and home batteries) requires managing massive concurrent connections. Traditional Java thread pools exhaust OS memory when handling 100,000+ simultaneous WebSocket connections from IoT devices.

## Use Case

A city grid operator monitors the GridWeaver dashboard. As a sudden storm hits, 50,000 solar nodes drop in power output simultaneously. The Java backend, leveraging Java 21's Virtual Threads (Project Loom), handles all 50,000 state changes concurrently without blocking. The Spring State Machine instantly triggers "Discharge" events to home battery nodes to stabilize the grid, with the live map UI reflecting the power rerouting in under a second.

## Key Modules

- **Virtual Thread Ingestion (Java 21 Loom)**: Highly concurrent TCP/WebSocket ingestion layer processing thousands of lightweight IoT pings simultaneously.
- **State Machine Engine (Spring State Machine)**: Manages the complex transition states of batteries (Charging, Discharging, Idle, Fault) based on real-time grid load.
- **Event Broker (Apache Kafka)**: Buffers incoming telemetry spikes to prevent backend overload.
- **GIS Dashboard (React & Leaflet)**: A geographic map interface showing live grid stability, node states, and power flow vectors.

## Cloud Computing Characteristics Applied

- **On-demand self-services**: Automatic provisioning of virtual threads based on IoT connection load
- **Rapid elasticity**: Virtual threads scale instantly from 1 to 100,000+ connections
- **Resource pooling**: Shared thread pool for all IoT device connections
- **Measured service**: Real-time monitoring of thread usage and device states
- **Multi-tenancy**: Multiple grid operators can monitor different regions simultaneously
- **Virtualization**: Virtual threads abstract OS thread resources
- **Resilient computing**: State machine handles fault states and automatic recovery
- **Automation**: Automatic state transitions based on grid load thresholds
- **Sustainability**: Optimized resource usage reduces energy consumption in data centers

## Project Structure

```
Gride/
├── backend/                 # Java 21 Spring Boot backend
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       └── resources/
│   ├── pom.xml
│   └── README.md
├── frontend/                # React frontend with Leaflet
│   ├── src/
│   ├── public/
│   ├── package.json
│   └── README.md
├── scripts/                 # Mock IoT scripts
│   └── mock_iot_generator.py
└── README.md
```

## Prerequisites

- Java 21+ (with Virtual Threads support)
- Node.js 18+
- Python 3.8+ (for mock IoT script)

**Note:** Maven Wrapper is included - no separate Maven installation needed.

## Getting Started

### Backend

**Windows (set JAVA_HOME first):**
```powershell
$env:JAVA_HOME="C:\Program Files\Java\jdk-22"
cd backend
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
export JAVA_HOME=/path/to/java21
cd backend
./mvnw clean install
./mvnw spring-boot:run
```

The backend will start on `http://localhost:8080`

### Frontend

```bash
cd frontend
npm install
npm start
```

The frontend will start on `http://localhost:3000`

### Mock IoT Generator

```bash
cd scripts
python mock_iot_generator.py
```

This will simulate 10,000 concurrent IoT device connections.

## Development Plan

### Week 1
- **Backend**: High-Concurrency Setup with Virtual Threads handling 10,000 concurrent connections
- **Frontend**: Map Scaffolding with Leaflet and static node markers

### Week 2
- **Backend**: State Machine Logic with Spring State Machine
- **Frontend**: Live Updates via WebSocket

### Week 3
- **Backend**: Kafka Integration for decoupling ingestion from processing
- **Frontend**: Data Overlays with heatmaps

### Week 4
- **Backend**: Distributed Logic with regional balancing
- **Frontend**: Event Log panel for auditing

## Technology Stack

### Backend
- Java 21 (Virtual Threads / Project Loom)
- Spring Boot 3.2
- Spring State Machine
- Spring WebSocket
- Apache Kafka
- Maven

### Frontend
- React 18
- Leaflet (GIS mapping)
- WebSocket client
- TailwindCSS
- Vite

## License

MIT License
