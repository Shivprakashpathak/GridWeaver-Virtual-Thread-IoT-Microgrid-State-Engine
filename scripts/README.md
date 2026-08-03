# Mock IoT Generator

Python script to simulate thousands of concurrent IoT device connections for testing GridWeaver.

## Features

- Simulates 10,000+ concurrent WebSocket connections
- Generates realistic telemetry data
- Supports different device types (Solar Panel, Battery, Wind Turbine)
- Random state transitions and power fluctuations
- Configurable device count and WebSocket URI

## Prerequisites

- Python 3.8+
- pip

## Installation

```bash
pip install -r requirements.txt
```

## Usage

### Basic Usage (10,000 devices)
```bash
python mock_iot_generator.py
```

### Custom Device Count
```bash
python mock_iot_generator.py --count 50000
```

### Custom WebSocket URI
```bash
python mock_iot_generator.py --uri ws://localhost:8080/ws/iot
```

### Full Customization
```bash
python mock_iot_generator.py --count 25000 --uri ws://192.168.1.100:8080/ws/iot
```

## Command Line Arguments

- `--count`: Number of devices to simulate (default: 10,000)
- `--uri`: WebSocket URI to connect to (default: ws://localhost:8080/ws/iot)

## Device Types

The script generates three types of IoT devices:

1. **Solar Panel**: Generates power, no battery
2. **Battery**: Stores and releases energy, has battery level
3. **Wind Turbine**: Generates power, no battery

## Telemetry Data

Each device sends telemetry with the following fields:

- `deviceId`: Unique device identifier
- `deviceType`: Type of device
- `latitude`: Geographic latitude
- `longitude`: Geographic longitude
- `powerOutput`: Current power output in kW
- `batteryLevel`: Battery percentage (for batteries only)
- `state`: Current device state (CHARGING, DISCHARGING, IDLE, FAULT)
- `timestamp`: Unix timestamp in milliseconds

## Simulation Behavior

- Devices are distributed around New York City coordinates
- Power output fluctuates randomly
- Battery levels change based on state
- State transitions occur randomly (1% probability per update)
- Update interval: 0.5-2.0 seconds (random per device)

## Performance

On a modern laptop, the script can handle:
- 10,000 devices with ~500MB RAM
- 50,000 devices with ~2GB RAM
- 100,000 devices with ~4GB RAM

## Stopping the Simulation

Press `Ctrl+C` to stop the simulation gracefully.

## Troubleshooting

### Connection Refused
Ensure the GridWeaver backend is running on the specified URI.

### Too Many Open Files
On Linux/Mac, you may need to increase the file descriptor limit:
```bash
ulimit -n 100000
```

### Memory Issues
Reduce the device count or increase available system RAM.
