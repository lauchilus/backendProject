package com.gamelist.main.models.reviews;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

public record ReviewUpdate(
        LocalDate date,
        float rating,
        String reviewText

) {
}
