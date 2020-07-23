package com.vvelikova.ppmtool.services;

import com.vvelikova.ppmtool.domain.User;
import com.vvelikova.ppmtool.exceptions.UsernameAlreadyExistsException;
import com.vvelikova.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser) {

        try{
        // encode the password
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            //username has to be unique (exception)
            newUser.setUsername(newUser.getUsername());

            // we dont persist or show the confirmpassword
            newUser.setConfirmPassword("");
            return userRepository.save(newUser);
        }catch (Exception ex){
            throw new UsernameAlreadyExistsException("Username " + newUser.getUsername() + " already exists!");
        }

    }


}
