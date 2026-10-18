package com.example.social_network.validator;

import com.example.social_network.domain.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        String errors = "";

        if (entity.getUsername() == null || entity.getUsername().trim().isEmpty()) {
            errors += "Username cannot be empty.\n";
        }

        else if (entity.getUsername().length() < 3) {
            errors += "Username must be at least 3 characters.\n";
        }

        if (entity.getFirstName() == null || entity.getFirstName().trim().isEmpty()) {
            errors += "First name cannot be empty.\n";
        }

        if (entity.getLastName() == null || entity.getLastName().trim().isEmpty()) {
            errors += "Last name cannot be empty.\n";
        }

        if (entity.getEmail() == null || !entity.getEmail().contains("@") || !entity.getEmail().contains(".")) {
            errors += "Email is invalid.\n";
        }

        if (entity.getBirthDate() == null || entity.getBirthDate().isAfter(LocalDate.now().minusYears(13))) {
            errors += "User must be at least 13 years old.\n";
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
