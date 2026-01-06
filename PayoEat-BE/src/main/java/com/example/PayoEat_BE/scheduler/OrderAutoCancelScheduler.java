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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderAutoCancelScheduler {
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;

     @Scheduled(fixedRate = 60000) // Comment to disable auto-cancel
    public void cancelExpiredOrders() {
        System.out.println("[Scheduler] Running cancelExpiredOrders() at " + LocalDateTime.now(ZoneId.of("Asia/Jakarta")));

        LocalDateTime cutoffTime = LocalDateTime.now(ZoneId.of("UTC")).minusMinutes(10);
        List<UUID> expiredOrders = orderRepository.findExpiredOrders(cutoffTime);

        System.out.println("[Scheduler] Found " + expiredOrders.size() + " expired payment orders.");

        for (UUID orderId : expiredOrders) {
            System.out.println("[Scheduler] Cancelling expired payment order: " + orderId);
            orderRepository.updateOrderStatus(orderId);
        }
    }

     @Scheduled(fixedRate = 60000) // Comment to disable auto-cancel
    public void cancelUnprocessedOrders() {
        System.out.println("[Scheduler] Running cancelUnprocessedOrders() at " +
                LocalDateTime.now(ZoneId.of("Asia/Jakarta")));

        LocalDateTime cutoffTime =
                LocalDateTime.now(ZoneId.of("UTC")).minusMinutes(10);

        List<UUID> expiredOrders =
                orderRepository.findExpiredUnprocessedOrders(cutoffTime);

        System.out.println("[Scheduler] Found " + expiredOrders.size() + " expired unprocessed orders.");

        for (UUID orderId : expiredOrders) {
            System.out.println("[Scheduler] Cancelling unprocessed order: " + orderId);
            orderRepository.updateOrderStatusToCancelled(orderId);
        }
    }


    @Scheduled(fixedRate = 60000)
    public void updateRestaurantOpenStatusScheduler() {
        List<UUID> restaurantUUIDLists = restaurantRepository.getRestaurantUUIDLists();

        for (UUID restaurantId : restaurantUUIDLists) {
            try {
                restaurantRepository.updateOpenStatusForRestaurant(restaurantId);
            } catch (Exception e) {
                System.out.println("[Scheduler] Error updating restaurant ");
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void cancelExpiredScheduledCheckIns() {
        System.out.println("[Scheduler] Running cancelExpiredScheduledCheckIns() at " + LocalDateTime.now());

        List<UUID> expiredOrders = orderRepository.findExpiredScheduledCheckInOrders();

        System.out.println("[Scheduler] Found " + expiredOrders.size() + " expired scheduled check-in orders.");

        for (UUID orderId : expiredOrders) {
            System.out.println("[Scheduler] Cancelling expired scheduled check-in order: " + orderId);
            orderRepository.cancelExpiredScheduledCheckIn(orderId);
        }
    }

}
