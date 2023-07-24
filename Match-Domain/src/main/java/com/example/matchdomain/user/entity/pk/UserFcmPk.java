package com.example.matchdomain.user.entity.pk;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFcmPk implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name="userId")
    private Long userId;

    @Column(name = "fcmToken")
    private String fcmToken;
}