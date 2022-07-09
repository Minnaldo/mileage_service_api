package com.triple.mileageserviceapi.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PointCheckRequestDto {
    String userId;
    int point;
}
