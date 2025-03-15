package com.example.PayoEat_BE.service.transaction;

import com.example.PayoEat_BE.model.Transaction;
import com.example.PayoEat_BE.request.AddTransactionRequest;

public interface ITransactionService {
    Transaction addOrderTransaction(AddTransactionRequest request, Long userId);
    Transaction addTopUpTransaction(AddTransactionRequest request, Long userId);
}
