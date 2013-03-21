package com.biermacht.brews.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.recipe.*;
import java.util.*;
import com.biermacht.brews.ingredient.*;

public class RecipeHandler extends DefaultHandler {

	// Types of things
	static int RECIPES = 1;
	static int RECIPE = 2;
	static int HOPS = 3;
	static int FERMENTABLES = 4;
	static int HOP = 5;
	static int FERMENTABLE = 6;
	static int YEAST = 7;
	static int YEASTS = 8;
	
	// Hold the current elements
    boolean currentElement = false;
    String currentValue = null;

	// Lists to store all the things
	ArrayList<Recipe> list = null;
	ArrayList<Fermentable> fermList = null;
	ArrayList<Hop> hopList = null;	
	ArrayList<Yeast> yeastList = null;

	// Objects for each type of thing
	Recipe r = null;
    Fermentable f = null;
	Hop h = null;
	Yeast y = null;
	
	
	// How we know what thing we're looking at
	int thingType = 0;

	/**
	* This returns a list containing all of the recipes parsed.
	*/
    public ArrayList<Recipe> getRecipes() {
        return list;
    }

	/**
	* This gets called whenever we encounter a new start element.  In this function
	* we create the new object to be populated and set the type of what we are looking at
	* so we can properly parse the following tags
	*/
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        currentElement = true;

		// We encounter a new recipe
        if (qName.equalsIgnoreCase("RECIPES"))
        {
			thingType = RECIPES;
            list = new ArrayList<Recipe>();
        }
		
		// We encounter a new recipe
		if (qName.equalsIgnoreCase("RECIPE"))
		{
			thingType = RECIPE;
			r = new Recipe("");
		}
		
		// We encounter a new fermentables list
		if (qName.equalsIgnoreCase("FERMENTABLES"))
		{
			thingType = FERMENTABLES;
			fermList = new ArrayList<Fermentable>();
		}
		
		// We encounter a new hops list
		if (qName.equalsIgnoreCase("HOPS"))
		{
			thingType = HOPS;
			hopList = new ArrayList<Hop>();
		}
		
		// We encounter a new hop
		if (qName.equalsIgnoreCase("HOP"))
		{
			thingType = HOP;
			h = new Hop("");
		}
		// We encounter a new fermentable
		if (qName.equalsIgnoreCase("FERMENTABLE"))
		{
			thingType = FERMENTABLE;
			f = new Fermentable("");
		}
		// We encounter a new yeast
		if (qName.equalsIgnoreCase("YEASTS"))
		{
			thingType = YEASTS;
			yeastList = new ArrayList<Yeast>();
		}

		// We encounter a new yeast
		if (qName.equalsIgnoreCase("YEAST"))
		{
			thingType = YEAST;
			y = new Yeast("");
		}
    }

	/**
	* This gets called when we run into an end element.  The value of the element is stored in
	* the variable 'currentValue' and is assigned appropriately based on thingType.
	*/
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        currentElement = false;

		if (qName.equalsIgnoreCase("RECIPE"))
		// We've finished a new recipe
		{
			Log.e("RecipeHandler", "New recipe added: " + r.getRecipeName());
			list.add(r);
			thingType = 0;
			return;
		}
		else if (qName.equalsIgnoreCase("HOPS"))
		// We have finished a list of hops.
		{
			thingType = 0;
			return;
		}
		else if (qName.equalsIgnoreCase("FERMENTABLES"))
		// We have finished a list of fermentables
		{
			thingType = 0;
			return;
		}
		else if (qName.equalsIgnoreCase("YEASTS"))
		// We have finished a list of yeasts
		{
			thingType = 0;
			return;
		}
		else if (qName.equalsIgnoreCase("FERMENTABLE"))
		// Finished a fermentable.  Add it to recipe and fermentables list.
		{
			thingType = 0;
			r.addIngredient(f);
			fermList.add(f);
			return;
		}
		else if (qName.equalsIgnoreCase("HOP"))
		// Finished a hop.  Add to recipe and list
		{
			thingType = 0;
			r.addIngredient(h);
			hopList.add(h);
			return;
		}
		else if (qName.equalsIgnoreCase("YEAST"))
		// Finished a yeast. Add to recipe and list
		{
			thingType = 0;
			r.addIngredient(y);
			yeastList.add(y);
			return;
		}
		
		/************************************************************
		* Handle individual types of ingredients / things below.  We check
		* the "thingType" and base our actions accordingly
		************************************************************/
		if (thingType == RECIPE)
		{
			if (qName.equalsIgnoreCase("NAME"))
			{
				r.setRecipeName(currentValue);
			}

			else if (qName.equalsIgnoreCase("VERSION"))
			{
				r.setVersion(Integer.parseInt(currentValue));
			}

			else if (qName.equalsIgnoreCase("TYPE"))
			{
				String type = "NULL";

				if (currentValue.equalsIgnoreCase(Recipe.EXTRACT))
					type = Recipe.EXTRACT;
				if (currentValue.contains("Extract"))
					type = Recipe.EXTRACT;
				if (currentValue.equalsIgnoreCase(Recipe.ALL_GRAIN))
					type = Recipe.ALL_GRAIN;
				if (currentValue.equalsIgnoreCase(Recipe.PARTIAL_MASH))
					type = Recipe.PARTIAL_MASH;
					
				r.setType(type);
			}
			
			else if (qName.equalsIgnoreCase("BREWER"))
			{
				r.setBrewer(currentValue);
			}
			
			else if (qName.equalsIgnoreCase("ASST_BREWER"))
			{
				// TODO
			}
			
			else if (qName.equalsIgnoreCase("BATCH_SIZE"))
			{
				r.setBatchSize(Float.parseFloat(currentValue));
			}
			
			else if (qName.equalsIgnoreCase("BOIL_SIZE"))
			{
				r.setBoilSize(Float.parseFloat(currentValue));
			}
			
			else if (qName.equalsIgnoreCase("EFFICIENCY"))
			{
				r.setEfficiency(Float.parseFloat(currentValue));
			}
		}
		
		if (thingType == FERMENTABLE)
		// We are looking at a fermentable
		// Do all of the fermentable things below
		// woo.
		{
			if (qName.equalsIgnoreCase("NAME"))
			{
				f.setName(currentValue);
			}

			else if (qName.equalsIgnoreCase("VERSION"))
			{
				// TODO: Set version!
			}

			else if (qName.equalsIgnoreCase("TYPE"))
			{
				String type = "NULL";

				if (currentValue.equalsIgnoreCase(Fermentable.ADJUNCT))
					type = Fermentable.ADJUNCT;
				if (currentValue.equalsIgnoreCase(Fermentable.EXTRACT))
					type = Fermentable.EXTRACT;
				if (currentValue.contains("Extract"))
					type = Fermentable.EXTRACT;
				if (currentValue.equalsIgnoreCase(Fermentable.GRAIN))
					type = Fermentable.GRAIN;
				if (currentValue.equalsIgnoreCase(Fermentable.SUGAR))
					type = Fermentable.SUGAR;

				f.setFermentableType(type);
			}

			else if (qName.equalsIgnoreCase("AMOUNT"))
			{
				double amt = Double.parseDouble(currentValue);
				f.setAmount(amt);
			}

			else if (qName.equalsIgnoreCase("YIELD"))
			{
				double yield = Double.parseDouble(currentValue);
				f.setYield(yield);
			}

			else if (qName.equalsIgnoreCase("COLOR"))
			{
				double color = Double.parseDouble(currentValue);
				f.setLovibondColor(color);
			}

			else if (qName.equalsIgnoreCase("ADD_AFTER_BOIL"))
			{
				boolean aab = (currentValue.equalsIgnoreCase("FALSE")) ? false : true;
				f.setAddAfterBoil(aab);
			}

			else if (qName.equalsIgnoreCase("ORIGIN"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("SUPPLIER"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("NOTES"))
			{
				f.setShortDescription(currentValue);
			}

			else if (qName.equalsIgnoreCase("COARSE_FINE_DIFF"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("MOISTURE"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("DIASTATIC_POWER"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("PROTEIN"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("MAX_IN_BATCH"))
			{
				if (currentElement != false)
				{
					double maxInBatch = Double.parseDouble(currentValue);
					f.setMaxInBatch(maxInBatch);
				}
			}

			else if (qName.equalsIgnoreCase("RECOMMEND_MASH"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("IBU_GAL_PER_LB"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("DISPLAY_AMOUNT"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("INVENTORY"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("POTENTIAL"))
			{
				// TODO: FUCK THIS SHIT
			}

			else if (qName.equalsIgnoreCase("DISPLAY_COLOR"))
			{
				// TODO: Add support for this field
			}
		}
		
		else if (thingType == HOP)
		// We are looking at a hop
		// Set all the hop things here
		// woo.
		{
			if (qName.equalsIgnoreCase("NAME"))
			{
				h.setName(currentValue);
			}

			else if (qName.equalsIgnoreCase("VERSION"))
			{
				// TODO: Set version!
			}

			else if (qName.equalsIgnoreCase("ORIGIN"))
			{
				h.setOrigin(currentValue);
			}

			else if (qName.equalsIgnoreCase("ALPHA"))
			{
				h.setAlphaAcidContent(Double.parseDouble(currentValue));
			}

			else if (qName.equalsIgnoreCase("AMOUNT"))
			{
				h.setAmount(Double.parseDouble(currentValue));
			}

			else if (qName.equalsIgnoreCase("USE"))
			{
				String use = "";

				if (currentValue.equalsIgnoreCase(Hop.USE_AROMA))
					use = Hop.USE_AROMA;
				if (currentValue.equalsIgnoreCase(Hop.USE_BOIL));
        		use = Hop.USE_BOIL;
				if (currentValue.equalsIgnoreCase(Hop.USE_DRY_HOP))
					use = Hop.USE_DRY_HOP;
				if (currentValue.equalsIgnoreCase(Hop.USE_MASH))
					use = Hop.USE_MASH;
				if (currentValue.equalsIgnoreCase(Hop.USE_FIRST_WORT))
					use = Hop.USE_FIRST_WORT;

				h.setUse(use);
			}

			else if (qName.equalsIgnoreCase("TIME"))
			{
				h.setTime((int) Double.parseDouble(currentValue));
			}

			else if (qName.equalsIgnoreCase("NOTES"))
			{
				h.setDescription(currentValue);
			}

			else if (qName.equalsIgnoreCase("TYPE"))
			{
				String type = "";

				if (currentValue.equalsIgnoreCase(Hop.TYPE_AROMA))
					type = Hop.TYPE_AROMA;
				if (currentValue.equalsIgnoreCase(Hop.TYPE_BITTERING))
					type = Hop.TYPE_BITTERING;
				if (currentValue.equalsIgnoreCase(Hop.TYPE_BOTH))
					type = Hop.TYPE_BOTH;

				h.setHopType(type);
			}

			else if (qName.equalsIgnoreCase("FORM"))
			{
				String form = "";

				if (currentValue.equalsIgnoreCase(Hop.FORM_PELLET))
					form = Hop.FORM_PELLET;
				if (currentValue.equalsIgnoreCase(Hop.FORM_WHOLE))
					form = Hop.FORM_WHOLE;
				if (currentValue.equalsIgnoreCase(Hop.FORM_PLUG))
					form = Hop.FORM_PLUG;

				h.setForm(form);
			}
		}
		
		else if (thingType == YEAST)
		// We are looking at a yeast
		// Set the yeast fiels here
		// woo.
		{
			if (qName.equalsIgnoreCase("NAME"))
			{
				y.setName(currentValue);
			}

			else if (qName.equalsIgnoreCase("VERSION"))
			{
				int version = Integer.parseInt(currentValue);
				y.setVersion(version);
			}

			else if (qName.equalsIgnoreCase("TYPE"))
			{
				String type = "Invalid Type";

				if (currentValue.equalsIgnoreCase(Yeast.ALE))
					type = Yeast.ALE;
				if (currentValue.equalsIgnoreCase(Yeast.LAGER))
					type = Yeast.LAGER;
				if (currentValue.equalsIgnoreCase(Yeast.WHEAT))
					type = Yeast.WHEAT;
				if (currentValue.equalsIgnoreCase(Yeast.WINE))
					type = Yeast.WINE;
				if (currentValue.equalsIgnoreCase(Yeast.CHAMPAGNE))
					type = Yeast.CHAMPAGNE;

				y.setType(type);
			}

			else if (qName.equalsIgnoreCase("FORM"))
			{
				String form = "Invalid Form";

				if (currentValue.equalsIgnoreCase(Yeast.CULTURE))
					form = Yeast.CULTURE;
				if (currentValue.equalsIgnoreCase(Yeast.DRY))
					form = Yeast.DRY;
				if (currentValue.equalsIgnoreCase(Yeast.LIQUID))
					form = Yeast.LIQUID;

				y.setForm(form);
			}

			else if (qName.equalsIgnoreCase("AMOUNT"))
			{
				double amt = Double.parseDouble(currentValue);
				y.setAmount(amt);
			}

			else if (qName.equalsIgnoreCase("AMOUNT_IS_WEIGHT"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("LABORATORY"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("PRODUCT_ID"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("MIN_TEMPERATURE"))
			{
				double minTemp = Double.parseDouble(currentValue);
				y.setMinTemp(minTemp);
			}

			else if (qName.equalsIgnoreCase("MAX_TEMPERATURE"))
			{
				double maxTemp = Double.parseDouble(currentValue);
				y.setMaxTemp(maxTemp);
			}

			else if (qName.equalsIgnoreCase("FLOCCULATION"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("ATTENUATION"))
			{
				double attenuation = Double.parseDouble(currentValue);
				y.setAttenuation(attenuation);
			}

			else if (qName.equalsIgnoreCase("NOTES"))
			{
				y.setNotes(currentValue);
			}

			else if (qName.equalsIgnoreCase("BEST_FOR"))
			{
				y.setBestFor(currentValue);
			}

			else if (qName.equalsIgnoreCase("MAX_REUSE"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("TIMES_CULTURED"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("ADD_TO_SECONDARY"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("DISPLAY_AMOUNT"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("DISP_MIN_TEMP"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("DISP_MAX_TEMP"))
			{
				// TODO: Add support for this field
			}

			else if (qName.equalsIgnoreCase("INVENTORY"))
			{
				// TODO: FUCK THIS SHIT
			}

			else if (qName.equalsIgnoreCase("CULTURE_DATE"))
			{
				// TODO: Add support for this field
			}
		}
    }

    @Override
    public void characters(char[] ch, int start, int length)
    throws SAXException {

        if (currentElement) {
            currentValue = new String(ch, start, length);
            currentElement = false;
        }

    }

}