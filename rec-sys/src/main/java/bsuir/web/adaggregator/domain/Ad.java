package bsuir.web.adaggregator.domain;

/**
 * Represents data holder for a common ad
 */
public interface Ad {
    /**
     * Get title of the ad
     * @return ad title
     */
    String getTitle();

    /**
     * Get description of the ad
     * @return ad description
     */
    String getDescription();

    /**
     * Get price of the ad
     * @return ad price
     */
    Double getPrice();

    /**
     * Get url for the original ad in external resources
     * @return url of the original ad
     */
    String getOriginalUrl();

    /**
     * Initialize internal state of the ad
     */
    void init();
}
