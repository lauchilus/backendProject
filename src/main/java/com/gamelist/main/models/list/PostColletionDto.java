package com.gamelist.main.models.list;

import org.springframework.web.multipart.MultipartFile;

public record PostColletionDto(

        MultipartFile image,
        String name
) {
}
