package com.bishal.receipeappbackend.core.parser;

import com.bishal.receipeappbackend.core.model.Recipe;
import com.bishal.receipeappbackend.core.model.Recipe;
import com.bishal.receipeappbackend.core.model.Temporary;

import java.util.Optional;

public class RecipeParser {

	public static Optional<Recipe> parseRecipe(Temporary temporaryInstance)
	{
 		if(temporaryInstance.title!=null && temporaryInstance.description!=null
		&& temporaryInstance.ingredients!=null && temporaryInstance.ingredients.length>0
		&& temporaryInstance.instructions!=null && temporaryInstance.instructions.length>0)
		{

			return Optional.of(new Recipe(temporaryInstance.title,temporaryInstance.description,
					temporaryInstance.ingredients,
					temporaryInstance.instructions));

		}

		 return Optional.empty();
	}


}
