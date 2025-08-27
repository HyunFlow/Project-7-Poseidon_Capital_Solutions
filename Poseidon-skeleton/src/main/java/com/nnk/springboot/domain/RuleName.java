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


    @Column(name = "name", length = 125)
    private String name;
    @Column(name = "description", length = 125)
    private String description;
    @Column(name = "json", length = 125)
    private String json;
    @Column(name = "template", length = 512)
    private String template;
    @Column(name = "sqlStr", length = 125)
    private String sqlStr;
    @Column(name = "sqlPart", length = 125)
    private String sqlPart;
}
