package com.bishal.receipeappbackend.core.dbhandler;

import com.bishal.receipeappbackend.core.model.User;

import javax.swing.*;
import java.sql.*;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class DbHandler {




	public static void main(String... args)
	{
		System.out.println(retrieveUserData("bishaladhikary","iamadeveloper").get());


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
