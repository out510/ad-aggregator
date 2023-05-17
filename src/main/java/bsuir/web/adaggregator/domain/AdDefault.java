package bsuir.web.adaggregator.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AdDefault implements Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 500)
    private String title;
    @Column(length = 2000)
    private String description;
    private Double price;
    @Column(unique = true)
    private String originalUrl;

    @Override
    public void init() {
        // do nothing
    }
}

