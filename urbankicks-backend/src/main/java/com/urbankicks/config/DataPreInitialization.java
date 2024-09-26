package com.urbankicks.config;

import com.urbankicks.entities.Brand;
import com.urbankicks.entities.Category;
import com.urbankicks.entities.Gender;
import com.urbankicks.repositories.BrandRepository;
import com.urbankicks.repositories.CategoryRepository;
import com.urbankicks.repositories.GenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Bean
    @Transactional
    public CommandLineRunner initDatabase() {
        return args -> {
            // Initialize genders
            Gender male = new Gender();
            male.setGenderName(Gender.GenderName.MALE);
            if (!genderExists(male)) {
                genderRepository.save(male);
            }

            Gender female = new Gender();
            female.setGenderName(Gender.GenderName.FEMALE);
            if (!genderExists(female)) {
                genderRepository.save(female);
            }

            Gender unisex = new Gender();
            unisex.setGenderName(Gender.GenderName.UNISEX);
            if (!genderExists(unisex)) {
                genderRepository.save(unisex);
            }

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
            createCategoryIfNotExists("Loafers", male);
            createCategoryIfNotExists("Sneakers", male);
            createCategoryIfNotExists("Sandals", male);
            createCategoryIfNotExists("Slippers", male);
            createCategoryIfNotExists("Boots", male);
            createCategoryIfNotExists("Oxfords", male);
            createCategoryIfNotExists("Brogues", male);

            createCategoryIfNotExists("Heels", female);
            createCategoryIfNotExists("Flats", female);
            createCategoryIfNotExists("Wedges", female);
            createCategoryIfNotExists("Sandals", female);
            createCategoryIfNotExists("Sneakers", female);
            createCategoryIfNotExists("Ballet Flats", female);
            createCategoryIfNotExists("Pumps", female);
            createCategoryIfNotExists("Boots", female);
            createCategoryIfNotExists("Mules", female);
            createCategoryIfNotExists("Slippers", female);

            createCategoryIfNotExists("Boys' Shoes", unisex);
            createCategoryIfNotExists("Girls' Shoes", unisex);
            createCategoryIfNotExists("Sneakers", unisex);
            createCategoryIfNotExists("Sandals", unisex);
            createCategoryIfNotExists("Boots", unisex);
            createCategoryIfNotExists("School Shoes", unisex);
            createCategoryIfNotExists("Sports Shoes", unisex);
            createCategoryIfNotExists("Slippers", unisex);

            createCategoryIfNotExists("Running Shoes", unisex);
            createCategoryIfNotExists("Hiking Boots", unisex);
            createCategoryIfNotExists("Training Shoes", unisex);
            createCategoryIfNotExists("Basketball Shoes", unisex);
            createCategoryIfNotExists("Football Shoes", unisex);
            createCategoryIfNotExists("Tennis Shoes", unisex);
            createCategoryIfNotExists("Cleats", unisex);

            createCategoryIfNotExists("Orthopedic Shoes", unisex);
            createCategoryIfNotExists("Waterproof Shoes", unisex);
            createCategoryIfNotExists("Vegan Footwear", unisex);
            createCategoryIfNotExists("Slip-resistant Shoes", unisex);

            createCategoryIfNotExists("Flip Flops", unisex);
            createCategoryIfNotExists("Winter Boots", unisex);
            createCategoryIfNotExists("Rain Boots", unisex);
        };
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
