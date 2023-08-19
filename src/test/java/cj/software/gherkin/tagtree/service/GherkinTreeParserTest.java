package cj.software.gherkin.tagtree.service;

import cj.software.gherkin.tagtree.entity.ParsedFeature;
import cj.software.gherkin.tagtree.entity.ParsedScenario;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.SortedSet;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GherkinTreeParser.class)
class GherkinTreeParserTest {
    @Autowired
    private GherkinTreeParser parser;

    @Test
    void metadata() {
        Service service = GherkinTreeParser.class.getAnnotation(Service.class);
        assertThat(service).as("@Service").isNotNull();
    }

    @Test
    void withoutTags() throws IOException {
        try (InputStream is = GherkinTreeParserTest.class.getResourceAsStream("NoTags.feature")) {
            SortedSet<ParsedFeature> parsed = parser.parse("NoTags.feature", is);
            assertThat(parsed).extracting("name").containsExactly("no tags");
            ParsedFeature first = parsed.first();
            SoftAssertions softy = new SoftAssertions();
            softy.assertThat(first.getScenarios()).extracting("name").containsExactly("scenario without a tag");
            softy.assertThat(first.getTags()).as("tags").isEmpty();
            softy.assertAll();
        }
    }

    @Test
    void withTagAtScenario() throws IOException {
        try (InputStream is = GherkinTreeParserTest.class.getResourceAsStream("TagAtScenario.feature")) {
            SortedSet<ParsedFeature> parsed = parser.parse("TagAtScenario.feature", is);
            assertThat(parsed).extracting("name").containsExactly("Tags only at a scenario");
            ParsedFeature feature = parsed.first();
            SortedSet<ParsedScenario> scenarios = feature.getScenarios();
            SoftAssertions softy = new SoftAssertions();
            softy.assertThat(feature.getTags()).isEmpty();
            softy.assertThat(scenarios).hasSize(1);
            softy.assertAll();
            ParsedScenario scenario = scenarios.first();
            assertThat(scenario).isNotNull();
            softy = new SoftAssertions();
            softy.assertThat(scenario.getName()).isEqualTo("the one with tags");
            softy.assertThat(scenario.getTags()).as("tags").containsExactly("@OnlyAtScenario");
            softy.assertAll();
        }
    }
}
