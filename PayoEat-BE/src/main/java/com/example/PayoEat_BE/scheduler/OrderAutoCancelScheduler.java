package com.example.PayoEat_BE.scheduler;

import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.repository.OrderRepository;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OrderAutoCancelScheduler {
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;

    private static final Map<UUID, LocalDateTime> manualOverrides = new ConcurrentHashMap<>();

    public static void recordManualOverride(UUID restaurantId) {
        manualOverrides.put(restaurantId, LocalDateTime.now());
        System.out.println("Restaurant " + restaurantId + " manually controlled at " + LocalDateTime.now());
    }

    public static boolean hasManualOverride(UUID restaurantId) {
        return manualOverrides.containsKey(restaurantId);
    }

    public static void clearManualOverride(UUID restaurantId) {
        if (manualOverrides.remove(restaurantId) != null) {
            System.out.println("Restaurant " + restaurantId + " returned to scheduled operation");
        }
    }

     @Scheduled(fixedRate = 60000)
    public void cancelExpiredOrders() {
        System.out.println("[Scheduler] Running cancelExpiredOrders() at " + LocalDateTime.now());

        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(10);
        List<UUID> expiredOrders = orderRepository.findExpiredOrders(cutoffTime);

        System.out.println("[Scheduler] Found " + expiredOrders.size() + " expired payment orders.");

        for (UUID orderId : expiredOrders) {
            System.out.println("[Scheduler] Cancelling expired payment order: " + orderId);
            orderRepository.updateOrderStatus(orderId);
        }
    }

     @Scheduled(fixedRate = 60000)
    public void cancelUnprocessedOrders() {
        System.out.println("[Scheduler] Running cancelUnprocessedOrders() at " +
                LocalDateTime.now());

        LocalDateTime cutoffTime =
                LocalDateTime.now().minusMinutes(10);

        List<UUID> expiredOrders =
                orderRepository.findExpiredUnprocessedOrders(cutoffTime);

        System.out.println("[Scheduler] Found " + expiredOrders.size() + " expired unprocessed orders.");

        for (UUID orderId : expiredOrders) {
            System.out.println("[Scheduler] Cancelling unprocessed order: " + orderId);
            orderRepository.updateOrderStatusToCancelled(orderId);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void autoAcceptExpiredVerifications() {
        System.out.println("[Scheduler] Running autoAcceptExpiredVerifications() at " +
                LocalDateTime.now());

        LocalDateTime cutoffTime =
                LocalDateTime.now().minusMinutes(10);

        List<UUID> expiredVerifications =
                orderRepository.findExpiredVerificationOrders(cutoffTime);

        System.out.println("[Scheduler] Found " + expiredVerifications.size() + " expired verification orders.");

        for (UUID orderId : expiredVerifications) {
            System.out.println("[Scheduler] Auto-accepting expired verification for order: " + orderId);
            orderRepository.confirmOrderPayment(orderId);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void updateRestaurantOpenStatusScheduler() {
        List<UUID> restaurantUUIDLists = restaurantRepository.getRestaurantUUIDLists();

        for (UUID restaurantId : restaurantUUIDLists) {
            try {
                boolean updated = restaurantRepository.updateOpenStatusForRestaurant(restaurantId, manualOverrides);
                if (updated && hasManualOverride(restaurantId)) {
                    clearManualOverride(restaurantId);
                }
            } catch (Exception e) {
                System.out.println("Error updating restaurant " + restaurantId + ": " + e.getMessage());
            }
        }
    }

}
