package com.bishal.receipeappbackend.core.authenticator;

import com.bishal.receipeappbackend.core.dbhandler.DbHandler;
import com.bishal.receipeappbackend.core.model.User;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Authenticator {


	public static void main(String... args)
	{

//		String username = "bishal";
//		String password = "sdx3^^5555&";
//		System.out.println("Authentication of username: "+username+" and password: "+ password +"\n"  + authenticate(username,password));
//
//
//		Pattern passwordPattern = Pattern.compile("([a-zA-Z!@#$%^&*()])*([0-9])+([a-zA-Z!@#$%^&*()])*([a-zA-Z0-9])*([!@#$%^&*()])+([a-zA-Z0-9])*|([a-zA-Z0-9])*([!@#$%^&*()])+([a-zA-Z0-9])*([a-zA-Z!@#$%^&*()])*([0-9])+([a-zA-Z!@#$%^&*()])*");
//		Matcher m = passwordPattern.matcher(password);
//
//		System.out.println("Password Matches: " + m.matches());


		System.out.println(authenticate("bishaladhikary","bishal@12"));
		System.out.println(authenticate("akshaykumar","iama"));

	}

	public static boolean authenticate(String username, String password)
	{


		if(checkCredentialFormat(username, password))
		{
			Optional<User> user = retrieveUserCreds(username,password);
			if(user.isPresent())
			{
				return true;
			}
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


	public static Optional<User> retrieveUserCreds(String username,String password)
	{

		Optional<User> user = DbHandler.runQuery(username,password,DbHandler.fetchUserFunction);
		return user;


	}


}
