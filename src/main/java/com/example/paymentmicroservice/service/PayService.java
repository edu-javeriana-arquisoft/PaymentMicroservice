package com.example.paymentmicroservice.service;

import com.example.paymentmicroservice.model.Pay;
import com.example.paymentmicroservice.repository.PayRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PayService {
    @Autowired
    PayRepository payRepository;
    @Value("${stripe.apiKey}")
    private String stripeApiKey;
    public Pay createPay(Pay pay) {
        return payRepository.save(pay);
    }

    public List<Pay> getAllPays() {
        return payRepository.findAll();
    }


    public Optional<Pay> getPayById(Long id) {
        return payRepository.findById(id);
    }


    public Pay updatePay(Long id, Pay updatedPay) {
        Optional<Pay> existingPayOptional = payRepository.findById(id);

        if (existingPayOptional.isPresent()) {
            Pay existingPay = existingPayOptional.get();
            existingPay.setCustomerId(updatedPay.getCustomerId());
            existingPay.setAmount(updatedPay.getAmount());
            existingPay.setPaymentType(updatedPay.getPaymentType());
            return payRepository.save(existingPay);
        } else {
            return null;
        }
    }
    public List<Pay> findPaysByCustomerId(Long customerId) {
        return payRepository.findPaysByCustomerId(customerId);
    }

    public Charge onlinePayment() throws StripeException{
        Stripe.apiKey = stripeApiKey;
        Map<String, Object> params = new HashMap<>();
        params.put("amount", 1000); // Monto en centavos
        params.put("currency", "usd");
        params.put("source", "tok_visa"); // Token de tarjeta simulado

        return Charge.create(params);
    }

}
