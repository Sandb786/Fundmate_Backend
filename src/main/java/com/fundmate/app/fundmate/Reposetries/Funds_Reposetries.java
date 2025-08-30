package com.fundmate.app.fundmate.Reposetries;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fundmate.app.fundmate.Entity_Classes.FundsList;
import java.util.List;


public interface Funds_Reposetries extends MongoRepository<FundsList, String>
{
    // Additional query methods can be defined here if needed
    public FundsList findByTitleAndUserId(String title, String userId);

    public List<FundsList> findByUserId(String userId);

    public List<FundsList> findByTitle(String title);


}
