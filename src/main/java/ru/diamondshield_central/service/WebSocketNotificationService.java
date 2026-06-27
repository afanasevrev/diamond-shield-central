package ru.diamondshield_central.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.diamondshield_central.dto.accessevent.AccessEventResponse;
import ru.diamondshield_central.dto.alarmevent.AlarmEventResponse;
import ru.diamondshield_central.dto.websocket.CardBindingNotification;
import ru.diamondshield_central.dto.websocket.DeviceStatusNotification;

@Service
public class WebSocketNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketNotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendAccessEvent(AccessEventResponse event) {
        // Уведомление о новом событии доступа
        messagingTemplate.convertAndSend("/topic/access-events", event);
    }

    public void sendAlarmEvent(AlarmEventResponse event) {
        // Уведомление о новой тревоге
        messagingTemplate.convertAndSend("/topic/alarm-events", event);
    }

    public void sendDeviceStatus(DeviceStatusNotification notification) {
        // Уведомление об изменении статуса оборудования
        messagingTemplate.convertAndSend("/topic/device-status", notification);
    }

    public void sendCardBinding(CardBindingNotification notification) {
        // Уведомление по процессу ожидания карты
        messagingTemplate.convertAndSend("/topic/card-binding", notification);
    }
}