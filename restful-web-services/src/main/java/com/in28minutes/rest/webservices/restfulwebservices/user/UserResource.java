package com.in28minutes.rest.webservices.restfulwebservices.user;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;//org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn(Class<T>, Object...)
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
public class UserResource {
	private UserDaoService service;
	
	public UserResource(UserDaoService service) {
		this.service=service;
	}
	//GET /users
	@GetMapping("/users")
	public List<User> retriveAllUsers(){
		return service.findAll();
	}
	//GET /users/{id}
	//Hateoas  => so use EntityModel , WebMvcLinkBuilder
	//
	//http://localhost:8080/users
	//
	@GetMapping("/users/{id}")
	public EntityModel<User> retriveUsers(@PathVariable int id){
		User user=service.findOne(id);
		if(user==null) {
			throw new UserNotFoundException("id:"+id);
		}
		EntityModel<User> entityModel=EntityModel.of(user);
		//return user;
		WebMvcLinkBuilder link=linkTo(methodOn(this.getClass()).retriveAllUsers());
		entityModel.add(link.withRel("all-users"));
		return entityModel;
	}
	//DELETE /users/{id}
	@DeleteMapping("/users/{id}")
	public void deleteUsers(@PathVariable int id){
		service.deleteById(id);
		
	}
	//POST /users
	@PostMapping("/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User savedUser=service.save(user);
		// /user/4 => /users /{id}, user.getId()
		URI location= ServletUriComponentsBuilder.fromCurrentRequest()
						.path("/{id}")
						.buildAndExpand(savedUser.getId())
						.toUri();
		//location- /user/4
		return ResponseEntity.created(location).build();
	}
	
}
