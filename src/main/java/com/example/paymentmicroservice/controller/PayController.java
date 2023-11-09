package com.example.paymentmicroservice.controller;

import com.example.paymentmicroservice.model.Pay;
import com.example.paymentmicroservice.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pays")
public class PayController {
    @Autowired
    private PayService payService;


    @PostMapping
    public ResponseEntity<Pay> createPay(@RequestBody Pay pay) {
        Pay createdPay = payService.createPay(pay);
        return new ResponseEntity<>(createdPay, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<Pay>> getAllPays() {
        List<Pay> pays = payService.getAllPays();
        return new ResponseEntity<>(pays, HttpStatus.OK);
    }


    @GetMapping("/{payId}")
    public ResponseEntity<Pay> getPayById(@PathVariable Long payId) {
        Optional<Pay> pay = payService.getPayById(payId);
        if (pay.isPresent()) {
            return new ResponseEntity<>(pay.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
