package com.fundmate.app.fundmate.Entity_Classes;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class FundsList
{
    @Id
    private String id;

    private String userId;
    private String title;
    private int totalEntrie;
    private String date;
    private List<Entries> entries;
}

