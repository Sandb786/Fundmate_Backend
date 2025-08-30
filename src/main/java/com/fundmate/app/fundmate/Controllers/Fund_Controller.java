package com.fundmate.app.fundmate.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.fundmate.app.fundmate.Entity_Classes.Entries;
import com.fundmate.app.fundmate.Entity_Classes.FundsList;
import com.fundmate.app.fundmate.Reposetries.Funds_Reposetries;
import com.fundmate.app.fundmate.Reposetries.User_Reposetry;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@CrossOrigin(origins = "https://fundmate786.netlify.app") // Allow Only Front end
public class Fund_Controller 
{

    @Autowired
    Funds_Reposetries fundsReposetries;

    @Autowired
    User_Reposetry userRepository;

/***************************************************< Fund 'C R U D' >*************************************************/

    // 1. Add fund Handler
    @PostMapping("/addFundList")
    public ResponseEntity<?> addFundList(@RequestBody FundsList fundlist) 
    {
       
        if (fundsReposetries.findByTitleAndUserId(fundlist.getTitle(), fundlist.getUserId()) != null) 
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Alrady Exist");
        }

        fundsReposetries.save(fundlist);

        return ResponseEntity.status(HttpStatus.OK).body("Fund Added"); // Return the received FundsList object
    }

    // 2. Quickly add fund Handler
    @PostMapping("/quickAddFund/{id}")
    public ResponseEntity<?> quickAddFund(@PathVariable String id,@RequestBody Entries entrie) 
    {   
        // 1. fatch the FundsList by title
        FundsList fundsList = fundsReposetries.findById(id).get();

        // 2. set list of entries in fundsList object
        fundsList.getEntries().add(entrie);

        // 3. increment total entries count
        fundsList.setTotalEntrie(fundsList.getTotalEntrie() + 1);

        // 4. Save object in database
        fundsReposetries.save(fundsList);

        // Return the created FundsList object
        return ResponseEntity.ok("done");
    }

    // 3. Get all funds Handler
    @GetMapping("/getallFunds")
    public ResponseEntity<?> allFunds(@RequestParam String id)
    {   
        
        return ResponseEntity.ok(fundsReposetries.findByUserId(id)); // Return the temporary list of FundsList                                                                      // objects

    }
    
    @PutMapping("/updateTitle/{id}")
    public ResponseEntity<?> updateFundTitle(@PathVariable String id, @RequestParam String title) 
    {
        
        // Chack if the name is alrady have or not
        if (fundsReposetries.findByUserId(fundsReposetries.findById(id).get().getUserId()).stream().anyMatch(e-> e.getTitle().equals(title.toLowerCase()) && !e.getId().equals(id) )) 
        {
          return ResponseEntity.status(HttpStatus.CONFLICT).body("Title is alrady prasent");  
        }

        // 1. Find The FundList By ID 
       FundsList fundsList=fundsReposetries.findById(id).get();

       // 2. Set New Titile in FundList
       fundsList.setTitle(title.toLowerCase());
       
       // 3. Save the updated title.
       fundsReposetries.save(fundsList);
       
       
       return ResponseEntity.ok("work Done");
    }

    // 4. Delete FundList
    @DeleteMapping("/deleteFundList/{id}")
    public ResponseEntity<?> deleteFund(@PathVariable String id)
    {
      
        fundsReposetries.deleteById(id);

        return ResponseEntity.ok("FundList Deleted");
    }
    
/***************************************************< Entries 'C R U D' >*************************************************/

    // 1. Get all Entries
    @GetMapping("/getAllEntries/{id}")
    public ResponseEntity<?> getallEtries(@PathVariable String id) 
    {
      
        FundsList fundsList = fundsReposetries.findById(id).get();

        return ResponseEntity.ok(fundsList.getEntries()); // Return the list of Entries objects
    }

    @PutMapping("/updateEntrie")
    public ResponseEntity<?> updateEntries(@RequestBody Entries entrie, @RequestParam String fundName, @RequestParam String userId) 
    {
        // 1. fatch the FundsList by title
        FundsList fundsList = fundsReposetries.findByTitleAndUserId(fundName, userId);

        // 2. Find And Replace the entriy.. ()
        fundsList.getEntries().forEach(e -> 
        {
            if (e.getEntrieId().equals(entrie.getEntrieId())) 
            {
                  e.setNote(entrie.getNote());
                  e.setAmount(entrie.getAmount());
            }
        });


        // # Simple Logic
        //
        // List<Entries> entries=fundsList.getEntries();
        // for (Entries e : entries)
        // {
        // if (e.getEntrieId().equals(entrie.getEntrieId()))
        // {
        // e.setNote(entrie.getNote());
        // e.setAmount(entrie.getAmount());
        // }
        // }

        // 3. Save The Updated FundList
        fundsReposetries.save(fundsList);

        return ResponseEntity.ok("ffdff");
    }

    @DeleteMapping("/deleteEntrie")
    public ResponseEntity<?> deleteEntrie(@RequestParam String id, @RequestParam String fundName, @RequestParam String userId)
    {
        
        // 1. fatch the FundsList by title
         FundsList fundsList = fundsReposetries.findByTitleAndUserId(fundName, userId);
   
        
        List<Entries> entries=fundsList.getEntries();
       
        // 2. Efficiently remove the entry from the list where the entry's ID matches the given ID.
        // `removeIf` uses a Predicate (condition) to check each entry and removes it if the condition is true.
        // This uses a Java 8 Predicate to filter and remove in a clean, functional way
                /*
                    x -> x > 5                  // Predicate for integers
                    str -> str.isEmpty()       // Predicate for strings
                    e -> e.getId().equals(id) // Predicate for entries
                */
                
                entries.removeIf(e->e.getEntrieId().equals(id));
    

        // 3. Decrese Total Entrie Count.
        fundsList.setTotalEntrie(fundsList.getTotalEntrie()-1);

        // 4. Save The Updated FundList
        fundsReposetries.save(fundsList);
        
        return ResponseEntity.ok(entries);
    }
    
   

    
}
