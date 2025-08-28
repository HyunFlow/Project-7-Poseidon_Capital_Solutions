package com.nnk.springboot.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dto.RatingDto;
import com.nnk.springboot.repositories.RatingRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingService ratingService;

    @Test
    void createRating_savesEntityWithRequest() {
        RatingDto request = new RatingDto();
        request.setMoodysRating("A+");
        request.setSandPRating("B-");
        request.setFitchRating("C++");
        request.setOrderNumber(100);

        ratingService.createRating(request);

        ArgumentCaptor<Rating> captor = ArgumentCaptor.forClass(Rating.class);
        verify(ratingRepository).save(captor.capture());
        verifyNoMoreInteractions(ratingRepository);

        Rating saved = captor.getValue();
        assertEquals("A+", saved.getMoodysRating());
        assertEquals("B-", saved.getSandPRating());
        assertEquals("C++", saved.getFitchRating());
        assertEquals(100, saved.getOrderNumber());
    }

    @Test
    void updateRating_updatesFoundEntityAndSaves() {
        Integer id = 1;

        Rating existing = new Rating();
        existing.setId(id);
        existing.setMoodysRating("A+");
        existing.setSandPRating("B-");
        existing.setFitchRating("C++");
        existing.setOrderNumber(100);

        when(ratingRepository.findById(id)).thenReturn(Optional.of(existing));

        RatingDto request = new RatingDto();
        request.setMoodysRating("New ModdysRating");
        request.setSandPRating("New SandPRating");
        request.setFitchRating("New FitchRating");
        request.setOrderNumber(1);

        ratingService.updateRating(id, request);

        ArgumentCaptor<Rating> captor = ArgumentCaptor.forClass(Rating.class);
        verify(ratingRepository).findById(id);
        verify(ratingRepository).save(captor.capture());
        verifyNoMoreInteractions(ratingRepository);

        Rating saved = captor.getValue();
        assertEquals("New ModdysRating", saved.getMoodysRating());
        assertEquals("New SandPRating", saved.getSandPRating());
        assertEquals("New FitchRating", saved.getFitchRating());
        assertEquals(1, saved.getOrderNumber());
    }

    @Test
    void updateRating_throws_whenNotFound() {
        Integer id = 1;
        when(ratingRepository.findById(id)).thenReturn(Optional.empty());

        RatingDto request = new RatingDto();
        request.setMoodysRating("A+");
        request.setSandPRating("B-");
        request.setFitchRating("C++");
        request.setOrderNumber(100);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, ()-> ratingService.updateRating(id, request));
        assertEquals("Rating not found", ex.getMessage());

        verify(ratingRepository).findById(id);
        verifyNoMoreInteractions(ratingRepository);
    }

    @Test
    void deleteRating_deletes_whenExists() {
        when(ratingRepository.existsById(1)).thenReturn(true);
        ratingService.deleteRating(1);

        verify(ratingRepository).existsById(1);
        verify(ratingRepository).deleteById(1);
    }

    @Test
    void deleteRating_throws_whenNotFound() {
        when(ratingRepository.existsById(1)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, ()-> ratingService.deleteRating(1));
        verify(ratingRepository).existsById(1);
        verifyNoMoreInteractions(ratingRepository);
    }
}
