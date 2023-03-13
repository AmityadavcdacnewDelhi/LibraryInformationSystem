package org.fi.exception;

public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException(Long id) {
		super("could not find the user " + id);

	}

}
