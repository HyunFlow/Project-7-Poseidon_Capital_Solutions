package com.nnk.springboot.dto;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RatingDto {

    private Integer id;
    @Size(max = 125)
    private String moodysRating;
    @Size(max = 125)
    private String sandPRating;
    @Size(max = 125)
    private String fitchRating;
    @PositiveOrZero
    private Integer orderNumber;
}
