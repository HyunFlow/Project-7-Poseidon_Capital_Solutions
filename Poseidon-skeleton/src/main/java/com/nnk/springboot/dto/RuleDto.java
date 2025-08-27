package com.nnk.springboot.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RuleDto {

    private Integer id;
    @Size(max = 125)
    private String name;
    @Size(max = 125)
    private String description;
    @Size(max = 125)
    private String json;
    @Size(max = 512)
    private String template;
    @Size(max = 125)
    private String sqlStr;
    @Size(max = 125)
    private String sqlPart;

}
