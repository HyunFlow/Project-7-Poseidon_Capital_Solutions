package com.nnk.springboot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import java.sql.Timestamp;

@RequiredArgsConstructor
@Data
@Entity
@Table(name = "Curvepoint")
public class CurvePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    Integer id;

    @NotBlank(message = "must not be null")
    Integer curveId;

    @Column(name = "asOfDate")
    Timestamp asOfDate;
    @Column(name = "term")
    Double term;
    @Column(name = "value")
    Double value;
    @Column(name = "creationDate")
    Timestamp creationDate;
}
