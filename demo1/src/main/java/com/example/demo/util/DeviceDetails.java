package com.example.demo.util;

import lombok. AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDetails {
    private String ipAddress;
    private String userAgent;
    private String deviceInfo;      // "Chrome on Windows 10 (Desktop)"
    private String browser;         // "Chrome"
    private String os;              // "Windows 10"
    private String deviceType;      // "Desktop", "Mobile", "Tablet"
}