package com.biermacht.brews.frontend;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.IngredientActivities.AddEditActivity;
import com.biermacht.brews.frontend.adapters.SpinnerAdapter;
import com.biermacht.brews.recipe.BeerStyle;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.recipe.MashProfile;
import com.biermacht.brews.utils.Callbacks.BooleanCallback;
import com.biermacht.brews.utils.Callbacks.Callback;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;
import com.biermacht.brews.utils.Units;
import com.biermacht.brews.utils.Utils;
import com.biermacht.brews.frontend.adapters.*;
import android.widget.*;

public class AddRecipeActivity extends AddEditActivity {

    // Rows to be displayed
    public View efficiencyView;
    public View batchSizeView;
    public View boilSizeView;
    public Spinner styleSpinner;
    public Spinner typeSpinner;
    public Spinner profileSpinner;

    // Titles
    public TextView efficiencyViewTitle;
    public TextView batchSizeViewTitle;
    public TextView boilSizeViewTitle;

    // Contents
    public TextView efficiencyViewText;
    public TextView batchSizeViewText;
    public TextView boilSizeViewText;

    // Data storage declarations
    public BeerStyle style;
    public MashProfile profile;
    public String type;
    public double efficiency;

    // Spinner array declarations
    public ArrayList<BeerStyle> styleArray;
    public ArrayList<String> typeArray;
    public ArrayList<MashProfile> profileArray;

    // Callbacks
    public BooleanCallback boilVolumeCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Disable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.GONE);

        // Initialize each of the rows to be displayed
        mainView = (ViewGroup) findViewById(R.id.main_layout);
        efficiencyView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        batchSizeView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);;
        boilSizeView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);

        // Set onClickListeners for edit text views
        efficiencyView.setOnClickListener(onClickListener);
        batchSizeView.setOnClickListener(onClickListener);
        boilSizeView.setOnClickListener(onClickListener);

        /************************************************************************
         ************* Add views to main view  **********************************
         *************************************************************************/
        mainView.removeAllViews();
        mainView.addView(nameView);
        mainView.addView(styleSpinner);
        mainView.addView(typeSpinner);
        mainView.addView(profileSpinner);
        mainView.addView(timeView);
        mainView.addView(efficiencyView);
        mainView.addView(batchSizeView);
        mainView.addView(boilSizeView);

        /************************************************************************
         ************* Get titles, set values   **********************************
         *************************************************************************/
        nameViewTitle.setText("Recipe Name");
        timeViewTitle.setText("Boil Time (mins)");

        efficiencyViewTitle = (TextView) efficiencyView.findViewById(R.id.title);
        efficiencyViewTitle.setText("Predicted Efficiency");

        batchSizeViewTitle = (TextView) batchSizeView.findViewById(R.id.title);
        batchSizeViewTitle.setText("Batch Size (" + Units.getVolumeUnits() + ")");

        boilSizeViewTitle = (TextView) boilSizeView.findViewById(R.id.title);
        boilSizeViewTitle.setText("Boil Size (" + Units.getVolumeUnits() + ")");

        /************************************************************************
         ************* Get content views, set values   **************************
         *************************************************************************/
        nameViewText.setText(mRecipe.getRecipeName());
        timeViewText.setText(String.format("%d", mRecipe.getBoilTime()));

        efficiencyViewText = (TextView) efficiencyView.findViewById(R.id.text);
        efficiencyViewText.setText(String.format("%2.2f", mRecipe.getEfficiency()));

        batchSizeViewText = (TextView) batchSizeView.findViewById(R.id.text);
        batchSizeViewText.setText(String.format("%2.2f", mRecipe.getDisplayBatchSize()));

        boilSizeViewText = (TextView) boilSizeView.findViewById(R.id.text);
        boilSizeViewText.setText(String.format("%2.2f", mRecipe.getDisplayBoilSize()));
    }

    @Override
    public void onMissedClick(View v)
    {
        AlertDialog alert;
        if (v.equals(efficiencyView))
            alert = alertBuilder.editTextFloatAlert(efficiencyViewText, efficiencyViewTitle).create();
        else if (v.equals(batchSizeView))
            alert = alertBuilder.editTextFloatAlert(batchSizeViewText, batchSizeViewTitle).create();
        else if (v.equals(boilSizeView))
            alert = alertBuilder.editTextFloatCheckBoxAlert(boilSizeViewText, boilSizeViewTitle, mRecipe.getCalculateBoilVolume(), boilVolumeCallback).create();
        else
            return; // In case its none of those views...

        // Force keyboard open and show popup
        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alert.show();
    }

    @Override
    public void getValuesFromIntent()
    {
        super.getValuesFromIntent();
    }

    @Override
    public void createCallback()
    {
        // Default callback, called when alertBuilders are finished.
        // Allows us to update fields that are dependent on other fields.
        callback = new Callback()
        {
            @Override
            public void call()
            {
                try
                {
                    // Get new values
                	Log.d("AddRecipeActivity", "Calling callback.");
                    acquireValues();
                }
                catch (Exception e)
                {
                    Log.d("AddRecipeActivity", "Exception in callback from alert dialog");
                    e.printStackTrace();
                }
                // Update fields here
                boilSizeViewText.setText(String.format("%2.2f", mRecipe.getDisplayBoilSize()));
            }
        };

        // Callback for when boilVolume is updated.  We need to
        // check if the option to auto-calc boil volume has changed via user
        // selection.
        boilVolumeCallback = new BooleanCallback()
        {
            @Override
            public void call(boolean b)
            {
            	Log.d("AddRecipeActivity", "Boil volume checkbox pressed.");
                mRecipe.setCalculateBoilVolume(b);
                boilSizeViewText.setText(String.format("%2.2f", mRecipe.getDisplayBoilSize()));
            }
        };
    }

    @Override
    public void onRecipeNotFound()
    {
        Log.d("AddRecipeActivity", "onRecipeNotFound because we're creating one!");
        // Set values
        mRecipe = Constants.NEW_RECIPE;
        style = mRecipe.getStyle();
        profile = mRecipe.getMashProfile();
        type = mRecipe.getType();
        efficiency = mRecipe.getEfficiency();
    }

    @Override
    public void createSpinner()
    {
        // Set up style spinner here
        styleSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);
        BeerStyleSpinnerAdapter styleAdapter = new BeerStyleSpinnerAdapter(this, styleArray);
        styleAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        styleSpinner.setAdapter(styleAdapter);

        // Set up mash profile spinner
        profileSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);
        MashProfileSpinnerAdapter profileAdapter = new MashProfileSpinnerAdapter(this, profileArray);
        profileAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        profileSpinner.setAdapter(profileAdapter);

        // Set up brew type spinner
        typeSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);
        SpinnerAdapter typeAdapter = new SpinnerAdapter(this, typeArray, "Recipe Type");
        typeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        typeSpinner.setAdapter(typeAdapter);
    }

    @Override
    public void setInitialSpinnerSelection()
    {
        typeSpinner.setSelection(typeArray.indexOf(mRecipe.getType()));
        profileSpinner.setSelection(profileArray.indexOf(mRecipe.getMashProfile()));
        styleSpinner.setSelection(styleArray.indexOf(mRecipe.getStyle()));
    }

    @Override
    public void configureSpinnerListener()
    {
        // Handle beer style selector here
        styleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                style = styleArray.get(position);
                callback.call();
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {
            }

        });

        // Handle mash profile selector here
        profileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                profile = profileArray.get(position);
                Log.d("AddRecipeActivity", "Got profile: " + profile.getName());
                callback.call();
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {
            }

        });

        // Handle type selector here
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                type = typeArray.get(position);
                callback.call();

                if(type.equals(Recipe.EXTRACT))
                {
                    profileSpinner.setVisibility(View.GONE);
                    efficiencyView.setVisibility(View.GONE);
                }
                else
                {
                    profileSpinner.setVisibility(View.VISIBLE);
                    efficiencyView.setVisibility(View.VISIBLE);
                }
            }

            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
    }

    @Override
    public void getList()
    {
        // Get array of styles and mash profiles
        styleArray = MainActivity.ingredientHandler.getStylesList();
        profileArray = MainActivity.ingredientHandler.getMashProfileList();
        typeArray = new ArrayList<String>();
        typeArray.add(Recipe.EXTRACT);
        typeArray.add(Recipe.PARTIAL_MASH);
        typeArray.add(Recipe.ALL_GRAIN);

        // If it doesn't contain the current recipes style / profile,
        // then it is custom and we add it to the list.
        if(!styleArray.contains(mRecipe.getStyle()))
            styleArray.add(mRecipe.getStyle());
        if(!profileArray.contains(mRecipe.getMashProfile()))
            profileArray.add(mRecipe.getMashProfile());
    }

    @Override
    public void onCancelPressed()
    {
        setResult(Constants.RESULT_CANCELED, new Intent());
        finish();
    }

    @Override
    public void onDeletePressed()
    {

    }

    @Override
    public void acquireValues() throws Exception
    {
        super.acquireValues();

        efficiency = Double.parseDouble(efficiencyViewText.getText().toString());
        double batchSize = Double.parseDouble(batchSizeViewText.getText().toString());
        double boilSize = Double.parseDouble(boilSizeViewText.getText().toString());
        String description = "No Description Provided";

        mRecipe.setRecipeName(name);
        mRecipe.setVersion(Utils.getXmlVersion());
        mRecipe.setType(type);
        mRecipe.setStyle(style);
        mRecipe.setMashProfile(profile);
        mRecipe.setBrewer("Biermacht Brews"); // TODO
        mRecipe.setDisplayBatchSize(batchSize);
        mRecipe.setDisplayBoilSize(boilSize);
        mRecipe.setBoilTime(time);
        mRecipe.setEfficiency(efficiency);
        mRecipe.setBatchTime(1);
        mRecipe.setNotes(description);
    }

    @Override
    public void onFinished()
    {        
        // Add recipe to database and open up the recipe activity.
        Intent intent = new Intent(AddRecipeActivity.this, DisplayRecipeActivity.class);
        intent.putExtra(Constants.KEY_RECIPE, Database.createRecipeFromExisting(mRecipe));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}