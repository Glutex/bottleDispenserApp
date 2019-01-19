package com.example.aleksi.bottledispenserapp;

// bottle class
public class Bottle
{
    protected String bottleName;
    protected String manufacturer;
    protected double bottleSize;
    protected double bottlePrice;

    public Bottle()
    {
        bottleName = "Pepsi Max";
        manufacturer = "Pepsi";
        bottleSize = 0.5;
        bottlePrice = 1.80;
    }

    public Bottle(String name, String company, double size, double price)
    {
        bottleName = name;
        manufacturer = company;
        bottleSize = size;
        bottlePrice = price;
    }

    public String getBottleName()
    {
        return bottleName;
    }

    public String getManufacturer()
    {
        return manufacturer;
    }


    public double getBottleSize()
    {
        return bottleSize;
    }

    public double getBottlePrice()
    {
        return bottlePrice;
    }
}