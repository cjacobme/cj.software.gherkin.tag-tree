package cj.software.gherkin.tagtree.entity;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class ParsedScenarioTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = ParsedScenario.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }


    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        ParsedScenario.Builder builder = ParsedScenario.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                ParsedScenario.class);

        ParsedScenario instance = builder.build();
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
        ParsedScenario instance = ParsedScenario.builder()
                .withName(name)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getName()).as("name").isEqualTo(name);
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        ParsedScenario instance = new ParsedScenarioBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<ParsedScenario>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }

    @Test
    void stringPresentation() {
        ParsedScenario instance = new ParsedScenarioBuilder().build();
        String asString = instance.toString();
        assertThat(asString).as("String presentation").isEqualTo("ParsedScenario[sample scenario]");
    }

    @Test
    void sort() {
        ParsedScenario parsedScenario1 = new ParsedScenarioBuilder().withName("1").build();
        ParsedScenario parsedScenario4 = new ParsedScenarioBuilder().withName("4").build();
        ParsedScenario parsedScenario2 = new ParsedScenarioBuilder().withName("2").build();
        List<ParsedScenario> list = new ArrayList<>();
        list.add(parsedScenario1);
        list.add(parsedScenario4);
        list.add(parsedScenario2);
        Collections.sort(list);
        assertThat(list).extracting("name").containsExactly("1", "2", "4");
    }

    @Test
    void twoEqualObjects() {
        ParsedScenario instance1 = new ParsedScenarioBuilder().build();
        ParsedScenario instance2 = new ParsedScenarioBuilder().build();
        assertThat(instance1).isEqualTo(instance2);
    }

    @Test
    void twoEqualHashes() {
        ParsedScenario instance1 = new ParsedScenarioBuilder().build();
        ParsedScenario instance2 = new ParsedScenarioBuilder().build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void unequalNames() {
        ParsedScenario instance1 = new ParsedScenarioBuilder().build();
        ParsedScenario instance2 = new ParsedScenarioBuilder().withName("unequal name").build();
        assertThat(instance1).isNotEqualTo(instance2);
    }

    @Test
    void unequalNameHashes() {
        ParsedScenario instance1 = new ParsedScenarioBuilder().build();
        ParsedScenario instance2 = new ParsedScenarioBuilder().withName("unequal name").build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    void otherObjectType() {
        ParsedScenario instance1 = new ParsedScenarioBuilder().build();
        Object instance2 = Boolean.FALSE;
        assertThat(instance1).isNotEqualTo(instance2);
    }

    @Test
    void addTags() {
        ParsedScenario instance = new ParsedScenarioBuilder().build();
        instance.addTag("tag2");
        instance.addTag("tag1");
        instance.addTag("tag75");
        instance.addTag("tag2");
        SortedSet<String> tags = instance.getTags();
        assertThat(tags).as("tags").containsExactly("tag1", "tag2", "tag75");
    }
}