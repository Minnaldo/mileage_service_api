package com.triple.mileageserviceapi.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewRequestDto {
    String type;
    String action;
    String reviewId;
    String content;
    String attachedPhotoIds;
    String userId;
    String placeId;
}
