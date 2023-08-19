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

class CoordinateTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = Coordinate.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }

    @Test
    void builderIsXmlTransient() {
        XmlTransient annotation = Coordinate.Builder.class.getAnnotation(XmlTransient.class);
        assertThat(annotation).as("Builder XmlTransient").isNotNull();
    }


    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        Coordinate.Builder builder = Coordinate.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                Coordinate.class);

        Coordinate instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getFeature()).as("feature").isNull();
        softy.assertThat(instance.getScenario()).as("scenario").isNull();
        softy.assertAll();
    }

    @Test
    void constructFilled() {
        String feature = "_feature";
        String scenario = "_scenario";
        Coordinate instance = Coordinate.builder()
                .withFeature(feature)
                .withScenario(scenario)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getFeature()).as("feature").isEqualTo(feature);
        softy.assertThat(instance.getScenario()).as("scenario").isEqualTo(scenario);
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        Coordinate instance = new CoordinateBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Coordinate>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }

    @Test
    void stringPresentation() {
        Coordinate instance = new CoordinateBuilder().build();
        String asString = instance.toString();
        assertThat(asString).isEqualTo("Coordinate[feature=sample feature,scenario=sample scenario in sample feature]");
    }

    @Test
    void twoEquals() {
        Coordinate instance1 = new CoordinateBuilder().build();
        Coordinate instance2 = new CoordinateBuilder().build();
        assertThat(instance1).isEqualTo(instance2);
    }

    @Test
    void twoEqualHashes() {
        Coordinate instance1 = new CoordinateBuilder().build();
        Coordinate instance2 = new CoordinateBuilder().build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void unequalFeature() {
        Coordinate instance1 = new CoordinateBuilder().build();
        Coordinate instance2 = new CoordinateBuilder().withFeature("unequal").build();
        assertThat(instance1).isNotEqualTo(instance2);
    }

    @Test
    void unequalFeatureHashes() {
        Coordinate instance1 = new CoordinateBuilder().build();
        Coordinate instance2 = new CoordinateBuilder().withFeature("unequal").build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    void unequalScenario() {
        Coordinate instance1 = new CoordinateBuilder().build();
        Coordinate instance2 = new CoordinateBuilder().withScenario("unequal").build();
        assertThat(instance1).isNotEqualTo(instance2);
    }

    @Test
    void unequalScenarioHashes() {
        Coordinate instance1 = new CoordinateBuilder().build();
        Coordinate instance2 = new CoordinateBuilder().withScenario("unequal").build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    @SuppressWarnings("squid:S5845")    // I want to test different object type
    void otherObject() {
        Coordinate instance1 = new CoordinateBuilder().build();
        Object instance2 = "oops";
        assertThat(instance1).isNotEqualTo(instance2);
    }

    @Test
    void sort() {
        List<Coordinate> list = new ArrayList<>(Arrays.asList(
                Coordinate.builder().withFeature("f2").withScenario("s3").build(),
                Coordinate.builder().withFeature("f2").withScenario("s1").build(),
                Coordinate.builder().withFeature("f3").withScenario("s1").build(),
                Coordinate.builder().withFeature("f3").withScenario("s2").build(),
                Coordinate.builder().withFeature("f1").withScenario("s3").build()));
        Collections.sort(list);
        assertThat(list).as("list").extracting("feature", "scenario").containsExactly(
                tuple("f1", "s3"),
                tuple("f2", "s1"),
                tuple("f2", "s3"),
                tuple("f3", "s1"),
                tuple("f3", "s2"));
    }
}