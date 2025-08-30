package com.fundmate.app.fundmate.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.fundmate.app.fundmate.Entity_Classes.User;

@Service
public class Mail_Service 
{

    @Autowired
    private JavaMailSender mailSender;

    // Method to send verification email
    public boolean sendVerificationEmail(User user) 
    {
        // Create a simple email message
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(user.getEmail());
        message.setSubject("Email Verification");
        message.setText("Please verify your email using the following link: " + "https://fundmatebackend-production.up.railway.app/verifyEmail?token="+user.getVerificationToken() +
                        "\nThis token will expire at: " + user.getTokenExpiry());
                        
         mailSender.send(message);

        return true; // Return true if email sent successfully
    }


    // Method to send otp email
    public boolean sendOTPEmail(String email,int otp) 
    {
        // Create a simple email message
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("FundMate OTP for  Verification");
        message.setText("Your Fundmate Otp Is : "+otp);

        // Send Mail Use MailSender Class
        mailSender.send(message);
        System.out.println("Mail send to: "+email+"\t"+"otp : "+otp);


        return true; // Return true if email sent successfully
    }

    
    
}
