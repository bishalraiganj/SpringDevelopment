package com.bishal.receipeappbackend.core.model;

public record Recipe(String title,int uid,String description,String[] ingredients,String[] instructions) {


	public Recipe(String title,String description,String[] ingredients,String[] instructions)
	{
		this(title,0,description,ingredients,instructions);
	}


}
