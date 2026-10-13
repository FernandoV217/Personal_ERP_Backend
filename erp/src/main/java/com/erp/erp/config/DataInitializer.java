package com.erp.erp.config;

import com.erp.erp.Model.Role;
import com.erp.erp.Model.User;
import com.erp.erp.repository.RoleRepository;
import com.erp.erp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Crear Roles si no existen
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));
        roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_USER")));

        // 2. Crear Administrador por defecto si no existe
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@nexuserp.com");
            admin.setFullName("Administrador del Sistema");
            admin.setPassword(passwordEncoder.encode("admin123")); // Contraseña por defecto
            admin.setRoles(Set.of(adminRole));

            userRepository.save(admin);
            System.out.println(">>> Usuario administrador por defecto creado (admin / admin123)");
        }
    }
}
