package com.in28minutes.rest.webservices.restfulwebservices.exception;

import java.time.LocalDate;
import java.time.LocalDateTime;

/*When we enter unknnown user example "/users/532" it  will give 404 error and
 give some predefined structure which is provided by springBoot , But for restApi it should by 
 in proper structure of error message , so to provide custom error message we preform 
 below ways and we should extend ResponseEntityExceptionHandler class and we should override
 the """public final ResponseEntity<Object> handleException(Exception ex, WebRequest request)
  throws Exception {""" this method to give our own custom exception structure*/
public class ErrorDetails {
	//timestamp
	//message
	//details
	private LocalDateTime timestamp;
	private String message;
	private String details;
	public ErrorDetails(LocalDateTime timestamp, String message, String details) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public String getMessage() {
		return message;
	}
	public String getDetails() {
		return details;
	}	
	
	
}
