package com.example.PayoEat_BE.scheduler;

import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.repository.OrderRepository;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderAutoCancelScheduler {
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;

    private static final Map<UUID, LocalDateTime> manualOverrides = new ConcurrentHashMap<>();

    public static void recordManualOverride(UUID restaurantId) {
        manualOverrides.put(restaurantId, LocalDateTime.now());
    }

    public static boolean hasManualOverride(UUID restaurantId) {
        return manualOverrides.containsKey(restaurantId);
    }

    public static void clearManualOverride(UUID restaurantId) {
        manualOverrides.remove(restaurantId);
    }

    @Scheduled(fixedRate = 60000)
    public void cancelExpiredOrders() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(10);
        List<UUID> expiredOrders = orderRepository.findExpiredOrders(cutoffTime);

        if (!expiredOrders.isEmpty()) {
            log.info("Cancelling {} expired orders", expiredOrders.size());
        }
        for (UUID orderId : expiredOrders) {
            orderRepository.updateOrderStatus(orderId);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void cancelUnprocessedOrders() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(10);
        List<UUID> expiredOrders = orderRepository.findExpiredUnprocessedOrders(cutoffTime);

        if (!expiredOrders.isEmpty()) {
            log.info("Cancelling {} unprocessed orders", expiredOrders.size());
        }
        for (UUID orderId : expiredOrders) {
            orderRepository.updateOrderStatusToCancelled(orderId);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void autoAcceptExpiredVerifications() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(10);
        List<UUID> expiredVerifications = orderRepository.findExpiredVerificationOrders(cutoffTime);

        if (!expiredVerifications.isEmpty()) {
            log.info("Auto-accepting {} expired payment verifications", expiredVerifications.size());
        }
        for (UUID orderId : expiredVerifications) {
            orderRepository.confirmOrderPayment(orderId);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void updateRestaurantOpenStatusScheduler() {
        List<UUID> restaurantUUIDLists = restaurantRepository.getRestaurantUUIDLists();

        for (UUID restaurantId : restaurantUUIDLists) {
            try {
                boolean updated = restaurantRepository.updateOpenStatusForRestaurant(restaurantId, manualOverrides);
                if (updated) {
                    log.info("Updated open status for restaurant {}", restaurantId);
                    if (hasManualOverride(restaurantId)) {
                        clearManualOverride(restaurantId);
                    }
                }
            } catch (Exception e) {
                log.error("Failed to update open status for restaurant {}: {}", restaurantId, e.getMessage());
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void cancelExpiredScheduledCheckIns() {
        List<UUID> expiredOrders = orderRepository.findExpiredScheduledCheckInOrders();

        if (!expiredOrders.isEmpty()) {
            log.info("Cancelling {} expired scheduled check-ins", expiredOrders.size());
        }
        for (UUID orderId : expiredOrders) {
            orderRepository.cancelExpiredScheduledCheckIn(orderId);
        }
    }

}
