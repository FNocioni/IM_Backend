package com.iit.investmentManager.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping(value = "/register")
    public Object registerUser(@RequestParam Map<String, String> params){

        String firstName = params.get("firstName");
        String lastName = params.get("lastName");
        String email = params.get("email");
        String phone = params.get("phone");
        String password = params.get("password");

        UserEntity newUser = new UserEntity();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setPhone(Integer.parseInt(phone));
        newUser.setPassword(password);

        List<UserEntity> listOfUsers = new ArrayList<>();
        listOfUsers = userRepository.findAll();

        for(UserEntity user : listOfUsers){
            if(Objects.equals(user.getEmail(), newUser.getEmail())){
                return ResponseEntity.badRequest().body("User with that e-mail already exists");
            }
        }

        userRepository.save(newUser);

        return ResponseEntity.ok("New user " + firstName + " " + lastName + "  successfully registered with e-mail: " + email);
    }

    @PostMapping(value = "/login")
    public Object loginUser(@RequestParam Map<String, String> params){

        String email = params.get("email");
        String password = params.get("password");

        List<UserEntity> listOfUsers = new ArrayList<>();
        listOfUsers = userRepository.findAll();

        for(UserEntity user : listOfUsers){
            if(Objects.equals(user.getEmail(), email)){
                if(Objects.equals(user.getPassword(), password)){
                    return ResponseEntity.ok("Successfully logged in as: " + user.getFirstName() + " " + user.getLastName());
                }else{
                    return ResponseEntity.badRequest().body("Incorrect Password");
                }
            }
        }

        return ResponseEntity.badRequest().body("No user registered with that e-mail");

    }

}
