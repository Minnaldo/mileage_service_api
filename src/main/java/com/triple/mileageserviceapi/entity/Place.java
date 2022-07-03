package com.triple.mileageserviceapi.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Place {

    @Id                             // PK 지정
    @Column(name = "place_id")
    private String placeId;

    private String name;
}
