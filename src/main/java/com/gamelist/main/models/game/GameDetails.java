package com.gamelist.main.models.game;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "game_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDetails {

    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(length = 1000)
    private String summary;
    private String name;

    @Column(name = "first_release_date")
    @Temporal(TemporalType.DATE)
    private LocalDate firstReleaseDate;

    @Column(name = "image_url")
    private String imageUrl;

    private String company;
}
