package com.example.aleksi.bottledispenserapp;

import java.util.ArrayList;
import java.util.List;

// Bottledispenser
public class BottleDispenser
{
    private int bottles;
    private double money;
    public Bottle returnBottle;
    private ArrayList<Bottle> bottleList = new ArrayList(bottles);


    /* Lisätty viikko 8 kohdalla, Singleton suunnitteluperiaate*/
    private static BottleDispenser instance = null;

    public BottleDispenser()
    {
        bottles = 6;
        money = 0;

        bottleList.add(new Bottle());
        bottleList.add(new Bottle("Pepsi Max", "Pepsi", 1.5, 2.2));
        bottleList.add(new Bottle("Coca-Cola Zero", "Coca-Cola", 0.5, 2.0));
        bottleList.add(new Bottle("Coca-Cola Zero", "Coca-Cola", 1.5, 2.5));
        bottleList.add(new Bottle("Fanta Zero", "Coca-Cola", 0.5, 1.95));
        bottleList.add(new Bottle("Fanta Zero", "Coca-Cola", 0.5, 1.95));
    }

    public static BottleDispenser getInstance()
    {

        if (instance == null)
        {
            instance = new BottleDispenser();
        }
        else
        {
            System.out.println("Instance already exists.");
        }

        return instance;
    }

    protected void verifyInstance()
    {

        System.out.println(this);
    }


    public double addMoney(int AddMoney)
    {
        money += AddMoney;
        System.out.println("Klink! Lisää rahaa laitteeseen!");
        return money;
    }

    public Bottle buyBottle(int bottleNumber) {

        if ((money <= 0) || (money < bottleList.get(bottleNumber).getBottlePrice()))
        {
            System.out.println("Syötä rahaa ensin!");
        }
        else if ((bottles <= 0))
        {
            bottles = 0;
            System.out.println("Pulloautomaatti on tyhjä!");
        }
        else if(bottleNumber > bottleList.size())
        {
            System.out.println("Out of bottles.");
        }
        else
        {

            money -= bottleList.get((bottleNumber)).getBottlePrice();
            System.out.println("KACHUNK! " + bottleList.get((bottleNumber)).getBottleName() + " tipahti masiinasta!");
            returnBottle = bottleList.get((bottleNumber));
            bottleList.remove((bottleNumber));
            bottles -= 1;
            return returnBottle;
        }
        Bottle failBottle = new Bottle("Empty","",0,0);
        return failBottle;
    }

    public int checkInventory(String bottleToSearch, String bottleSizeToSearch)
    {
        int bottleNameFoundIndex;
        int bottleSizeFoundIndex = -1;
        double bottelSizeToFind = Double.parseDouble(bottleSizeToSearch);
        List<Integer> bottleNamesFoundList = new ArrayList();

        for(int i = 0, maxBottles = bottleList.size(); i < maxBottles; i++)
        {
            if(bottleList.get(i).getBottleName().equals(bottleToSearch))
            {
                bottleNamesFoundList.add(i);
                System.out.println("Oikean niminen pullo löytynyt! " + i);
            }

        }


        if(bottleNamesFoundList.size() > 0)
        {
            for(int i = 0, maxBottles = bottleNamesFoundList.size(); i < maxBottles; i++)
            {
                bottleNameFoundIndex = bottleNamesFoundList.get(i).intValue();

                if(bottelSizeToFind == bottleList.get(bottleNameFoundIndex).getBottleSize())
                {
                    bottleSizeFoundIndex = bottleNameFoundIndex;
                    System.out.println("Oikea pullo löytyy indeksistä: " + bottleNameFoundIndex);
                }



            }
        }

        if(bottleSizeFoundIndex != -1)
        {
            return bottleSizeFoundIndex;
        }

        bottleNamesFoundList.clear();
        bottleSizeFoundIndex = -1;
        return bottleSizeFoundIndex;
    }


    public ArrayList getBottleList()
    {

        for(int j = 0, maxBottles = bottleList.size(); j < maxBottles; j++)
        {
            System.out.println((j+1) + ". Nimi: " + bottleList.get(j).getBottleName());
            System.out.print("\tKoko: " + bottleList.get(j).getBottleSize());
            System.out.println("\tHinta: " + bottleList.get(j).getBottlePrice());
        }

        return bottleList;
    }

    public double getMoney()
    {
        return money;
    }

    public int getBottles()
    {
        return bottles;
    }

    public void returnMoney()
    {
        System.out.print("Klink klink. Sinne menivät rahat! Rahaa tuli ulos ");
        System.out.format("%.2f", money);
        System.out.print("€\n");
        money = 0;
    }

}