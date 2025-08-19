package com.bishal.receipeappbackend.core.model;

import java.util.Arrays;

public record Recipe(String title, String description, String[] ingredients, String[] instructions) {



	@Override
	public String toString()
	{


		return "[title= %s | description= %s | ingredients= %s | instructions= %s]".formatted(title,description, Arrays.toString(ingredients),Arrays.toString(instructions));
	}


}
