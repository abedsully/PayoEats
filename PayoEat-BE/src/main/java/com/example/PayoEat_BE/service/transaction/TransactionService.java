package com.example.PayoEat_BE.service.transaction;

import com.example.PayoEat_BE.enums.TransactionType;
import com.example.PayoEat_BE.enums.UserRoles;
import com.example.PayoEat_BE.exceptions.ForbiddenException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.Order;
import com.example.PayoEat_BE.model.Restaurant;
import com.example.PayoEat_BE.model.Transaction;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.repository.OrderRepository;
import com.example.PayoEat_BE.repository.RestaurantRepository;
import com.example.PayoEat_BE.repository.TransactionRepository;
import com.example.PayoEat_BE.repository.UserRepository;
import com.example.PayoEat_BE.request.AddTransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {
    private final TransactionRepository transactionRepository;
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @Override
    public Transaction addOrderTransaction(AddTransactionRequest request, Long userId) {
        Transaction newTransaction = new Transaction();
        newTransaction.setTransactionAmount(request.getTransactionAmount());
        newTransaction.setTransactionType(request.getTransactionType());

        if (request.getOrderId() != null) {
            Order order = orderRepository.findByIdAndIsActiveFalse(request.getOrderId())
                            .orElseThrow(() -> new NotFoundException("Order not found"));

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User not found"));

            Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(order.getRestaurantId())
                            .orElseThrow(() -> new NotFoundException("Restaurant not found"));


            if (!restaurant.getUserId().equals(userId) && !user.getRoles().equals(UserRoles.RESTAURANT)) {
                throw new ForbiddenException("Sorry you can't finish this order as your current user");
            }

            newTransaction.setOrderId(order.getId());
            newTransaction.setUserId(order.getUserId());
        }

        newTransaction.setDateTime(LocalDateTime.now());

        return transactionRepository.save(newTransaction);
    }

    @Override
    public Transaction addTopUpTransaction(AddTransactionRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.getRoles().equals(UserRoles.CUSTOMER)) {
            throw new ForbiddenException("You can't top up unless you are a customer");
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setTransactionAmount(request.getTransactionAmount());
        newTransaction.setTransactionType(TransactionType.TOPUP);
        newTransaction.setDateTime(LocalDateTime.now());
        newTransaction.setUserId(user.getId());

        return transactionRepository.save(newTransaction);
    }
}
