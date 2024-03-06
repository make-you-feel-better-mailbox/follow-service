package com.onetwo.followservice.adapter.out.persistence.entity;

import com.onetwo.followservice.adapter.out.persistence.repository.converter.BooleanNumberConverter;
import com.onetwo.followservice.domain.Follow;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
@Entity
@NoArgsConstructor
@Table(name = "target_follow")
public class FollowEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String follower;

    @Column(nullable = false)
    private String followee;

    @Column(nullable = false, length = 1)
    @Convert(converter = BooleanNumberConverter.class)
    private Boolean state;

    public FollowEntity(Long id, String follower, String followee, Boolean state) {
        this.id = id;
        this.follower = follower;
        this.followee = followee;
        this.state = state;
    }

    public static FollowEntity domainToEntity(Follow follow) {
        FollowEntity likeEntity = new FollowEntity(
                follow.getId(),
                follow.getFollower(),
                follow.getFollowee(),
                follow.getState()
        );

        likeEntity.setMetaDataByDomain(follow);
        return likeEntity;
    }
}
