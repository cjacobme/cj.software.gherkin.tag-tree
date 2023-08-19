package cj.software.gherkin.tagtree.service;

import cj.software.gherkin.tagtree.entity.Coordinate;
import cj.software.gherkin.tagtree.entity.ParsedFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Converter.class)
class ConverterTest {
    @Autowired
    private Converter converter;

    @Test
    void scenario1() throws IOException {
        scenario("scenario1.json", "converted1.json");
    }

    @Test
    void scenario2() throws IOException {
        scenario("scenario2.json", "converted2.json");
    }

    private void scenario(String sourceName, String expectedName) throws IOException {
        try (InputStream isSource = ConverterTest.class.getResourceAsStream(sourceName)) {
            try (InputStream isExpected = ConverterTest.class.getResourceAsStream(expectedName)) {
                ObjectMapper mapper = new ObjectMapper();
                Set<ParsedFeature> features = mapper.readValue(isSource, new TypeReference<>() {
                });
                SortedMap<String, SortedSet<Coordinate>> converted = converter.convert(features);
                SortedMap<String, SortedSet<Coordinate>> expected = mapper.readValue(isExpected, new TypeReference<>() {
                });
                assertThat(converted).as("converted").usingRecursiveComparison().isEqualTo(expected);
            }
        }
    }
}
