package cj.software.gherkin.tagtree.service;

import cj.software.gherkin.tagtree.entity.ParsedFeature;
import cj.software.gherkin.tagtree.entity.ParsedScenario;
import io.cucumber.gherkin.GherkinParser;
import io.cucumber.messages.types.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

@Service
public class GherkinTreeParser {
    private final Logger logger = LogManager.getFormatterLogger();

    public SortedSet<ParsedFeature> parse(String uri, InputStream is) throws IOException {
        SortedSet<ParsedFeature> result = new TreeSet<>();
        GherkinParser gherkinParser = GherkinParser.builder()
                .includeSource(false)
                .includePickles(false)
                .build();
        Stream<Envelope> stream =  gherkinParser.parse(uri, is);
        stream.forEach(envelope -> {
            Optional<GherkinDocument> optGherkinDocument = envelope.getGherkinDocument();
            if (optGherkinDocument.isPresent()) {
                GherkinDocument gherkinDocument = optGherkinDocument.get();
                Optional<Feature> optFeature = gherkinDocument.getFeature();
                if (optFeature.isPresent()) {
                    Feature feature = optFeature.get();
                    String name = feature.getName();
                    logger.info("parse scenario \"%s\"...", name);
                    ParsedFeature parsedFeature = ParsedFeature.builder().withName(name).build();
                    parsedFeature = parse(parsedFeature, feature);
                    result.add(parsedFeature);
                }
            }
        });
        return result;
    }

    private ParsedFeature parse (ParsedFeature source, Feature feature) {
        ParsedFeature result = source;
        List<FeatureChild> children = feature.getChildren();
        for (FeatureChild featureChild : children) {
            Optional<Scenario> optScenario = featureChild.getScenario();
            if (optScenario.isPresent()) {
                Scenario scenario = optScenario.get();
                String name = scenario.getName();
                ParsedScenario parsedScenario = ParsedScenario.builder().withName(name).build();
                result.addScenario(parsedScenario);
            }
        }
        return result;
    }
}
