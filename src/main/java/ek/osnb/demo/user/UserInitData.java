package ek.osnb.demo.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
class UserInitData implements CommandLineRunner {

    private final AppUserRepository users;
    private final PasswordEncoder encoder;

    UserInitData(AppUserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        // The password is pw for all three users, stored as a BCrypt hash
        users.save(new AppUser("user1", encoder.encode("pw"), "USER"));
        users.save(new AppUser("user2", encoder.encode("pw"), "USER"));
        users.save(new AppUser("admin", encoder.encode("pw"), "ADMIN"));

        // Print what is stored in the database
        for (AppUser user : users.findAll()) {
            System.out.println(user.getUsername() + " " + user.getPassword() + " " + user.getRole());
        }
    }
}
