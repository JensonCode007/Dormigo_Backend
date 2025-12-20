package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OtpService {

    public String generateOTPCode(){

        int otp = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(otp);
    }
    public LocalDateTime getExpiryDate(){
        return LocalDateTime.now().plusMinutes(10);
    }
    public void sendOTPToBuyer(String email, String otpCode){


    }
    public boolean verifyOTP(String requestOTPCode, String orderOTPCode, LocalDateTime expiryDate){
        if(Objects.equals(requestOTPCode, orderOTPCode) && !expiryDate.isBefore(LocalDateTime.now())){
            return true;
        }
        else{
            return false;
        }
    }
}
