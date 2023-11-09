package com.example.paymentmicroservice.repository;

import com.example.paymentmicroservice.model.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PayRepository extends JpaRepository<Pay,Long> {
    List<Pay> findPaysByCustomerId(Long customerId);
    ;

}

