package com.bishal.receipeappbackend.core.model;

public class Temporary {

	public String title;
	public String description;
	public String[] ingredients;
	public String[] instructions;

	public Temporary(String title,String description,String[] ingredients,String[] instructions)
	{
		this.title=title;
		this.description=description;
		this.ingredients=ingredients;
		this.instructions=instructions;
	}


}
