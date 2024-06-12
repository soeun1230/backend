package iise_capston.imgcloud.member;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor
@Table(name = "PeopleImage")
public class PeopleImageMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long peopleId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private OauthMember userPeopleId;

    @Column
    private String ImageKey;

    @Column
    private String smallImageKey;

    @Column
    private double totalScore;

    @Column
    private double metaScore;

    @Column
    private double brisqueScore;

    @Column
    private String imageTitle;

    @Column
    private String coordinate;
}
