package com.bishal.receipeappbackend.core.model;

public record User(int uid,String username,String password,String name, String email) {

	@Override
	public String toString()
	{
		return "[uid: %d , username: %s , password: %s , name: %s , email: %s]".formatted(uid,username,password,name,email);
	}

}
