package com.example.paymentmicroservice.controller;

import com.example.paymentmicroservice.model.Pay;
import com.example.paymentmicroservice.service.PayService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("pays")
public class PayController {
    @Autowired
    private PayService payService;


    @PostMapping
    public ResponseEntity<Pay> makePay(@RequestBody Pay pay) {
        Pay createdPay = payService.createPay(pay);
        return new ResponseEntity<>(createdPay, HttpStatus.CREATED);
    }
    @PostMapping("/onlinePayment")
    public ResponseEntity<?> makeOnlinePay(@RequestBody Pay pay) throws StripeException {
        try {
            Charge createdPay = payService.onlinePayment((int)pay.getAmount());

            if(createdPay != null){
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("receipt", createdPay.getReceiptUrl());
                jsonResponse.put("id", createdPay.getId());
                jsonResponse.put("amount", createdPay.getAmount());
                jsonResponse.put("source", createdPay.getSource());
                payService.createPay(pay);
                return new ResponseEntity<>(jsonResponse, HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(null, HttpStatus.PAYMENT_REQUIRED);
            }

        } catch (StripeException e) {
            return new ResponseEntity<>("Error al procesar el pago: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Pay>> getAllPays() {
        List<Pay> pays = payService.getAllPays();
        return new ResponseEntity<>(pays, HttpStatus.OK);
    }


    @GetMapping("/{payId}")
    public ResponseEntity<Pay> getPayById(@PathVariable Long payId) {
        Optional<Pay> pay = payService.getPayById(payId);
        return pay.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{payId}")
    public ResponseEntity<Pay> updatePay(@PathVariable Long payId, @RequestBody Pay updatedPay) {
        Pay updatedPayResult = payService.updatePay(payId, updatedPay);
        if (updatedPayResult != null) {
            return new ResponseEntity<>(updatedPayResult, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Pay>> findPaysByCustomerId(@PathVariable Long customerId) {
        List<Pay> pays = payService.findPaysByCustomerId(customerId);
        return new ResponseEntity<>(pays, HttpStatus.OK);
    }
}
