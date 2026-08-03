package com.gridweaver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IoTDevice {
    private String deviceId;
    private String deviceType; // SOLAR_PANEL, BATTERY, WIND_TURBINE
    private Double latitude;
    private Double longitude;
    private Double powerOutput; // in kW
    private Double batteryLevel; // percentage (0-100)
    private String state; // CHARGING, DISCHARGING, IDLE, FAULT
    private Long timestamp;
}
