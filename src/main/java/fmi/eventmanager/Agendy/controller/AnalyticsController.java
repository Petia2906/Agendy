package fmi.eventmanager.Agendy.controller;

import fmi.eventmanager.Agendy.model.dto.EventAnalyticsResponse;
import fmi.eventmanager.Agendy.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/events/{eventId}")
public class AnalyticsController {

    public AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/analytics")
    public ResponseEntity<EventAnalyticsResponse> getEventAnalytics(@PathVariable Long eventId,
                                                                    @AuthenticationPrincipal Long userId) {
        EventAnalyticsResponse response = analyticsService.getAnalytics(eventId, userId);
        return ResponseEntity.ok(response);
    }
}
