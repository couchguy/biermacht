package com.biermacht.brews.recipe;

import java.util.ArrayList;

public class MashProfile
{
	// Beer XML 1.0 Required Fields ===================================
	// ================================================================
	private String name;		            // profile name
	private int version;			        // XML Version -- 1
	private double grainTemp;              // Grain temp in C
	private ArrayList<MashStep> mashSteps;  // List of steps
	
	
	// Beer XML 1.0 Optional Fields ===================================
	// ================================================================
	private double tunTemp;		    // TUN Temperature in C
	private double spargeTemp;      // Sparge Temp in C
	private double pH;              // pH of water
	private double tunWeight;       // Weight of TUN in kG
	private double tunSpecificHeat; // Specific heat of TUN
	private Boolean equipAdj;       // Adjust for heating of equip?
	
	// Custom Fields ==================================================
	// ================================================================
	private long id;                  // id for use in database
	private long ownerId;			  // id for parent recipe

	// Static values =================================================
	// ===============================================================
	

    // Basic Constructor	
	public MashProfile() {
		this.setName("Unnamed Mash Profile");
		this.setVersion(1);
		this.setBeerXmlStandardGrainTemp(0);
		this.mashSteps = new ArrayList<MashStep>();
	    this.setBeerXmlStandardTunTemp(0);
		this.setpH(7);
		this.setBeerXmlStandardTunWeight(0);
		this.setBeerXmlStandardTunSpecHeat(0);
		this.setEquipmentAdjust(false);
		this.id = -1;
		this.ownerId = -1;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}

	public void setVersion(int v)
	{
		this.version = v;
	}

	public void setBeerXmlStandardGrainTemp(double temp)
	{
		this.grainTemp = temp;
	}
	
	public double getBeerXmlStandardGrainTemp()
	{
		return this.grainTemp;
	}

	public void setBeerXmlStandardTunTemp(double temp)
	{
		this.tunTemp = temp;
	}
	
	public double getBeerXmlStandardTunTemp()
	{
		return this.tunTemp;
	}

	public void setpH(double pH)
	{
		this.pH = pH;
	}
	
	public double getpH()
	{
		return this.pH;
	}

	public void setBeerXmlStandardTunWeight(double weight)
	{
		this.tunWeight = weight;
	}
	
	public double getBeerXmlStandardTunWeight()
	{
		return this.tunWeight;
	}

	public void setBeerXmlStandardTunSpecHeat(double heat)
	{
		this.tunSpecificHeat = heat;
	}
	
	public double getBeerXmlStandardTunSpecHead()
	{
		return this.tunSpecificHeat;
	}

	public void setEquipmentAdjust(boolean adj)
	{
		this.equipAdj = adj;
	}
	
	public Boolean getEquipmentAdjust()
	{
		return this.equipAdj;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}

	public long getId()
	{
		return this.id;
	}

	public void setOwnerId(long id)
	{
		this.ownerId = id;
	}

	public long getOwnerId()
	{
		return this.ownerId;
	}
}
