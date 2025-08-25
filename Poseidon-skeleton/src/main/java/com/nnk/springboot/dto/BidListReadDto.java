package com.nnk.springboot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BidListReadDto {
    private Integer bidListId;
    private String account;
    private String type;
    private Double bidQuantity;
}
