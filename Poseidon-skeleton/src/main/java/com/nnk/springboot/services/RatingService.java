package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dto.RatingDto;
import com.nnk.springboot.repositories.RatingRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    @Transactional
    public void createRating(RatingDto request) {
        Rating rating = new Rating();
        rating.setMoodysRating(request.getMoodysRating());
        rating.setSandPRating(request.getSandPRating());
        rating.setFitchRating(request.getFitchRating());
        rating.setOrderNumber(request.getOrderNumber());

        ratingRepository.save(rating);
    }

    @Transactional
    public void updateRating(Integer id, RatingDto request) {
        Rating currentRating = ratingRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Rating not found"));

        currentRating.setMoodysRating(request.getMoodysRating());
        currentRating.setSandPRating(request.getSandPRating());
        currentRating.setFitchRating(request.getFitchRating());
        currentRating.setOrderNumber(request.getOrderNumber());

        ratingRepository.save(currentRating);
    }

    @Transactional
    public void deleteRating(Integer id) {
        if (!ratingRepository.existsById(id)) {
            throw new IllegalArgumentException("Rating not found");
        }
        ratingRepository.deleteById(id);
    }

    public RatingDto loadRatingById(Integer id) {
        Rating currentRating = ratingRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Rating not found"));

        return getRatingDto(currentRating);
    }

    public List<RatingDto> loadAllRatings() {
        return ratingRepository.findAll().stream()
            .map(this::getRatingDto).toList();
    }

    private RatingDto getRatingDto(Rating currentRating) {
        RatingDto ratingDto = new RatingDto();
        ratingDto.setId(currentRating.getId());
        ratingDto.setMoodysRating(currentRating.getMoodysRating());
        ratingDto.setSandPRating(currentRating.getSandPRating());
        ratingDto.setFitchRating(currentRating.getFitchRating());
        ratingDto.setOrderNumber(currentRating.getOrderNumber());

        return ratingDto;
    }
}
