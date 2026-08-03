#!/usr/bin/env python3
"""
Mock IoT Device Generator for GridWeaver
Simulates 10,000 concurrent IoT device connections using WebSocket
"""

import asyncio
import websockets
import json
import random
import time
from datetime import datetime
import argparse

# Configuration
DEFAULT_DEVICE_COUNT = 10000
DEFAULT_URI = "ws://localhost:8080/ws/iot"
DEVICE_TYPES = ["SOLAR_PANEL", "BATTERY", "WIND_TURBINE"]
DEVICE_STATES = ["CHARGING", "DISCHARGING", "IDLE", "FAULT"]

# Mock city coordinates (New York City area)
BASE_LAT = 40.7128
BASE_LNG = -74.0060


class IoTDevice:
    def __init__(self, device_id, device_type, lat, lng):
        self.device_id = device_id
        self.device_type = device_type
        self.latitude = lat
        self.longitude = lng
        self.power_output = random.uniform(0, 50)
        self.battery_level = random.uniform(0, 100) if device_type == "BATTERY" else None
        self.state = random.choice(DEVICE_STATES)
        self.websocket = None

    def generate_telemetry(self):
        """Generate realistic telemetry data"""
        # Simulate power fluctuations
        self.power_output = max(0, self.power_output + random.uniform(-2, 2))
        
        # Simulate battery level changes for batteries
        if self.device_type == "BATTERY" and self.battery_level is not None:
            if self.state == "DISCHARGING":
                self.battery_level = max(0, self.battery_level - random.uniform(0.1, 0.5))
            elif self.state == "CHARGING":
                self.battery_level = min(100, self.battery_level + random.uniform(0.1, 0.3))
        
        # Random state transitions (low probability)
        if random.random() < 0.01:  # 1% chance of state change
            self.state = random.choice(DEVICE_STATES)
        
        return {
            "deviceId": self.device_id,
            "deviceType": self.device_type,
            "latitude": self.latitude,
            "longitude": self.longitude,
            "powerOutput": round(self.power_output, 2),
            "batteryLevel": round(self.battery_level, 1) if self.battery_level is not None else None,
            "state": self.state,
            "timestamp": int(time.time() * 1000)
        }

    async def connect_and_send(self, uri):
        """Connect to WebSocket and send telemetry"""
        retry_delay = 1
        max_retries = 5
        
        for attempt in range(max_retries):
            try:
                self.websocket = await websockets.connect(uri)
                print(f"[{self.device_id}] Connected successfully")
                break
            except Exception as e:
                print(f"[{self.device_id}] Connection attempt {attempt + 1} failed: {e}")
                if attempt < max_retries - 1:
                    await asyncio.sleep(retry_delay)
                    retry_delay *= 2
                else:
                    print(f"[{self.device_id}] Failed to connect after {max_retries} attempts")
                    return

        try:
            # Send initial telemetry
            await self.send_telemetry()
            
            # Continuously send telemetry updates
            while True:
                await asyncio.sleep(random.uniform(0.5, 2.0))  # Random interval
                await self.send_telemetry()
                
        except websockets.exceptions.ConnectionClosed:
            print(f"[{self.device_id}] Connection closed")
        except Exception as e:
            print(f"[{self.device_id}] Error: {e}")
        finally:
            if self.websocket:
                await self.websocket.close()

    async def send_telemetry(self):
        """Send telemetry data to server"""
        if self.websocket and not self.websocket.closed:
            telemetry = self.generate_telemetry()
            await self.websocket.send(json.dumps(telemetry))


def generate_mock_devices(count):
    """Generate mock IoT devices distributed around a city"""
    devices = []
    
    for i in range(count):
        device_id = f"DEVICE-{str(i + 1).zfill(5)}"
        device_type = random.choice(DEVICE_TYPES)
        
        # Distribute devices around the base coordinates
        lat = BASE_LAT + random.uniform(-0.1, 0.1)
        lng = BASE_LNG + random.uniform(-0.1, 0.1)
        
        device = IoTDevice(device_id, device_type, lat, lng)
        devices.append(device)
    
    return devices


async def run_simulation(device_count, uri):
    """Run the IoT device simulation"""
    print(f"Generating {device_count} mock IoT devices...")
    devices = generate_mock_devices(device_count)
    
    print(f"Starting WebSocket connections to {uri}...")
    print(f"Simulating devices around coordinates: {BASE_LAT}, {BASE_LNG}")
    
    # Create tasks for all devices
    tasks = [device.connect_and_send(uri) for device in devices]
    
    # Run all tasks concurrently
    await asyncio.gather(*tasks, return_exceptions=True)


def main():
    parser = argparse.ArgumentParser(description="Mock IoT Device Generator for GridWeaver")
    parser.add_argument(
        "--count",
        type=int,
        default=DEFAULT_DEVICE_COUNT,
        help=f"Number of devices to simulate (default: {DEFAULT_DEVICE_COUNT})"
    )
    parser.add_argument(
        "--uri",
        type=str,
        default=DEFAULT_URI,
        help=f"WebSocket URI (default: {DEFAULT_URI})"
    )
    
    args = parser.parse_args()
    
    print("=" * 60)
    print("GridWeaver Mock IoT Device Generator")
    print("=" * 60)
    print(f"Device Count: {args.count}")
    print(f"WebSocket URI: {args.uri}")
    print(f"Start Time: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    print("=" * 60)
    
    try:
        asyncio.run(run_simulation(args.count, args.uri))
    except KeyboardInterrupt:
        print("\nSimulation stopped by user")
    except Exception as e:
        print(f"Error: {e}")


if __name__ == "__main__":
    main()
