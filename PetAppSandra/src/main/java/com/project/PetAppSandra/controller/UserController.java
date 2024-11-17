package com.project.PetAppSandra.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController { //CLASS for the POST I'm using in my login.js and storage the new information from a user

	//JdbcTemplate (uses method update to consult SQL and storage my new data)
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@PostMapping(value = "/register", consumes = "application/json", produces = "application/json")  //to accept JSON, endpoint that im using, the server can get http requests
	public ResponseEntity<String> registerUser(@RequestBody Map<String, String> userData) {
	    String account = userData.get("account");
	    String fullname = userData.get("fullname");
	    String birthdate = userData.get("birthdate");
	    String email = userData.get("email");
	    String phone = userData.get("phone");
	    String address = userData.get("address");
	    String username = userData.get("username");
	    String password = userData.get("password");

	    try {
	        String sql = "INSERT INTO users (account, fullname, birthdate, email, phone, address, username, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	        jdbcTemplate.update(sql, account, fullname, birthdate, email, phone, address, username, password);

	        return ResponseEntity.ok("User registered successfully");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user" + e.getMessage());
	    }
	}
}