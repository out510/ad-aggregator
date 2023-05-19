package bsuir.web.adaggregator.dto;

import bsuir.web.adaggregator.domain.AdDefault;
import lombok.Getter;

import java.util.List;

@Getter
public class AdDto {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private String originalUrl;
    private List<String> similarAds;

    public AdDto(AdDefault adDefault, List<String> similarAds) {
        this(
            adDefault.getId(),
            adDefault.getTitle(),
            adDefault.getDescription(),
            adDefault.getPrice(),
            adDefault.getOriginalUrl(),
            similarAds
        );
    }

    public AdDto(Long id, String title, String description, Double price, String originalUrl, List<String> similarAds) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.originalUrl = originalUrl;
        this.similarAds = similarAds;
    }
}
