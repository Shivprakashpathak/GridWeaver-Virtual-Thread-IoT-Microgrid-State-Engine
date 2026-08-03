package com.gridweaver.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gridweaver.model.IoTDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class IoTWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(IoTWebSocketHandler.class);
    private static final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private static final AtomicLong connectionCount = new AtomicLong(0);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExecutorService virtualThreadExecutor;

    public IoTWebSocketHandler(ExecutorService virtualThreadExecutor) {
        this.virtualThreadExecutor = virtualThreadExecutor;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        long count = connectionCount.incrementAndGet();
        sessions.put(session.getId(), session);
        logger.info("WebSocket connection established. Session ID: {}, Total connections: {}", 
                    session.getId(), count);
        
        // Send welcome message
        session.sendMessage(new TextMessage(
            objectMapper.writeValueAsString(new ConnectionStatus("CONNECTED", count))
        ));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Handle incoming IoT device data using virtual thread
        virtualThreadExecutor.submit(() -> {
            try {
                String payload = message.getPayload();
                IoTDevice device = objectMapper.readValue(payload, IoTDevice.class);
                logger.debug("Received data from device {}: state={}, power={}kW", 
                            device.getDeviceId(), device.getState(), device.getPowerOutput());
                
                // Process device data (will be enhanced with State Machine in Week 2)
                processDeviceData(device);
                
                // Broadcast to all connected dashboard clients
                broadcastDeviceUpdate(device);
                
            } catch (Exception e) {
                logger.error("Error processing message: {}", e.getMessage(), e);
            }
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        long count = connectionCount.decrementAndGet();
        logger.info("WebSocket connection closed. Session ID: {}, Total connections: {}", 
                    session.getId(), count);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.error("WebSocket transport error for session {}: {}", 
                    session.getId(), exception.getMessage(), exception);
        sessions.remove(session.getId());
    }

    private void processDeviceData(IoTDevice device) {
        // This will be enhanced with State Machine integration in Week 2
        // For now, just log the data
        logger.info("Processing device: {} | Type: {} | State: {} | Power: {}kW | Battery: {}%",
                   device.getDeviceId(), device.getDeviceType(), device.getState(), 
                   device.getPowerOutput(), device.getBatteryLevel());
    }

    private void broadcastDeviceUpdate(IoTDevice device) {
        String message;
        try {
            message = objectMapper.writeValueAsString(device);
            TextMessage textMessage = new TextMessage(message);
            
            sessions.values().forEach(session -> {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(textMessage);
                    } catch (IOException e) {
                        logger.error("Error broadcasting to session {}: {}", 
                                    session.getId(), e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            logger.error("Error broadcasting device update: {}", e.getMessage(), e);
        }
    }

    public long getConnectionCount() {
        return connectionCount.get();
    }

    public static ConcurrentHashMap<String, WebSocketSession> getSessions() {
        return sessions;
    }

    record ConnectionStatus(String status, long connectionCount) {}
}
