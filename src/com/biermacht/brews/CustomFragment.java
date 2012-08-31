package com.biermacht.brews;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.TableLayout;

public class CustomFragment extends Fragment {

	private int resource;
	private Recipe r;
	private boolean isIngredientList;
	
	public CustomFragment(int resource, Recipe r)
	{
		this.resource = resource;
		this.r = r;
		
		// Determine what kind of view this is
		if (resource == R.layout.ingredient_view)
			isIngredientList = true;
		else
			isIngredientList = false;
	}
	
	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		TableLayout tableView = new TableLayout(DisplayRecipeActivity.appContext);
		tableView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		if(isIngredientList)
		{
		  ArrayList<Ingredient> ingredientList = r.getIngredientList();
		  IngredientArrayAdapter ingredientArrayAdapter = new IngredientArrayAdapter(DisplayRecipeActivity.appContext, ingredientList);
		  ListView listView = new ListView(DisplayRecipeActivity.appContext);
		  listView.setAdapter(ingredientArrayAdapter);
		  tableView.addView(listView);
		}
		else
		{
		  ArrayList<Instruction> instructionList = r.getInstructionList();
		  InstructionArrayAdapter instructionArrayAdapter = new InstructionArrayAdapter(DisplayRecipeActivity.appContext, instructionList);
		  ListView listView = new ListView(DisplayRecipeActivity.appContext);
		  listView.setAdapter(instructionArrayAdapter);
		  tableView.addView(listView);
		}

		
		// Remove all views, then add new ones
		container.removeAllViews();
		container.addView(tableView);
		
		return inflater.inflate(resource, container, false);
	}
}
