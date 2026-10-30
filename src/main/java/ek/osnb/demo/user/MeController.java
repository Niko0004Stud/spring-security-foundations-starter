package ek.osnb.demo.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class MeController {

    record MeResponse(String username, List<String> roles) {
    }

    @GetMapping("/api/me")
    MeResponse me(Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                // Spring Security 7 also adds FACTOR_PASSWORD, which is not a role
                .filter(authority -> authority.startsWith("ROLE_"))
                .toList();

        return new MeResponse(authentication.getName(), roles);
    }
}
