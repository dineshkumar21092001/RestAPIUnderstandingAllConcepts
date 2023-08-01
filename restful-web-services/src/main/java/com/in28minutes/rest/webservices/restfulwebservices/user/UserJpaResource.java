package com.in28minutes.rest.webservices.restfulwebservices.user;

//org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn(Class<T>, Object...)
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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

import com.in28minutes.rest.webservices.restfulwebservices.jpa.PostRepository;
import com.in28minutes.rest.webservices.restfulwebservices.jpa.UserRepository;

import jakarta.validation.Valid;

@RestController
public class UserJpaResource {
	private UserDaoService service;
	private UserRepository userrepository;
	private PostRepository postRepository;

	public UserJpaResource(UserDaoService service, UserRepository userrepository,PostRepository postRepository) {
		this.service = service;
		this.userrepository = userrepository;
		this.postRepository=postRepository;
	}

	// GET /users
	@GetMapping("/jpa/users")
	public List<User> retriveAllUsers() {
		return userrepository.findAll();
	}

	// GET /users/{id}
	// Hateoas => so use EntityModel , WebMvcLinkBuilder
	//
	// http://localhost:8080/users
	//
	@GetMapping("/jpa/users/{id}")
	public EntityModel<User> retriveUsers(@PathVariable int id) {
		Optional<User> user = userrepository.findById(id);// use Optional class
		if (user.isEmpty()) {
			throw new UserNotFoundException("id:" + id);
		}
		EntityModel<User> entityModel = EntityModel.of(user.get());
		// return user;
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retriveAllUsers());
		entityModel.add(link.withRel("all-users"));
		return entityModel;
	}

	// DELETE /users/{id}
	@DeleteMapping("/jpa/users/{id}")
	public void deleteUsers(@PathVariable int id) {
		userrepository.deleteById(id);

	}

	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> retrivePostsForUser(@PathVariable int id) {
		Optional<User> user = userrepository.findById(id);// use Optional class
		if (user.isEmpty()) {
			throw new UserNotFoundException("id:" + id);
		}
		return user.get().getPosts();
		
	}

	// POST /users
	@PostMapping("/jpa/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User savedUser = userrepository.save(user);
		// /user/4 => /users /{id}, user.getId()
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();
		// location- /user/4
		return ResponseEntity.created(location).build();
	}
	
	@PostMapping("/jpa/users/{id}/posts")
	public ResponseEntity<Object> createPostsForUser(@PathVariable int id,@Valid @RequestBody Post post) {
		Optional<User> user = userrepository.findById(id);// use Optional class
		if (user.isEmpty()) {
			throw new UserNotFoundException("id:" + id);
		}
		post.setUser(user.get());
		Post savedPost=postRepository.save(post);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedPost.getId())
				.toUri();
		return ResponseEntity.created(location).build();	 
		
	}
}
