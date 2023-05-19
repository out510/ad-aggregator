package bsuir.web.adaggregator.controller;

import bsuir.web.adaggregator.domain.AdDefault;
import bsuir.web.adaggregator.dto.AdDto;
import bsuir.web.adaggregator.schedule.WebScrapingSchedule;
import bsuir.web.adaggregator.service.AdService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdController {
    @Resource
    private AdService adService;
    @Resource
    private WebScrapingSchedule webScrapingSchedule;

    @GetMapping("/ads")
    public String showAdsPage(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int pageSize,
                              Model model)  {
        Page<AdDefault> adPage = adService.getAds(page - 1, pageSize);
        model.addAttribute("pages", adPage);

        int start = Math.max(1, adPage.getNumber() - 2);
        int last = Math.min(start + 6, adPage.getTotalPages());
        model.addAttribute("start", start);
        model.addAttribute("last", last);

        return "ads";
    }

    @GetMapping("/ads/{adId}")
    public String showAdDetails(@PathVariable Long adId, Model model) {
        adService.getAdById(adId)
            .ifPresent(ad -> model.addAttribute("ad", ad));
        return "adDetails";
    }

    @GetMapping("/ads/scrape")
    public String showScrapePage() {
        return "scrape";
    }

    @PostMapping("/scrape")
    public String scrape() {
        webScrapingSchedule.scrapeAds();
        return "redirect:ads/scrape";
    }
}
