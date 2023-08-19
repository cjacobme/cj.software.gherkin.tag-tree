package cj.software.gherkin.tagtree.service;

import static org.assertj.core.api.Assertions.*;

import cj.software.gherkin.tagtree.entity.ParsedFeature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.SortedSet;

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
            assertThat(first.getScenarios()).extracting("name").containsExactly("scenario without a tag");
        }
    }
}
