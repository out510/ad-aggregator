package bsuir.web.adaggregator.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdDefault implements Ad {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private String originalUrl;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @Override
    public void init() {
        // do nothing
    }
}

