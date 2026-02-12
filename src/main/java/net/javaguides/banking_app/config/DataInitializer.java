package net.javaguides.banking_app.config;

import net.javaguides.banking_app.entity.Role;
import net.javaguides.banking_app.entity.User;
import net.javaguides.banking_app.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ROLE_ADMIN);
            userRepository.save(admin);

            System.out.println("\n═══════════════════════════════════════");
            System.out.println("✅ DEFAULT ADMIN CREATED!");
            System.out.println("   Username: admin");
            System.out.println("   Password: admin123");
            System.out.println("   ⚠️  Please change password after first login!");
            System.out.println("═══════════════════════════════════════\n");
        }
    }
}