package com.example.PayoEat_BE.scheduler;

import com.example.PayoEat_BE.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderAutoCancelScheduler {

    private final OrderRepository orderRepository;

    @Scheduled(fixedRate = 60000)
    public void cancelExpiredOrders() {
        System.out.println("[Scheduler] Running cancelExpiredOrders() at " + LocalDateTime.now());

        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(10);
        List<UUID> expiredOrders = orderRepository.findExpiredOrders(cutoffTime);

        System.out.println("[Scheduler] Found " + expiredOrders.size() + " expired orders.");

        for (UUID orderId : expiredOrders) {
            System.out.println("[Scheduler] Cancelling order: " + orderId);
            orderRepository.updateOrderStatus(orderId);
        }
    }
}
