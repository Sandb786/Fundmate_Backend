package com.fundmate.app.fundmate.Controllers;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fundmate.app.fundmate.Entity_Classes.User;
import com.fundmate.app.fundmate.Reposetries.User_Reposetry;
import com.fundmate.app.fundmate.Services.Mail_Service;
import com.fundmate.app.fundmate.Services.OTPGenrator;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;





@RestController
@CrossOrigin(origins = "https://fundmate786.netlify.app") // Allow Only Front end
public class User_Controller 
{

    // Reposetry to access User Data
    @Autowired
    User_Reposetry userRepository;  

    // Service to send mail
    @Autowired
    Mail_Service mailService;

    // Service to Genrate otp
    @Autowired
    OTPGenrator otpGenrator;

    //Temp User
    User tempUser;

    
    // Endpoint to check if the application is running
    @GetMapping("/")
    public ResponseEntity<?> hello() 
    {
        System.out.println("\n Called...");
        
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Connected to Fundmate Application");
    }

    // Endpoint to Create a new user
    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody User user) 
    {

        // Check if the user already exists
        if (userRepository.existsByEmail(user.getEmail().toLowerCase())) 
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists with this email");
        }


        // 1. Generate a unique ID for the user
        String token=UUID.randomUUID().toString();

        // 2. Set User Credentials 
        user.setVerificationToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusMinutes(10)); // Token valid for 10 minutes
        user.setEmailVerified(false);
        user.setEmail(user.getEmail().toLowerCase()); // Normalize email to lowercase
        System.out.println("\n\n Expiry Time: "+user.getTokenExpiry());

        // 3. Set user Data in Temporary User
         tempUser = user;

        // 4. Send mail to user with verification token
           boolean res=mailService.sendVerificationEmail(tempUser);

        if (!res) 
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send verification email");
        }

        System.out.println("\n\n token: " + tempUser.getVerificationToken());


        return ResponseEntity.status(HttpStatus.OK).body("Mail send..");
    }
    

    // Endpoint to verify email using the token
    // Note: name of argument should match the parameter in the URL
    // Example: /verifyEmail?token=your_token_here | String token

    @GetMapping("/verifyEmail")
    public ResponseEntity<?> mailVerification(@RequestParam String token) 
    {
        
       
        // 1. Check if the token is valid
        if (tempUser == null || !tempUser.getVerificationToken().equals(token)  || tempUser.getTokenExpiry().compareTo(LocalDateTime.now()) < 0)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
        }

        // Mark the email as verified
        tempUser.setEmailVerified(true);


        // 2. Save the user to the database
            userRepository.save(tempUser);


        //302 FOUND → means: "Don't stay here. Go to this new URL.
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("https://fundmate786.netlify.app/verify-success")).build();
    }
    
    // Endpoint for User Login..
    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody UserLogin_DTO userDto) 
    {
        

    // 1. Check if the user exists in the database
       User user=userRepository.findByEmail(userDto.getEmail().toLowerCase());

       

    // 2. If user is null, it means user does not exist
        if (user == null) 
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid email");
        }

        // 3. If user exists, check if the password matches
        // Note: In a real application, you should hash the password and compare the hashed values
        if(user.getPassword().equals(userDto.getPassword())) 
        {
            // User found and password matches
            return ResponseEntity.ok(user.getId());

        }
        else 
        {
            // User not found or password does not match
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }
   
        
    }
    
    @GetMapping("/userdetail")
    public ResponseEntity<?> getMethodName(@RequestParam String userId,@RequestParam String type)
    {

        if (type.equals("nameAndEmail"))
        {
            User user=userRepository.findById(userId).get();
            User_DTO user_dto=new User_DTO(user.getName(),user.getEmail());

            return ResponseEntity.ok(user_dto);   
        }
        else
        { 
            return ResponseEntity.ok(userRepository.findById(userId).get());
        }


    }

/***************************************************< Update User Detail >*************************************************/


// 1. Update Name 

@PatchMapping("/updateName/{id}")
public ResponseEntity<?> updateUserName(@PathVariable String id, @RequestParam String newName) 
{
    // Traditional logic : findById -> get Obj -> set Data -> save Obj : all in 3 line

             User user=userRepository.findById(id).get();
             user.setName(newName);
             userRepository.save(user);
   
    return ResponseEntity.ok("Name Changed..."); // It return Updated user Object. 
}

// 2. Update Email

@PatchMapping("/updateEmail/{id}")
public ResponseEntity<?> updateEmail(@PathVariable String id,@RequestParam String newEmail,@RequestParam boolean isVerified)
{

    // Chack If This Email Arady In Our System Or Not 
    if (userRepository.existsByEmail(newEmail.toLowerCase())) 
    {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Email Alrady Present.");
    }

    if (!isVerified) // In First Is 'Flase' So We Send Otp After Right Otp Enter 'True'
    {
       
        // 1. Set Otp
          int otp=otpGenrator.getOtp();

        // 2. Chack use is exist or  not
        if(userRepository.existsById(id)) 
        {
            mailService.sendOTPEmail(newEmail, otp);
        }
       
        return ResponseEntity.ok(otp); 
    }
   
 // After Email Is Verified... update user gmail
    User user=userRepository.findById(id).get();
    user.setEmail(newEmail);
    userRepository.save(user);

    return ResponseEntity.ok("Email Updated");
}
 
/***************************************************< Reset Password >*************************************************/

@PostMapping("/resetPassword/sendOtp/{email}")
public ResponseEntity<?> postMethodName(@PathVariable String email) 
{
    int otp = otpGenrator.getOtp();

    if (!userRepository.existsByEmail(email)) 
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    if (mailService.sendOTPEmail(email, otp)) 
    {
        return ResponseEntity.ok(otp);
    }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
}

@PostMapping("/resetPassword")
public ResponseEntity<?> resetUserPassword(@RequestBody UserLogin_DTO entity) 
{
    
    //1. Find user By Email
    User user=userRepository.findByEmail(entity.getEmail());

    if (user==null) 
    {
       return ResponseEntity.status(404).body("User Not Found");    
    }

    user.setPassword(entity.getPassword());
    userRepository.save(user);

    return ResponseEntity.ok("Done");
}



}





@Data
class UserLogin_DTO
{
    private String email;
    private String password;
}

@Data
@AllArgsConstructor // For Param COnstructur The @DATA is not give that
class User_DTO 
{
 private String name;
 private String email;

}




// 📌 Explanation of the code snippet

// This sets the Location header of the response.

// 📌 It's telling the browser:

// “Please redirect the user to: http://localhost:5173/authLogin.”

// The URI.create(...) just builds a URI object from a string.

// ✅ .build()
// This finalizes the ResponseEntity and returns it.


// 📌 Note: The following code is commented out to avoid blocking the main thread.
/*
 *      try {
        Thread.sleep(5000); // 5000 milliseconds = 5 seconds

       
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
 */