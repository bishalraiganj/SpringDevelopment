package com.bishal.receipeappbackend.core.authenticator;

import java.util.regex.Pattern;

public class Authenticator {


	public static boolean authenticate(String username, String password)
	{






	}


	public static boolean credentialChecker(String username, String password)
	{
		Pattern usernamePattern = Pattern.compile("([a-zA-Z0-9]{1,20})");

		Pattern passwordPattern = Pattern.compile("([a-zA-Z!@#$%^&*()])*([0-9])+([a-zA-Z!@#$%^&*()])*([a-zA-Z0-9])*([!@#$%^&*()])+([a-zA-Z0-9])*|([a-zA-Z0-9])*([!@#$%^&*()])+([a-zA-Z0-9])*([a-zA-Z!@#$%^&*()])*([0-9])+([a-zA-Z!@#$%^&*()])*");


		String p =""


	}


}
