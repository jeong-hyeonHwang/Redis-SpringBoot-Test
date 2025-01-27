package com.jhh.redisliketest.entity;

import lombok.*;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_date_sentences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDateSentence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_date_sentence_id")
    private Integer userDateSentenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "date_sentence_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private DateSentence dateSentence;

    @OneToMany(mappedBy = "userDateSentence", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<UserSentenceLikeLog> userSentenceLikeLogs = new HashSet<>();
}
