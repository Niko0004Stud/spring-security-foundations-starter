package ek.osnb.bff.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
class UserInitData implements CommandLineRunner {

    private final AppUserRepository users;

    UserInitData(AppUserRepository users) {
        this.users = users;
    }

    @Override
    public void run(String... args) {
        // The password is pw for all three users, stored as a BCrypt hash
        users.save(new AppUser("user1", "$2a$10$OawES41Z9XNBSv7a.F5a8.sDeQmcB..ChMdGXCopd7N4xCoCTJIzu", "USER"));
        users.save(new AppUser("user2", "$2a$10$J2sxlEUm0HDYBE1S8IKUUeTG569zwIDAKzslrrMflUsUwE7oo7jf.", "USER"));
        users.save(new AppUser("admin", "$2a$10$9/FvDCoWIqbkSIjIGGnQWeBPQ3vlipgvFc75S2xp503PvL1qcF66W", "ADMIN"));

        // Print what is stored in the database
        for (AppUser user : users.findAll()) {
            System.out.println(user.getUsername() + " " + user.getPassword() + " " + user.getRole());
        }
    }
}
