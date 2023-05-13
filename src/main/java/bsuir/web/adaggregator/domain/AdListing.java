package bsuir.web.adaggregator.domain;

import java.io.IOException;
import java.util.List;

/**
 * Represents listing of ads from the specified web-site
 * TODO: make class generic to work with different type of Ads, not only with AdFromUrl. Because there will be several AdFromUrl for different web sites
 */
public interface AdListing {

    /**
     * Set initial values as first page and last page.
     * <p>
     * Calling some methods without initializing can produce {@link bsuir.web.adaggregator.exception.NotInitializedException}
     */
    void init();

    /**
     * Get adds from page of provided number from the URL specified during ad listing creation
     *
     * @param pageNumber number of the page
     * @return list of ads from specified page
     * @throws IOException can occur during downloading html page
     */
    List<Ad> getAdsFromPage(int pageNumber) throws IOException;

    /**
     * Get number of the first page in the listing.
     * <p>
     * This is the number listed on the website, the technical number will be one less
     *
     * @return number of the first page
     * @throws bsuir.web.adaggregator.exception.NotInitializedException when ad listing is not initialized
     */
    int getFirsPageNumber();

    /**
     * Get number of the last page in the listing.
     * <p>
     * This is the number listed on the website, the technical number will be one less
     *
     * @return number of the last page
     * @throws bsuir.web.adaggregator.exception.NotInitializedException when ad listing is not initialized
     */
    int getLastPageNumber();
}
