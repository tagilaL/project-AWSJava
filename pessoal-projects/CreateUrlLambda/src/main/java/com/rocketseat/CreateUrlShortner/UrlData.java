package com.rocketseat.CreateUrlShortner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UrlData {
    private String urlOriginal;
    private long expirationTime;
}
