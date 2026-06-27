package ru.diamondshield_central.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.diamondshield_central.dto.cardbinding.CardBindingWaitRequest;
import ru.diamondshield_central.service.CardBindingService;

@RestController
@RequestMapping("/api/card-binding")
public class CardBindingController {

    private final CardBindingService cardBindingService;

    public CardBindingController(CardBindingService cardBindingService) {
        this.cardBindingService = cardBindingService;
    }

    @PostMapping("/start-wait")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('IDENTIFIER_MANAGE')")
    public void startWait(@Valid @RequestBody CardBindingWaitRequest request) {
        // Запускаем ожидание карты на указанном считывателе
        cardBindingService.startWait(request);
    }

    @PostMapping("/cancel-wait")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasAuthority('IDENTIFIER_MANAGE')")
    public void cancelWait(@Valid @RequestBody CardBindingWaitRequest request) {
        // Отменяем ожидание карты
        cardBindingService.cancelWait(request);
    }
}