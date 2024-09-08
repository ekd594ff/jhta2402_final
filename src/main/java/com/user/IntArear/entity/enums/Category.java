package com.user.IntArear.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category{
    WALLPAPERANDFLOORING("wallpaperandflooring"),
    FILMANDTILES("filmandtiles"),
    LIGHTS("lights"),
    OTHER("other");

    private final String category;
}
