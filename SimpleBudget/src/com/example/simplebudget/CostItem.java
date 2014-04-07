package com.example.simplebudget;

public class CostItem {
    private String description;  
    private double cost;  
  
    public void add(String desc, double amount) { 
    	if (desc.isEmpty()){desc = "Generic";}
        this.description = desc;  
        this.cost = amount;  
    }  
  
    public String getDescript() {  
        return description;  
    }  
    public double getAmt() {  
        return cost;  
    } 

}
