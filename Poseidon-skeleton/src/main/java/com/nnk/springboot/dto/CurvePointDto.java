package com.nnk.springboot.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CurvePointDto {

    private Integer id;

    @NotNull(message = "must not be null")
    private Integer curveId;

    @Digits(integer = 6, fraction = 1, message = "must be a numeric value and at most 1 decimal place.")
    private Double term;

    @Digits(integer = 6, fraction = 1, message = "must be a numeric value and at most 1 decimal place.")
    private Double value;
}
