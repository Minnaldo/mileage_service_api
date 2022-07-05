package com.triple.mileageserviceapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @Column(name = "user_id")
    private String userId;


    private int point;
}
