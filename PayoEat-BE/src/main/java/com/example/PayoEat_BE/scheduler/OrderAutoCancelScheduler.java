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

    @Scheduled(fixedRate = 60000)
    public void cancelExpiredOrders() {
        System.out.println("[Scheduler] Running cancelExpiredOrders() at " + LocalDateTime.now(ZoneId.of("Asia/Jakarta")));

        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(10);
        List<UUID> expiredOrders = orderRepository.findExpiredOrders(cutoffTime);

        System.out.println("[Scheduler] Found " + expiredOrders.size() + " expired orders.");

        for (UUID orderId : expiredOrders) {
            System.out.println("[Scheduler] Cancelling order: " + orderId);
            orderRepository.updateOrderStatus(orderId);
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

}
