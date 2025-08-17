package com.bishal.receipeappbackend.core.model;

public class RegRequestDTO {

	private int uid;
	private String username;
	private String password;
	private String name;
	private String email;

	public RegRequestDTO(int uid,String username,String password,String name,String email)
	{
		this.uid=uid;
		this.username=username;
		this.password=password;
		this.name=name;
		this.email=email;
	}


	public int getUid()
	{
		return uid;
	}
	public String getUsername()
	{
		return username;
	}
	public String getPassword()
	{
		return password;
	}
	public String getName()
	{
		return name;
	}
	public String getEmail()
	{
		return email;
	}

}
