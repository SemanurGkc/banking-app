package net.javaguides.banking_app.service;

import net.javaguides.banking_app.entity.User;
import net.javaguides.banking_app.repository.IUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUserRepository userRepository,
                       PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public User register(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

}
