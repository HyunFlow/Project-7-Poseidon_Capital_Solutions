package com.nnk.springboot;

import static org.junit.jupiter.api.Assertions.*;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class RatingTests {

    @Autowired
    private RatingRepository ratingRepository;

    @Test
    public void ratingTest() {
        Rating rating = new Rating();
        rating.setMoodysRating("Moodys Rating");
        rating.setSandPRating("Sand PRating");
        rating.setFitchRating("Fitch Rating");
        rating.setOrderNumber(10);

        // Save
        rating = ratingRepository.save(rating);
        assertNotNull(rating.getId());
        assertTrue(rating.getOrderNumber() == 10);

        // Update
        rating.setOrderNumber(20);
        rating = ratingRepository.save(rating);
        assertTrue(rating.getOrderNumber() == 20);

        // Find
        List<Rating> listResult = ratingRepository.findAll();
        assertTrue(listResult.size() > 0);

        // Delete
        Integer id = rating.getId();
        ratingRepository.delete(rating);
        Optional<Rating> ratingList = ratingRepository.findById(id);
        assertFalse(ratingList.isPresent());
    }
}
