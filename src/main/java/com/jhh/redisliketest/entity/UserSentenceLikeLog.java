package com.jhh.redisliketest.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_sentence_like_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSentenceLikeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_sentence_like_log_id")
    private Integer userSentenceLikeLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_date_sentence_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private UserDateSentence userDateSentence;

    @Column(name = "like_yn")
    private Boolean likeYn;

    @Column(name = "create_at", updatable = false)
    private LocalDateTime createAt;

    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
    }
}
