package bsuir.web.adaggregator.service.imp;

import bsuir.web.adaggregator.domain.Ad;
import bsuir.web.adaggregator.domain.AdDefault;
import bsuir.web.adaggregator.repository.AdRepository;
import bsuir.web.adaggregator.service.SimilarityService;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class SimilarityServiceImpl implements SimilarityService {
    public static final double SIMILARITY_THRESHOLD = 0.4;
    private final AdRepository adRepository;

    @Override
    public Set<AdDefault> getSimilarAds(Ad ad) {
        return adRepository.findAll()
            .stream()
            .filter(adFromDb -> !Objects.equals(ad.getId(), adFromDb.getId()) && areAdsSimilar(ad, adFromDb))
            .collect(Collectors.toSet());
    }

    private boolean areAdsSimilar(Ad ad1, Ad ad2) {
        // Создание объектов Document для обоих текстов
        Document document1 = new Document(String.format("%s %s", ad1.getTitle(), ad1.getDescription()));
        Document document2 = new Document(String.format("%s %s", ad2.getTitle(), ad2.getDescription()));

        // Получение предложений из каждого документа
        List<Sentence> sentences1 = document1.sentences();
        List<Sentence> sentences2 = document2.sentences();

        // Вычисление схожести на основе количества общих предложений
       return calculateTextSimilarity(sentences1, sentences2) >= SIMILARITY_THRESHOLD;
    }

    private double calculateTextSimilarity(List<Sentence> sentences1, List<Sentence> sentences2) {
        // Вычисление количества общих предложений
        long numberOfSimilarSentences = sentences1.stream()
            .filter(sentence1 -> sentences2.stream().anyMatch(sentence2 -> areSentencesSimilar(sentence1, sentence2)))
            .count();
        // Вычисление степени схожести на основе индекса Жаккара
        return (double) numberOfSimilarSentences / getTextUnionSize(sentences1.size(), sentences2.size(), numberOfSimilarSentences);
    }

    private boolean areSentencesSimilar(Sentence sentence1, Sentence sentence2) {
        return calculateSentencesSimilarityIndex(sentence1, sentence2) >= SIMILARITY_THRESHOLD;
    }

    // Jaccard Index
    private double calculateSentencesSimilarityIndex(Sentence sentence1, Sentence sentence2) {
        Set<String> tokens1 = tokenizeText(sentence1.text());
        Set<String> tokens2 = tokenizeText(sentence2.text());
        return (double) getIntersectionSize(tokens1, tokens2) / getUnionSize(tokens1, tokens2);
    }

    private Set<String> tokenizeText(String text) {
        return Stream.of(text.toLowerCase().split("\\W+"))
            .filter(StringUtils::isNotEmpty)
            .collect(Collectors.toSet());
    }

    private long getIntersectionSize(Set<String> words1, Set<String> words2) {
        return words1.stream()
            .filter(word1 -> words2.stream().anyMatch(word2 -> areWordsSimilar(word1, word2)))
            .count();
    }

    private boolean areWordsSimilar(String word1, String word2) {
        // TODO: add check for mistakes and synonyms
        return word1.equals(word2);
    }

    private int getUnionSize(Set<String> set1, Set<String> set2) {
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        return union.size();
    }

    private long getTextUnionSize(long sentencesSize1, long sentencesSize2, long numberOfSimilarSentences) {
        return sentencesSize1 + sentencesSize2 - numberOfSimilarSentences;
    }
}
