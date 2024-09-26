package com.urbankicks.config;

import com.urbankicks.entities.Brand;
import com.urbankicks.entities.Category;
import com.urbankicks.entities.Gender;
import com.urbankicks.entities.UserRegister;
import com.urbankicks.repositories.BrandRepository;
import com.urbankicks.repositories.CategoryRepository;
import com.urbankicks.repositories.GenderRepository;
import com.urbankicks.repositories.UserRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataPreInitialization {

    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final GenderRepository genderRepository;
    private final UserRegisterRepository userRegisterRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Transactional
    public CommandLineRunner initDatabase() {
        return args -> {
            // Initialize genders
            Gender male = createGenderIfNotExists(Gender.GenderName.MALE);
            Gender female = createGenderIfNotExists(Gender.GenderName.FEMALE);
            Gender unisex = createGenderIfNotExists(Gender.GenderName.UNISEX);

            // Initialize brands
            List<String> brands = Arrays.asList("Nike", "Adidas", "Puma", "Reebok", "New Balance");
            for (String brandName : brands) {
                Brand brand = new Brand();
                brand.setBrandName(brandName);
                brand.setCreatedAt(LocalDateTime.now());
                if (!brandExists(brand)) {
                    brandRepository.save(brand);
                }
            }

            // Initialize categories
            createCategoryIfNotExists("Casual Shoes", male);
            createCategoryIfNotExists("Formal Shoes", male);
            createCategoryIfNotExists("Sports Shoes", male);
            createCategoryIfNotExists("Heels", female);
            createCategoryIfNotExists("Boys' Shoes", unisex);
            // Add other categories similarly...

            // Create an Admin User
            if (!userRegisterRepository.existsById(1)) {
                UserRegister adminUser = new UserRegister();
                adminUser.setUsername("nadeem");
                adminUser.setEmail("nadeempalkhiwala@urbankicks.com");
                adminUser.setPassword(passwordEncoder.encode("nadeem")); // Ensure to encode the password in a real application
                adminUser.setFirstname("Nadeem");
                adminUser.setLastname("Palkhiwala");
                adminUser.setRole(UserRegister.Role.ADMIN);
                adminUser.setCreatedAt(LocalDateTime.now());
                adminUser.setIsLoggedOut(false);
                userRegisterRepository.save(adminUser);
            }
        };
    }

    private Gender createGenderIfNotExists(Gender.GenderName genderName) {
        Gender gender = new Gender();
        gender.setGenderName(genderName);
        if (!genderExists(gender)) {
            genderRepository.save(gender);
        }
        // Return the managed instance
        return genderRepository.findByGenderName(genderName);
    }

    private boolean brandExists(Brand brand) {
        return brandRepository.existsByBrandName(brand.getBrandName());
    }

    private boolean genderExists(Gender gender) {
        return genderRepository.existsByGenderName(gender.getGenderName());
    }

    private void createCategoryIfNotExists(String categoryName, Gender gender) {
        if (!categoryExists(categoryName, gender)) {
            Category category = new Category();
            category.setCategoryName(categoryName);
            category.setGender(gender);
            category.setCreatedAt(LocalDateTime.now());
            categoryRepository.save(category);
        }
    }

    private boolean categoryExists(String categoryName, Gender gender) {
        return categoryRepository.existsByCategoryNameAndGender(categoryName, gender);
    }
}
