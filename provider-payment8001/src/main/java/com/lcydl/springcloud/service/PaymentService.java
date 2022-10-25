package com.lcydl.springcloud.service;

import com.lcydl.springcloud.entities.Payment;

public interface PaymentService {

    int create(Payment payment);

    Payment getPaymentById(Long id);

}
