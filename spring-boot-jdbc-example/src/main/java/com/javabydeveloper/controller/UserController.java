package com.javabydeveloper.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.javabydeveloper.model.User;
import com.javabydeveloper.template.UserJdbcTemplate;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {


	@Autowired
	private UserJdbcTemplate userRepository;

	public UserController() {
		super();
	}

	// Read - get all

	@GetMapping
	public List<User> getAllUsers() {
		return (List<User>) userRepository.findAll();
	}

	// Read - find one

	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable(value = "id") final Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new NoSuchElementException("User not availbele for Id :" + userId));

		return ResponseEntity.ok().body(user);
	}

	// Read - by username

	@GetMapping("/name/{username}")
	public ResponseEntity<User> getUserByName(@PathVariable(value = "username") final String name) {

		User user = userRepository.findByUserName(name)
				.orElseThrow(() -> new NoSuchElementException("User not availbele for Id :" + name));

		return ResponseEntity.ok().body(user);
	}

	// Write - create

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void createUser(@Valid @RequestBody User user) {

		System.out.println(user); // Just to inspect values for demo
		userRepository.save(user);
	}

	// Write - update

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void updateUser(@PathVariable("id") final long id, @RequestBody final User user) {
		User findUser = userRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("User not availbele for Id :" + id));
		userRepository.save(user);
	}

	// Write - delete

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable final long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("User not availbele for Id :" + id));
		userRepository.delete(user);
	}

	// Read - by sorted and paginated
	@GetMapping(params = { "page", "size", "sortBy" })
	public List<User> findAllBySortAndPage(@RequestParam("page") final int page, @RequestParam("size") final int size,
			@RequestParam("sortBy") final String sortBy, @RequestParam("sortOrder") final String sortOrder) {

		PageRequest pageable = PageRequest.of(page, size, Direction.fromString(sortOrder), sortBy);

		Page<User> result = userRepository.findAll(pageable);

		if (!result.isEmpty())
			return result.getContent();
		else
			return new ArrayList<User>();
	}

	// Read - by only paginated
	@GetMapping(params = { "page", "size" })
	public List<User> findAllByPage(@RequestParam("page") final int page, @RequestParam("size") final int size) {

		PageRequest pageable = PageRequest.of(page, size);

		Page<User> result = userRepository.findAll(pageable);

		if (!result.isEmpty())
			return result.getContent();
		else
			return new ArrayList<User>();
	}

	// Read - by only sorted
	@GetMapping(params = { "sortBy" })
	public List<User> findAllBySort(@RequestParam("sortBy") final String sortBy,
			@RequestParam("sortOrder") final String sortOrder) {

		Sort sort = Sort.by(Direction.fromString(sortOrder), sortBy);

		return (List<User>) userRepository.findAll(sort);

	}

}
