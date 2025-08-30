package com.fundmate.app.fundmate.Services;

import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class OTPGenrator 
{

    public int getOtp()
    {
        Random random=new Random();
        
        return random.nextInt(100000,999999);
        
        // YE bhi kar sakte he..
        // return new Random().nextInt(100000,999999);
    }

}
