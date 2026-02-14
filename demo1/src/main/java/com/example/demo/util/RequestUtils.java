package com.example.demo. util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j. Slf4j;
import org. springframework.stereotype.Component;

@Component
@Slf4j
public class RequestUtils {

    /**
     * Get client IP address from request
     * Handles proxies, load balancers, and direct connections
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        // Try different headers (in case of proxy/load balancer)
        String[] IP_HEADER_CANDIDATES = {
                "X-Forwarded-For",      // Standard proxy header
                "Proxy-Client-IP",      // Apache proxy
                "WL-Proxy-Client-IP",   // WebLogic
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "HTTP_VIA",
                "REMOTE_ADDR"           // Direct connection
        };

        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            if (ip != null && ! ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // X-Forwarded-For can contain multiple IPs (client, proxy1, proxy2)
                // Format: "client, proxy1, proxy2"
                // We want the FIRST one (actual client)
                if (ip.contains(",")) {
                    ip = ip.split(",")[0].trim();
                }
                log.debug("IP found in header {}: {}", header, ip);
                return ip;
            }
        }

        // Fallback to remote address
        String ip = request.getRemoteAddr();
        log.debug("IP from remote address: {}", ip);
        return ip;
    }

    /**
     * Get device and browser info from User-Agent header
     */
    public static String getDeviceInfo(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");

        if (userAgent == null || userAgent.isEmpty()) {
            return "Unknown Device";
        }

        return parseUserAgent(userAgent);
    }

    /**
     * Parse User-Agent string to human-readable format
     */
    private static String parseUserAgent(String userAgent) {
        // Extract browser
        String browser = "Unknown Browser";
        if (userAgent.contains("Chrome") && ! userAgent.contains("Edg")) {
            browser = "Chrome";
        } else if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) {
            browser = "Safari";
        } else if (userAgent.contains("Firefox")) {
            browser = "Firefox";
        } else if (userAgent.contains("Edg")) {
            browser = "Edge";
        } else if (userAgent.contains("Opera") || userAgent.contains("OPR")) {
            browser = "Opera";
        }

        // Extract OS
        String os = "Unknown OS";
        if (userAgent. contains("Windows NT 10.0")) {
            os = "Windows 10";
        } else if (userAgent.contains("Windows NT 11. 0")) {
            os = "Windows 11";
        } else if (userAgent.contains("Mac OS X")) {
            os = "macOS";
        } else if (userAgent.contains("Linux")) {
            os = "Linux";
        } else if (userAgent.contains("Android")) {
            os = "Android";
        } else if (userAgent.contains("iPhone") || userAgent.contains("iPad")) {
            os = "iOS";
        }

        // Extract device type
        String deviceType = "Desktop";
        if (userAgent. contains("Mobile")) {
            deviceType = "Mobile";
        } else if (userAgent.contains("Tablet") || userAgent.contains("iPad")) {
            deviceType = "Tablet";
        }

        return String.format("%s on %s (%s)", browser, os, deviceType);
    }

    /**
     * Get full device details (for detailed logging)
     */
    public static DeviceDetails getDeviceDetails(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = getClientIpAddress(request);
        String deviceInfo = getDeviceInfo(request);

        return DeviceDetails.builder()
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .deviceInfo(deviceInfo)
                .browser(extractBrowser(userAgent))
                .os(extractOS(userAgent))
                .deviceType(extractDeviceType(userAgent))
                .build();
    }

    // Helper methods
    private static String extractBrowser(String userAgent) {
        if (userAgent == null) return "Unknown";
        if (userAgent.contains("Chrome") && !userAgent.contains("Edg")) return "Chrome";
        if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) return "Safari";
        if (userAgent.contains("Firefox")) return "Firefox";
        if (userAgent.contains("Edg")) return "Edge";
        return "Unknown";
    }

    private static String extractOS(String userAgent) {
        if (userAgent == null) return "Unknown";
        if (userAgent.contains("Windows NT 10.0")) return "Windows 10";
        if (userAgent.contains("Windows NT 11.0")) return "Windows 11";
        if (userAgent.contains("Mac OS X")) return "macOS";
        if (userAgent.contains("Android")) return "Android";
        if (userAgent.contains("iPhone") || userAgent.contains("iPad")) return "iOS";
        if (userAgent.contains("Linux")) return "Linux";
        return "Unknown";
    }

    private static String extractDeviceType(String userAgent) {
        if (userAgent == null) return "Unknown";
        if (userAgent. contains("Mobile")) return "Mobile";
        if (userAgent.contains("Tablet") || userAgent.contains("iPad")) return "Tablet";
        return "Desktop";
    }
}