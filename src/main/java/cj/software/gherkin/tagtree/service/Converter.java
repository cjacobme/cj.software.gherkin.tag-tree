package cj.software.gherkin.tagtree.service;

import cj.software.gherkin.tagtree.entity.Coordinate;
import cj.software.gherkin.tagtree.entity.ParsedFeature;
import cj.software.gherkin.tagtree.entity.ParsedScenario;

import java.util.*;

public class Converter {
    public SortedMap<String, SortedSet<Coordinate>> convert(Collection<ParsedFeature> features) {
        SortedMap<String, SortedSet<Coordinate>> result = new TreeMap<>();
        for (ParsedFeature feature : features) {
             rolloutFeatureTags(result, feature);
             rolloutScenarioTags(result, feature);
        }
        return result;
    }

    private SortedMap<String, SortedSet<Coordinate>> rolloutFeatureTags (
            SortedMap<String, SortedSet<Coordinate>> source,
            ParsedFeature feature) {
        SortedMap<String, SortedSet<Coordinate>> result = source;
        String featureName = feature.getName();
        for (String tag : feature.getTags()) {
            for (ParsedScenario scenario : feature.getScenarios()) {
                String scenarioName = scenario.getName();
                add(result, tag, featureName, scenarioName);
            }
        }
        return result;
    }

    private SortedMap<String, SortedSet<Coordinate>> rolloutScenarioTags (
            SortedMap<String, SortedSet<Coordinate>> source,
            ParsedFeature feature) {
        SortedMap<String, SortedSet<Coordinate>> result = source;
        String featureName = feature.getName();
        for (ParsedScenario scenario : feature.getScenarios()) {
            String scenarioName = scenario.getName();
            for (String tag : scenario.getTags()) {
                add(result, tag, featureName, scenarioName);
            }
        }
        return result;
    }

    private SortedMap<String, SortedSet<Coordinate>> add (
            SortedMap<String, SortedSet<Coordinate>> source,
            String tag,
            String featureName,
            String scenarioName) {
        SortedMap<String, SortedSet<Coordinate>> result = source;
        SortedSet<Coordinate> coordinates = result.computeIfAbsent(tag, k -> new TreeSet<>());
        Coordinate coordinate = Coordinate.builder().withFeature(featureName).withScenario(scenarioName).build();
        coordinates.add(coordinate);
        return result;
    }
}
