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
//		System.out.println(retrieveUserData("bishaladhikary","iamadeveloper").get());

		System.out.println("-".repeat(50));

		System.out.println(runQuery("akshaykumar","akshay@123",fetchUserFunction).get());

		System.out.println(registerUser("Quaresma","q1234!","Ricardo Quaresma","ricardo@gmail.com",registerUserFunction));


		System.out.println(registerUser("Jasika","j1234!","Jasika Barman","jasika@gmail.com",registerUserFunction));

	}



		public static BiFunction<Connection,String,Boolean> registerUserFunction = (conn,upne)->{

			final String sqlQuery = "INSERT INTO userbase (username,password,name,email) VALUES (?,?,?,?)";

			final String username = upne.split(",")[0];
			final String password = upne.split(",")[1];
			final String name = upne.split(",")[2];
			final String email = upne.split(",")[3];

			if(Authenticator.checkCredentialFormat(username,password) && !Authenticator.authenticate(username,password)) {
				try {
					PreparedStatement pst = conn.prepareStatement(sqlQuery);
					pst.setString(1,username);
					pst.setString(2, password);
					pst.setString(3,name);
					pst.setString(4,email);
					int rowsAffected = pst.executeUpdate();
					if(rowsAffected == 1)
					{
						System.out.printf("User Registerd : %s Successfully  ",username);
						return true;
					}
					else if(rowsAffected > 1)
					{
						System.out.printf("Something Went Wrong ! DB , %d rows affected",rowsAffected);
						return false;
					}
					else{
						return false;
					}
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
			}
			else{
				System.out.println(" User : %s exists already".formatted(username));
				return false;
			}

};

	public static BiFunction<Connection,String,Optional<User>> fetchUserFunction = (connection,up)->{

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

	public static BiFunction<Connection,String,Optional<String>> infoFunct = (connection,up)->{

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
//					JOptionPane.showMessageDialog(null,"Connection Successful to "+connection);
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



	public static boolean registerUser(String username,String password,String name,String email,BiFunction<Connection,String,Boolean> biFunct)
	{
		String upne = username+","+password+","+name+","+email;

		try{
		try(Connection connection = getConnection("bishal12345").get())
		{
		return	biFunct.apply(connection,upne);
		}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}

	}




}
