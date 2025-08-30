package com.fundmate.app.fundmate.Entity_Classes;

import java.util.UUID;

import lombok.Data;

@Data
public class Entries
{
    private String entrieId=UUID.randomUUID().toString().substring(0, 5);
    private String note;
    private int amount;
    private String date;
}
