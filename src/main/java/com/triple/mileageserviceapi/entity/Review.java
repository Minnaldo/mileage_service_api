package com.triple.mileageserviceapi.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Review {

    @Id
    @Column(name = "review_id")
    private String reviewId;

    private String content;

    // TODO Photo String -> 배열로
    @Column(name = "attached_photo_ids")
    private String attachedPhotoIds;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "place_id")
    private String placeId;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_delete")
    private String isDelete;

}
