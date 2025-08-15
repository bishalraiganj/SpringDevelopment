package com.bishal.receipeappbackend.core.authenticator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Authenticator {


	public static void main(String... args)
	{

		String username = "bishal";
		String password = "3%";
		System.out.println("Authentication of username: "+username+" and password: "+ password +"\n"  + authenticate(username,password));


		Pattern passwordPattern = Pattern.compile("([a-zA-Z!@#$%^&*()])*([0-9])+([a-zA-Z!@#$%^&*()])*([a-zA-Z0-9])*([!@#$%^&*()])+([a-zA-Z0-9])*|([a-zA-Z0-9])*([!@#$%^&*()])+([a-zA-Z0-9])*([a-zA-Z!@#$%^&*()])*([0-9])+([a-zA-Z!@#$%^&*()])*");
		Matcher m = passwordPattern.matcher(password);

		System.out.println("Password Matches: " + m.matches());
	}

	public static boolean authenticate(String username, String password)
	{


		if(checkCredentialFormat(username, password))
		{
			return true;
		}

		return false;




	}


	public static boolean checkCredentialFormat(String username, String password)
	{
		username = username.trim();
		password = password.trim();

		if(username.length()>=5 && username.length() <= 20 && password.length()>=5 && password.length()<=20)
		{
			Pattern usernamePattern = Pattern.compile("([a-zA-Z0-9]{1,20})");

			//[restOfChars]*[AtleastOneCharsUniqueSet1]+[restOfChars]*[restOfChars]*[AtleastOneCharUniqueSet2]+[restOfChar]*|reverseOrder
			//Without using lookarounds
			Pattern passwordPattern = Pattern.compile("([a-zA-Z!@#$%^&*()])*([0-9])+([a-zA-Z!@#$%^&*()])*([a-zA-Z0-9])*([!@#$%^&*()])+([a-zA-Z0-9])*|([a-zA-Z0-9])*([!@#$%^&*()])+([a-zA-Z0-9])*([a-zA-Z!@#$%^&*()])*([0-9])+([a-zA-Z!@#$%^&*()])*");


			Matcher uMatcher = usernamePattern.matcher(username);
			Matcher pMatcher = passwordPattern.matcher(password);

			if(uMatcher.matches() && pMatcher.matches() )
			{
				return true;
			}

		}

		return false;
	}


	public static String retrievePassword(String username)
	{



		return "";
	}


}
