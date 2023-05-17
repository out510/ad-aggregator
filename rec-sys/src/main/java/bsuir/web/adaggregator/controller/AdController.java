package bsuir.web.adaggregator.controller;

import bsuir.web.adaggregator.domain.Ad;
import bsuir.web.adaggregator.domain.AdDefault;
import bsuir.web.adaggregator.service.AdService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class AdController {
    @Resource
    private AdService adService;

    @GetMapping("/ads")
    public String showAdsPage(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int pageSize,
                              Model model) throws IOException {
        Page<Ad> adPage = adService.getAds(page, pageSize);
        model.addAttribute("ads", adPage.getContent());
        model.addAttribute("totalPages", adPage.getTotalPages());
        return "ads";
    }
}
