package com.example.PayoEat_BE.service.balance;

import com.example.PayoEat_BE.enums.UserRoles;
import com.example.PayoEat_BE.exceptions.ForbiddenException;
import com.example.PayoEat_BE.exceptions.NotFoundException;
import com.example.PayoEat_BE.model.Balance;
import com.example.PayoEat_BE.model.User;
import com.example.PayoEat_BE.repository.BalanceRepository;
import com.example.PayoEat_BE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BalanceService implements IBalanceService{
    private final BalanceRepository balanceRepository;
    private final UserRepository userRepository;

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
}
