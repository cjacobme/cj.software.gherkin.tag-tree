package cj.software.gherkin.tagtree.entity;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.*;

import org.junit.jupiter.api.Test;

import java.io.Serializable;
import javax.validation.ConstraintViolation;
import javax.xml.bind.annotation.XmlTransient;

import org.assertj.core.api.SoftAssertions;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

class ParsedFeatureTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = ParsedFeature.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }

    @Test
    void builderIsXmlTransient() {
        XmlTransient annotation = ParsedFeature.Builder.class.getAnnotation(XmlTransient.class);
        assertThat(annotation).as("Builder XmlTransient").isNotNull();
    }


    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        ParsedFeature.Builder builder = ParsedFeature.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                ParsedFeature.class);

        ParsedFeature instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getName()).as("name").isNull();
        softy.assertAll();
    }

    @Test
    void constructFilled() {
        String name = "_name";
        ParsedFeature instance = ParsedFeature.builder()
                .withName(name)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getName()).as("name").isEqualTo(name);
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        ParsedFeature instance = new ParsedFeatureBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<ParsedFeature>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }

    @Test
    void stringPresentation() {
        ParsedFeature instance = new ParsedFeatureBuilder().build();
        String asString = instance.toString();
        assertThat(asString).as("String presentation").isEqualTo("ParsedFeature[sample feature]");
    }

    @Test
    void equalObjects() {
        ParsedFeature parsedFeature1 = new ParsedFeatureBuilder().build();
        ParsedFeature parsedFeature2 = new ParsedFeatureBuilder().build();
        assertThat(parsedFeature1).isEqualTo(parsedFeature2);
    }

    @Test
    void twoEqualHashes() {
        ParsedFeature instance1 = new ParsedFeatureBuilder().build();
        ParsedFeature instance2 = new ParsedFeatureBuilder().build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void unequalNames() {
        ParsedFeature instance1 = new ParsedFeatureBuilder().build();
        ParsedFeature instance2 = new ParsedFeatureBuilder().withName("unequal name").build();
        assertThat(instance1).isNotEqualTo(instance2);
    }

    @Test
    void unequalNameHashes() {
        ParsedFeature instance1 = new ParsedFeatureBuilder().build();
        ParsedFeature instance2 = new ParsedFeatureBuilder().withName("unequal name").build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    @SuppressWarnings("squid:S5845")    // I want to test different object type
    void otherObjectType() {
        ParsedFeature instance1 = new ParsedFeatureBuilder().build();
        assertThat(instance1).isNotEqualTo(false);
    }

    @Test
    void addScenarios() {
        ParsedScenario parsedScenario1 = new ParsedScenarioBuilder().build();
        ParsedScenario parsedScenario2 = new ParsedScenarioBuilder().withName("1").build();
        ParsedFeature instance = new ParsedFeatureBuilder().build();
        instance.addScenario(parsedScenario1);
        instance.addScenario(parsedScenario2);
        instance.addScenario(parsedScenario1);
        SortedSet<ParsedScenario> parsedScenarios = instance.getScenarios();
        assertThat(parsedScenarios).extracting("name").containsExactly("1", "sample scenario");
    }

    @Test
    void sort() {
        ParsedFeature parsedFeature1 = new ParsedFeatureBuilder().withName("1").build();
        ParsedFeature parsedFeature4 = new ParsedFeatureBuilder().withName("4").build();
        ParsedFeature parsedFeature2 = new ParsedFeatureBuilder().withName("2").build();
        List<ParsedFeature> list = new ArrayList<>();
        list.add(parsedFeature1);
        list.add(parsedFeature4);
        list.add(parsedFeature2);
        Collections.sort(list);
        assertThat(list).extracting("name").containsExactly("1", "2", "4");
    }
}