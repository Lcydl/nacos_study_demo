package com.lcydl.springcloud.service.impl;

import com.lcydl.springcloud.dao.PaymentDao;
import com.lcydl.springcloud.entities.Payment;
import com.lcydl.springcloud.service.PaymentService;

import javax.annotation.Resource;

public class PaymentServiceImpl implements PaymentService {

    @Resource
    private PaymentDao paymentDao;

    @Override
    public int create(Payment payment) {
        return 0;
    }

    @Override
    public Payment getPaymentById(Long id) {
        return null;
    }
}
