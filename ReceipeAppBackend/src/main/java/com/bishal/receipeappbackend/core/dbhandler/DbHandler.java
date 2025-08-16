package com.bishal.receipeappbackend.core.dbhandler;

import com.bishal.receipeappbackend.core.authenticator.Authenticator;
import com.bishal.receipeappbackend.core.model.User;

import javax.swing.*;
import java.sql.*;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class DbHandler {


	public static void main(String... args)
	{
		System.out.println(retrieveUserData("bishaladhikary","iamadeveloper").get());

		System.out.println("-".repeat(50));

		System.out.println(runQuery("akshaykumar","iamanactor",fetchUserFunction).get());




	}


	static BiFunction<Connection,String,Optional<User>> fetchUserFunction = (connection,up)->{

		final String sqlQuery = "SELECT * FROM userbase WHERE username = ? AND password = ?";

		try {
			try (
					PreparedStatement ps = connection.prepareStatement(sqlQuery);

			) {

				ps.setString(1, up.split(",")[0]);
				ps.setString(2, up.split(",")[1]);

				ResultSet rs = 	ps.executeQuery();
				if(rs.next())
				{
					return Optional.of(new User(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)));
				}
				else
				{
					return Optional.empty();
				}
			}
		}catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	};

	static BiFunction<Connection,String,Optional<String>> infoFunct = (connection,up)->{

		final String sqlQuery = "SELECT * FROM userbase WHERE username = ? AND password = ?";


		try{
			try(
					PreparedStatement ps = connection.prepareStatement(sqlQuery);
					)
			{
				ps.setString(1,up.split(",")[0]);
				ps.setString(2,up.split(",")[1]);

				ResultSet rs = ps.executeQuery();
				if(rs.next())
				{
					return Optional.of((rs.getString(4) + rs.getString(5)));
				}
				else{
					return Optional.of("User not found :-0 ");
				}

			}

		}catch(SQLException e)
		{
			throw new RuntimeException(e);
		}

	};








	public static Optional<Connection> getConnection(String pass)
	{
		if(pass.equals("bishal12345"))
		{
			try {
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/recipeapp", System.getenv("MYSQL_USER"),
						System.getenv("MYSQL_PASS"));
				System.out.println("Connected to DB :-) ");
				return Optional.of(conn);
			}catch(SQLException e)
			{
				System.out.println("Failed to connect to DB ;-o ");
				throw new RuntimeException(e);
			}
		}

		return Optional.empty();

	}





	public static Optional<User> runQuery(String username,String password,BiFunction<Connection,String,Optional<User>> biFunct)
	{

		final String up = "%s,%s".formatted(username,password);
		try(Connection conn = getConnection("bishal12345").get())
		{

		return	biFunct.apply(conn,up);

		}
		catch(SQLException e)
		{
			throw new RuntimeException(e);
		}




	}



	//InEfficient and unsafe and very complex logic with functional programming
	public static Optional<User> connectAndActDb(String pass, BiFunction<Connection,Statement,User> biFunctionDbActions)
	{


		if(pass.equals("bishal12345"))
		{
			try {
				try (Connection connection = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/recipeapp",
						System.getenv("MYSQL_USER"),
						System.getenv("MYSQL_PASS"));
					 Statement statement = connection.createStatement())
				{
					JOptionPane.showMessageDialog(null,"Connection Successful to "+connection);
					return Optional.of(biFunctionDbActions.apply(connection,statement));
				}
			}catch(SQLException e)
			{
				System.out.println("Database Connection Failed : ;-( ");
				throw new RuntimeException(e);
			}
		}

		return Optional.empty();
	}




	public static Optional<User> retrieveUserData(String username,String password)
	{

			BiFunction<Connection, Statement,User> biFunctionUserRetriever = (connection, statement) -> {

				String sqlQuery="SELECT * FROM userbase WHERE username='%s' AND password='%s'".formatted(username.trim(),password.trim());
				try {

					ResultSet userRs = statement.executeQuery(sqlQuery);
					if(userRs.next() )
					{
					User	user=new User(userRs.getInt(1),userRs.getString(2),
								userRs.getString(3),userRs.getString(4),userRs.getString(5));

					return user;
					}

				}
				catch(SQLException e)
				{
					System.out.println("user info retrieval query failed ");
					throw new RuntimeException(e);
				}
				return null;

			};

			return connectAndActDb("bishal12345",biFunctionUserRetriever);


	}




}
