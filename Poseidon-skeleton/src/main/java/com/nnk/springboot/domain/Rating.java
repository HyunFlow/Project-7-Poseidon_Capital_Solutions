package com.nnk.springboot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
@Entity
@Table(name = "Rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    Integer id;

    @Column(name = "moodysRating")
    String moodysRating;
    @Column(name = "sandPRating")
    String sandPRating;
    @Column(name = "fitchRating")
    String fitchRating;
    @Column(name = "orderNumber")
    Integer orderNumber;
}
