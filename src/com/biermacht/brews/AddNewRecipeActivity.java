package com.biermacht.brews;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AddNewRecipeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_recipe);
        
        // View holders
        Spinner beerTypeSpinner;
        EditText recipeNameEditText;
        EditText recipeDescriptionEditText;
        
        //Arraylist of beer types
        ArrayList<String> beerTypeArray = Recipe.getBeerTypeList();
        
        // Set up beer type spinner
        beerTypeSpinner = (Spinner) findViewById(R.id.beer_type_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, beerTypeArray);  
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        beerTypeSpinner.setAdapter(adapter);
        beerTypeSpinner.setSelection(0); 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_new_recipe, menu);
        return true;
    }
}