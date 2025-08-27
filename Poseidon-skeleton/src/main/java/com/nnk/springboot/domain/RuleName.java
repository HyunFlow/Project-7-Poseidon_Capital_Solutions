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
@Table(name = "Rulename")
public class RuleName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;


    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "json")
    private String json;
    @Column(name = "template")
    private String template;
    @Column(name = "sqlStr")
    private String sqlStr;
    @Column(name = "sqlPart")
    private String sqlPart;
}
