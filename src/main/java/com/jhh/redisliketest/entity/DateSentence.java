package com.jhh.redisliketest.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "date_sentences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateSentence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sentence_id")
    private Integer sentenceId;

    @Column(name = "sentence", length = 255)
    private String sentence;

    @Column(name = "date_id")
    private LocalDateTime dateId;

    @Column(name = "level", length = 2)
    private String level;

    @OneToMany(mappedBy = "dateSentence", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<UserDateSentence> userDateSentences = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        if (this.dateId == null) {
            this.dateId = LocalDateTime.now();
        }
    }
}
