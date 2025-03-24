package com.example.backup_service.Controller;

import com.example.backup_service.Model.MyAppUser;
import com.example.backup_service.Model.MyAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/req/users")
public class UserController {

    @Autowired
    private MyAppUserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody MyAppUser user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Username already exists"));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(Collections.singletonMap("message", "User added successfully"));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editUser(@PathVariable Long id, @RequestBody MyAppUser updateduser) {
        return userRepository.findById(id).map(user -> {
            if (!user.getUsername().equals(updateduser.getUsername()) &&
                userRepository.findByUsername(updateduser.getUsername()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("message", "Username already exists"));
            }

            user.setUsername(updateduser.getUsername());
            user.setEmail(updateduser.getEmail());
            user.setRole(updateduser.getRole());

            if (updateduser.getPassword() != null && !updateduser.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(updateduser.getPassword()));
            }
            userRepository.save(user);
            return ResponseEntity.ok(Collections.singletonMap("message", "User updated successfully"));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "User not found")));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "User deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "User not found"));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(
        @RequestParam(required = false) String username,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String password, Principal principal) {

        String currentUsername = principal.getName();
        Optional<MyAppUser> userOpt = userRepository.findByUsername(currentUsername);

        if (userOpt.isPresent()) {
                MyAppUser user = userOpt.get();

                if (userRepository.findByEmail(email).isPresent() && !user.getEmail().equals(email)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Email already exists!"));
                }

                user.setUsername(username);
                user.setEmail(email);
                user.setPassword(passwordEncoder.encode(password));

                userRepository.save(user);
                return ResponseEntity.ok(Collections.singletonMap("message", "Profile updated successfully!"));
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "User not found!"));
}

}


