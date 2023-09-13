package com.prodapt.practice.service;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.prodapt.practice.entity.User;
import com.prodapt.practice.exception.Exception;
import com.prodapt.practice.repository.UserRepository;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> authenticate(String username, String password) {
        Optional<User> optUser = userRepository.findByName(username);
        if (optUser.isEmpty()) {
            throw new Exception("User not found");
        }
        if (!optUser.get().getPassword().equals(password)) {
            return Optional.empty();
        }
        return optUser;
    }
    public User create(User user) {
        return userRepository.save(user);
    } 
}