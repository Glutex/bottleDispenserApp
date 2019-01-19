package com.example.aleksi.bottledispenserapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class MainActivity extends Activity
{

    Context context = null;
    TextView appTitleText;
    TextView machineInstructionText;
    TextView moneyInstructionText;
    TextView moneyAmountText;
    TextView bottleNameText;
    TextView bottleSizeText;
    TextView receiptSaveText;
    Spinner bottleListSpinner;
    Spinner bottleSizeSpinner;
    SeekBar moneySeekBar;

    ArrayList<Bottle> bottleListTemp = new ArrayList();
    ArrayList<String> bottleArrayNames = new ArrayList();
    ArrayAdapter<String> dataAdapter;
    ArrayAdapter<String> dataAdapterSize;
    ArrayList bottleArrayPrice = new ArrayList();
    ArrayList bottleArraySize = new ArrayList();

    int progressChangedValue = 0;
    int arrayIndex = 0;
    int buyBottleIndex = -1;
    double moneyBuff = 0;

    CharSequence selectedBottleName;
    CharSequence selectedBottleSize;
    CharSequence textBuffer;
    String fileName = null;

    Bottle receiptBottle;
    ArrayList<Bottle> receiptList = new ArrayList();
    // implementing singleton model
    BottleDispenser bottleMachine = BottleDispenser.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // context for save file
        context = MainActivity.this;
        System.out.println("kansion sijainti: " + context.getFilesDir());

        // textview setup
        appTitleText = (TextView) findViewById(R.id.appTitle);
        machineInstructionText = (TextView) findViewById(R.id.infoTextView);
        moneyInstructionText = (TextView) findViewById(R.id.moneyTextView);
        moneyAmountText = (TextView) findViewById(R.id.moneyAmountView);
        bottleNameText = (TextView) findViewById(R.id.bottleTextView);
        bottleSizeText = (TextView) findViewById(R.id.bottleSizeView);
        receiptSaveText = (TextView) findViewById(R.id.receiptTextView);


        // seekBar setup
        moneySeekBar = (SeekBar) findViewById(R.id.seekBar);
        moneySeekBar.setMax(10); // 10 e max input at a time
        moneySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar)
            {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar)
            {
                Toast.makeText(MainActivity.this, "Seek bar progress is :" + progressChangedValue, Toast.LENGTH_SHORT).show();
            }
        });

        getBottleListData();
        // spinner 1
        bottleNameSpinnerUpdate();
        // spinner 2
        bottleSizeSpinnerUpdate();

        // static instruction texts
        appTitleText.setText("Vending Machine");
        moneyInstructionText.setText("Slide for the amount of money to insert");
        bottleNameText.setText("Choose a drink");
        bottleSizeText.setText("Choose a size");

    }


    public void insertMoney(View v)
    {
        moneyBuff = bottleMachine.addMoney(progressChangedValue);
        moneyAmountText.setText(String.format ("%.2f", moneyBuff));
        moneySeekBar.setProgress(0);
    }


    public void buyBottle(View v)
    {
        machineInstructionText.setText("");
        // check for the bottle
        buyBottleIndex = bottleMachine.checkInventory(selectedBottleName.toString(), selectedBottleSize.toString());
        if(buyBottleIndex != -1)
        {
            machineInstructionText.setText("You bought: " + selectedBottleName + " size: " + selectedBottleSize + " Index: " + arrayIndex + "I: "+ buyBottleIndex);
            receiptList.add(bottleMachine.buyBottle(buyBottleIndex));
        }
        else
        {
            machineInstructionText.setText("The requested bottle is not in stock");
        }

        moneyBuff = bottleMachine.getMoney();
        moneyAmountText.setText(String.format ("%.2f", moneyBuff));

    }

    public void bottleNameSpinnerUpdate()
    {

        // spinner for the bottle name
        bottleListSpinner =  (Spinner) findViewById(R.id.spinner);
        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bottleArrayNames);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        bottleListSpinner.setAdapter(dataAdapter);


        // set listener that retrieves the selected item
        bottleListSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long id)
            {
                try
                {
                    selectedBottleName = bottleListSpinner.getItemAtPosition(position).toString();
                    arrayIndex = position;
                }
                catch (Exception e)
                {
                    machineInstructionText.setText("Error in spinner element");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });
    }

    public void bottleSizeSpinnerUpdate()
    {

        // spinner for the bottle name
        bottleSizeSpinner =  (Spinner) findViewById(R.id.spinner2);
        // Creating adapter for spinner
        dataAdapterSize = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bottleArraySize);
        // Drop down layout style - list view with radio button
        dataAdapterSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        bottleSizeSpinner.setAdapter(dataAdapterSize);


        // set listener that retrieves the selected item
        bottleSizeSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long id)
            {
                try
                {
                    selectedBottleSize = bottleSizeSpinner.getItemAtPosition(position).toString();
                    arrayIndex = position;
                }
                catch (Exception e)
                {
                    machineInstructionText.setText("Error in spinner2 element");
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });
    }

    public void getBottleListData()
    {
        // get bottleList name, size, prize
        bottleListTemp =  bottleMachine.getBottleList();
        for(int j = 0, maxBottles = bottleListTemp.size(); j < maxBottles; j++)
        {
            bottleArrayNames.add(bottleListTemp.get(j).getBottleName());
            bottleArrayPrice.add(bottleListTemp.get(j).getBottlePrice());
            bottleArraySize.add(bottleListTemp.get(j).getBottleSize());
        }
    }

    // save / write file
    public void saveReceipt (View v)
    {

        receiptSaveText.setText("printing receipt...");

        try
        {
            CharSequence fileText = "kuitti.txt";
            fileName = fileText.toString();
            OutputStreamWriter saveFile = new OutputStreamWriter(context.openFileOutput(fileName, context.MODE_PRIVATE));
            String saveFileString = "";
            for(int j = 0, maxReturnBottles = receiptList.size(); j < maxReturnBottles; j++)
            {
                saveFile.write(receiptList.get(j).getBottleName());
                saveFile.write(" ");
                saveFile.write(String.format("%.2f", receiptList.get(j).getBottleSize()));
                saveFile.write(" L ");
                saveFile.write(String.format("%.2f", receiptList.get(j).getBottlePrice()));
                saveFile.write(" € \n");
            }

            saveFile.close();

        }
        catch(IOException e)
        {
            Log.e( "IOexception",  "Syötevirhe");
        }
        finally
        {
            receiptSaveText.setText("File saved");
        }

    }

}


