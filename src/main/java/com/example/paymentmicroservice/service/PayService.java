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

    public Charge onlinePayment(double chargeAmount) throws StripeException {
        Stripe.apiKey = stripeApiKey;
        Map<String, Object> params = new HashMap<>();
        int amountInCents = (int) (chargeAmount * 100);
        params.put("amount", amountInCents);
        params.put("currency", "cop");
        params.put("source", "tok_visa");

        try {
            Charge createdCharge = Charge.create(params);

            if (createdCharge.getPaid()) {
                System.out.println("Pago exitoso. ID de carga: " + createdCharge.getId());
                return createdCharge;
            } else {
                System.out.println("El pago no se realizó. Estado: " + createdCharge.getStatus());
            }
        } catch (StripeException e) {
            e.printStackTrace();
        }
        return null;
    }

}
