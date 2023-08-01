
package com.in28minutes.rest.webservices.restfulwebservices.helloworld;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

//RestAPI
@RestController
public class HelloWorldController {		
	
	private MessageSource messageSource;
	
	private HelloWorldController(MessageSource messageSource) {
			this.messageSource=messageSource;
	}
	
	//url=>	/hello-world
	//localhost => "Hello World"
	
	@GetMapping(path="/hello-world")
	public String helloWorld() {
		return "Hello World";
	}
	@GetMapping(path="/hello-world-bean")
	public HelloWorldBean helloWorldBean() {
		return new HelloWorldBean("Hello World");
	}
	
	//path Parameters
	// /users/{id}/todos/{id} => /users/2/todos/200
	// /hello-world/path-variable/{name}
	@GetMapping(path="/hello-world/path-variable/{name}")
	public HelloWorldBean helloWorldPathVariable(@PathVariable String name) {
		return new HelloWorldBean(String.format("Hello World, %s",name));
	}
	//Here we use Locale class
	@GetMapping(path="/hello-world-internationalized")
	public String helloWorldInternationalized() {
		Locale locale=LocaleContextHolder.getLocale();
		return messageSource.getMessage("good.morning.message",null,"Default Message",locale);
		//	return "Hello World";
	}
}
