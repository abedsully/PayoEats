package com.example.PayoEat_BE.service.balance;

import com.example.PayoEat_BE.enums.UserRoles;
import com.example.PayoEat_BE.exceptions.ForbiddenException;
import com.example.PayoEat_BE.exceptions.InvalidException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.Balance;
import com.example.PayoEat_BE.model.Order;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.repository.BalanceRepository;
import com.example.PayoEat_BE.repository.OrderRepository;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import com.example.PayoEat_BE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BalanceService implements IBalanceService{
    private final BalanceRepository balanceRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public Balance activateBalance(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getRoles().equals(UserRoles.GUEST) || user.getRoles().equals(UserRoles.ADMIN)) {
            throw new ForbiddenException("Sorry you can't activate your balance");
        }

        Balance balance = new Balance();
        balance.setIsActive(false);
        balance.setUserId(user.getId());
        balance.setBalance(0.0);
        balance.setOpenedAt(LocalDateTime.now());
        balance.setLastTopUpAt(null);
        return balance;
    }

    @Override
    public Double getMyBalance(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getRoles().equals(UserRoles.GUEST) || user.getRoles().equals(UserRoles.ADMIN)) {
            throw new ForbiddenException("Sorry you can't see your balance");
        }

        Balance balance = balanceRepository.findByUserId(user.getId());

        return balance.getBalance();
    }

    @Override
    public void addBalance(Double amount, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Balance balance = balanceRepository.findByUserId(user.getId());

        Double currentBalance = balance.getBalance();

        balance.setBalance(currentBalance + amount);
        balance.setLastTopUpAt(LocalDateTime.now());

        balanceRepository.save(balance);
    }

    @Override
    public void processPayment(UUID orderId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Order order = orderRepository.findByIdAndIsActiveTrue(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(order.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        User restaurantUser = userRepository.findById(restaurant.getUserId())
                .orElseThrow(() -> new NotFoundException("User restaurant not found"));

        Balance userBalance = balanceRepository.findByUserId(user.getId());

        Balance restaurantBalance = balanceRepository.findByUserId(restaurantUser.getId());

        double currentUserBalance = userBalance.getBalance();
        double currentRestaurantBalance = restaurantBalance.getBalance();

        userBalance.setBalance(currentUserBalance - order.getTotalAmount());

        if (currentUserBalance < order.getTotalAmount()) {
            throw new InvalidException("You don't have enough wallet to pay for this order, Please top up");
        }

        if (order.getTotalAmount() < 0) {
            throw new InvalidException("Amount should not be less than 0");
        }

        restaurantBalance.setBalance(currentRestaurantBalance + order.getTotalAmount());

        order.setIsActive(false);
        balanceRepository.save(restaurantBalance);
        balanceRepository.save(userBalance);
        orderRepository.save(order);
    }
}
