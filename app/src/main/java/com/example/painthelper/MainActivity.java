/******************************************************************
 *  Paint Helper
 *  @Author : Brandon Lowery
 *
 *
 *  @Version : BETA 1
 *  @Date: 8/1/2017
 *
 *  Version Changes: None (first version)
 *
 *******************************************************************/


package com.example.painthelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class MainActivity extends AppCompatActivity{

    //constants
    private static final int NUM_FIELDS = 6;   //tells program how many formula fields there are
    int CONVERT_FROM_SETTING=0;   //needed to tell program what to convert from


    //OnCreate - executed when the app starts
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //*****************************Declarations*********************************

        //declare buttons and retrieve corresponding Views
        Button button_sample = (Button) findViewById(R.id.button_sample);   //convert to sample
        Button button_quart = (Button) findViewById(R.id.button_quart);     //convert to quart
        Button button_gallon = (Button) findViewById(R.id.button_gallon);   //convert to gallon
        Button button_5gallon = (Button) findViewById(R.id.button_5gal);    //convert to 5-gallon

        final int[] ozIds = {R.id.editText_oz1, R.id.editText_oz2, R.id.editText_oz3, R.id.editText_oz4, R.id.editText_oz5, R.id.editText_oz6};
        final int[] dropIds = {R.id.editText_drop1, R.id.editText_drop2, R.id.editText_drop3, R.id.editText_drop4, R.id.editText_drop5, R.id.editText_drop6};

        //*****************************End Declarations*****************************



        //******************Set Up Spinner (Drop-down Menu)***********************
        Spinner dropDown = (Spinner) findViewById(R.id.spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.convert_from, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        dropDown.setAdapter(adapter);

        //handle selection by user - CONVERT_FROM_SETTING will tell the conversion method
        //below how to perform its calculations.
        dropDown.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch(position) {
                    case 0:
                        CONVERT_FROM_SETTING = 8;   //sample selected (8oz)
                        break;
                    case 1:
                        CONVERT_FROM_SETTING = 32;  //quart selected (32oz)
                        break;
                    case 2:
                        CONVERT_FROM_SETTING = 128; //gallon selected (128oz)
                        break;
                    case 3:
                        CONVERT_FROM_SETTING = 640; //5-gallon selected (640oz)
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do Nothing
            }
        });
        //***************************************************************************

        //Convert to sample
        //setup listener
        button_sample.setOnClickListener(
                new View.OnClickListener()
                {

                    public void onClick(View v)
                    {
                        //Now that the sample button has been pressed we must convert the formula
                        //We proceed by cycling through the EditText views using the arrays of IDs that we set up above
                        //NOTE - both arrays have a fixed number of elements and both lengths will always be the same so
                        //  a constant is used for their length.
                        for (int i = 0; i < NUM_FIELDS; i++)
                        {
                            convertToSample((EditText)findViewById(ozIds[i]), (EditText) findViewById(dropIds[i]));
                        }

                    }
                }
        );

        //Convert to Quart
        //setup listener
        button_quart.setOnClickListener(
                new View.OnClickListener()
                {

                    public void onClick(View v)
                    {
                        //Now that the sample button has been pressed we must convert the formula
                        //We proceed by cycling through the EditText views using the arrays of IDs that we set up above
                        //NOTE - both arrays have a fixed number of elements and both lengths will always be the same so
                        //  a constant is used for their length.
                        for (int i = 0; i < NUM_FIELDS; i++)
                        {
                            convertToQuart((EditText)findViewById(ozIds[i]), (EditText) findViewById(dropIds[i]));
                        }

                    }
                }
        );

        //Convert to Gallon
        //setup listener
        button_gallon.setOnClickListener(
                new View.OnClickListener()
                {

                    public void onClick(View v)
                    {
                        //Now that the sample button has been pressed we must convert the formula
                        //We proceed by cycling through the EditText views using the arrays of IDs that we set up above
                        //NOTE - both arrays have a fixed number of elements and both lengths will always be the same so
                        //  a constant is used for their length.
                        for (int i = 0; i < NUM_FIELDS; i++)
                        {
                            convertToGallon((EditText)findViewById(ozIds[i]), (EditText) findViewById(dropIds[i]));
                        }

                    }
                }
        );

        //Convert to 5-Gallon
        //setup listener
        button_5gallon.setOnClickListener(
                new View.OnClickListener()
                {

                    public void onClick(View v)
                    {
                        //Now that the sample button has been pressed we must convert the formula
                        //We proceed by cycling through the EditText views using the arrays of IDs that we set up above
                        //NOTE - both arrays have a fixed number of elements and both lengths will always be the same so
                        //  a constant is used for their length.
                        for (int i = 0; i < NUM_FIELDS; i++)
                        {
                            convertTo5Gallon((EditText)findViewById(ozIds[i]), (EditText) findViewById(dropIds[i]));
                        }

                    }
                }
        );

    }

    //                                                          End of OnCreate
    //*****************************************************************************************************************************************
    //*****************************************************************************************************************************************
    //*****************************************************************************************************************************************
    //*****************************************************************************************************************************************
    //                                                          Conversion Methods

    //ConvertToSample
    //Converts one colorant formula to sample size
    // - Receives the ID of an EditText for ounces and for drops
    protected void convertToSample(EditText oz_box, EditText drop_box)
    {
        //check to make sure both boxes are not empty
        if(!oz_box.getText().toString().isEmpty() || !drop_box.getText().toString().isEmpty())
        {
            //now both boxes must be checked individually to set an empty box to zero
            if(oz_box.getText().toString().isEmpty())
                oz_box.setText(Double.toString(0));

            if(drop_box.getText().toString().isEmpty())
                drop_box.setText(Double.toString(0));

            //initialize temporary doubles for calculations
            double ounces = Double.parseDouble(oz_box.getText().toString());
            double drops = Double.parseDouble(drop_box.getText().toString());
            double convRate;

            switch(CONVERT_FROM_SETTING)
            {
                case 8:     //Sample - already a sample, so skip
                    return;
                case 32:    //Quart
                    convRate = .25;
                    break;
                case 128:  //Gallon
                    convRate = .0625;
                    break;
                case 640:   //5-Gallon
                    convRate = .0125;
                    break;
                default:
                    return;
            }
            drops += ounces*384;  //convert ounces to drops an add all drops together
            drops = drops*convRate; // convert drops to sample size
            ounces = 0; //reset ounces to zero to prepare for dividing drops into ounces.

            //Extract whole ounces from total drops until drops are < 1 ounce
            while(drops>=384)
            {
                ounces += 1;
                drops = drops-384;
            }

            //set values
            oz_box.setText(Double.toString(ounces));
            drop_box.setText(Double.toString(drops));
        }

    }
    

    /**
     * Converts one colorant formula to quart size.
     * @param oz_box - ID of EditText for ounces
     * @param drop_box - ID of EditText for drops
     */
    protected void convertToQuart(EditText oz_box, EditText drop_box)
    {
        //check to make sure both boxes are not empty
        if(!oz_box.getText().toString().isEmpty() || !drop_box.getText().toString().isEmpty())
        {
            //now both boxes must be checked individually to set an empty box to zero
            if(oz_box.getText().toString().isEmpty())
                oz_box.setText(Double.toString(0));

            if(drop_box.getText().toString().isEmpty())
                drop_box.setText(Double.toString(0));

            //initialize temporary doubles for calculations
            double ounces = Double.parseDouble(oz_box.getText().toString());
            double drops = Double.parseDouble(drop_box.getText().toString());
            double convRate;

            switch(CONVERT_FROM_SETTING)
            {
                case 8:
                    convRate = 4; //Sample
                    break;
                case 32:    //Already a quart - move on
                    return;
                case 128:  //Gallon
                    convRate = .25;
                    break;
                case 640:   //5-Gallon
                    convRate = .05;
                    break;
                default:
                    return;
            }
            drops += ounces*384;  //convert ounces to drops an add all drops together
            drops = drops*convRate; // convert drops to sample size
            ounces = 0; //reset ounces to zero to prepare for dividing drops into ounces.

            //Extract whole ounces from total drops until drops are < 1 ounce
            //NOTE - 384 drops = 1 ounce
            while(drops>=384)
            {
                ounces += 1;
                drops = drops-384;
            }

            //set values
            oz_box.setText(Double.toString(ounces));
            drop_box.setText(Double.toString(drops));

        }

    }

    //ConvertToGallon
    //Converts one colorant formula to gallon
    // - Receives the ID of an EditText for ounces and for drops
    protected void convertToGallon(EditText oz_box, EditText drop_box)
    {
        //check to make sure both boxes are not empty
        if(!oz_box.getText().toString().isEmpty() || !drop_box.getText().toString().isEmpty())
        {
            //now both boxes must be checked individually to set an empty box to zero
            if(oz_box.getText().toString().isEmpty())
                oz_box.setText(Double.toString(0));

            if(drop_box.getText().toString().isEmpty())
                drop_box.setText(Double.toString(0));

            //initialize temporary doubles for calculations
            double ounces = Double.parseDouble(oz_box.getText().toString());
            double drops = Double.parseDouble(drop_box.getText().toString());
            double convRate;

            switch(CONVERT_FROM_SETTING)
            {
                case 8:
                    convRate = 16; //Sample
                    break;
                case 32:
                    convRate = 4;   //Quart
                    return;
                case 128:  //Already a gallon
                    return;
                case 640:   //5-Gallon
                    convRate = .2;
                    break;
                default:
                    return;
            }
            drops += ounces*384;  //convert ounces to drops an add all drops together
            drops = drops*convRate; // convert drops to sample size
            ounces = 0; //reset ounces to zero to prepare for dividing drops into ounces.

            //Extract whole ounces from total drops until drops are < 1 ounce
            //NOTE - 384 drops = 1 ounce
            while(drops>=384)
            {
                ounces += 1;
                drops = drops-384;
            }

            //set values
            oz_box.setText(Double.toString(ounces));
            drop_box.setText(Double.toString(drops));

        }

    }

    //ConvertTo5Gallon
    //Converts one colorant formula to 5-gallon size
    // - Receives the ID of an EditText for ounces and for drops
    protected void convertTo5Gallon(EditText oz_box, EditText drop_box) {
        //check to make sure both boxes are not empty
        if (!oz_box.getText().toString().isEmpty() || !drop_box.getText().toString().isEmpty()) {
            //now both boxes must be checked individually to set an empty box to zero
            if (oz_box.getText().toString().isEmpty())
                oz_box.setText(Double.toString(0));

            if (drop_box.getText().toString().isEmpty())
                drop_box.setText(Double.toString(0));

            //initialize temporary doubles for calculations
            double ounces = Double.parseDouble(oz_box.getText().toString());
            double drops = Double.parseDouble(drop_box.getText().toString());
            double convRate;

            switch (CONVERT_FROM_SETTING) {
                case 8:
                    convRate = 80; //Sample
                    break;
                case 32:
                    convRate = 20;  //Quart
                    return;
                case 128:  //Gallon
                    convRate = 5;
                    break;
                case 640:   //Already a 5-Gallon
                    return;
                default:
                    return;
            }
            drops += ounces * 384;  //convert ounces to drops an add all drops together
            drops = drops * convRate; // convert drops to sample size
            ounces = 0; //reset ounces to zero to prepare for dividing drops into ounces.

            //Extract whole ounces from total drops until drops are < 1 ounce
            //NOTE - 384 drops = 1 ounce
            while (drops >= 384) {
                ounces += 1;
                drops = drops - 384;
            }


            //Added 8/2/17
            //drops = roundHalfDown(drops);  //Rounding function needs testing

            //set values
            oz_box.setText(Double.toString(ounces));
            drop_box.setText(Double.toString(drops));

        }
    }

    /* Potential Rounding Function - needs testing
    //Added 8/2/17
    public static double roundHalfDown(double d) {
        return new BigDecimal(d).setScale(0, RoundingMode.HALF_UP)
                .doubleValue();

    }
    */
}
