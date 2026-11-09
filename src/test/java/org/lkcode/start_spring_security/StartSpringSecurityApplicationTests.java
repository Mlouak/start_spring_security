package org.lkcode.start_spring_security;

import org.junit.jupiter.api.Test;
import org.lkcode.start_spring_security.repositories.UserRepository;
import org.lkcode.start_spring_security.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class StartSpringSecurityApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private CommandLineRunner initData;

    @Test
    void contextLoads() {
    }

    @Test
    void mapsRolesToSpringSecurityAuthorities() {
        var user = userDetailsService.loadUserByUsername("user2");
        var admin = userDetailsService.loadUserByUsername("user3");

        assertTrue(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertTrue(admin.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertTrue(admin.getAuthorities().contains(new SimpleGrantedAuthority("WRITE")));
    }

    @Test
    void initializerCanRunMoreThanOnce() throws Exception {
        initData.run();

        assertEquals(3, userRepository.count());
    }
}
