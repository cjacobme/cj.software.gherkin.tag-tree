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

class FeatureTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = Feature.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }

    @Test
    void builderIsXmlTransient() {
        XmlTransient annotation = Feature.Builder.class.getAnnotation(XmlTransient.class);
        assertThat(annotation).as("Builder XmlTransient").isNotNull();
    }


    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        Feature.Builder builder = Feature.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                Feature.class);

        Feature instance = builder.build();
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
        Feature instance = Feature.builder()
                .withName(name)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getName()).as("name").isEqualTo(name);
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        Feature instance = new FeatureBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Feature>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }

    @Test
    void stringPresentation() {
        Feature instance = new FeatureBuilder().build();
        String asString = instance.toString();
        assertThat(asString).as("String presentation").isEqualTo("Feature[sample feature]");
    }

    @Test
    void equalObjects() {
        Feature feature1 = new FeatureBuilder().build();
        Feature feature2 = new FeatureBuilder().build();
        assertThat(feature1).isEqualTo(feature2);
    }

    @Test
    void twoEqualHashes() {
        Feature instance1 = new FeatureBuilder().build();
        Feature instance2 = new FeatureBuilder().build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void unequalNames() {
        Feature instance1 = new FeatureBuilder().build();
        Feature instance2 = new FeatureBuilder().withName("unequal name").build();
        assertThat(instance1).isNotEqualTo(instance2);
    }

    @Test
    void unequalNameHashes() {
        Feature instance1 = new FeatureBuilder().build();
        Feature instance2 = new FeatureBuilder().withName("unequal name").build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    @SuppressWarnings("squid:S5845")    // I want to test different object type
    void otherObjectType() {
        Feature instance1 = new FeatureBuilder().build();
        assertThat(instance1).isNotEqualTo(false);
    }

    @Test
    void addScenarios() {
        Scenario scenario1 = new ScenarioBuilder().build();
        Scenario scenario2 = new ScenarioBuilder().withName("1").build();
        Feature instance = new FeatureBuilder().build();
        instance.addScenario(scenario1);
        instance.addScenario(scenario2);
        instance.addScenario(scenario1);
        SortedSet<Scenario> scenarios = instance.getScenarios();
        assertThat(scenarios).extracting("name").containsExactly("1", "sample scenario");
    }

    @Test
    void sort() {
        Feature feature1 = new FeatureBuilder().withName("1").build();
        Feature feature4 = new FeatureBuilder().withName("4").build();
        Feature feature2 = new FeatureBuilder().withName("2").build();
        List<Feature> list = new ArrayList<>();
        list.add(feature1);
        list.add(feature4);
        list.add(feature2);
        Collections.sort(list);
        assertThat(list).extracting("name").containsExactly("1", "2", "4");
    }
}