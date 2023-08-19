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

class TagTreeFeatureTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = TagTreeFeature.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }

    @Test
    void builderIsXmlTransient() {
        XmlTransient annotation = TagTreeFeature.Builder.class.getAnnotation(XmlTransient.class);
        assertThat(annotation).as("Builder XmlTransient").isNotNull();
    }


    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        TagTreeFeature.Builder builder = TagTreeFeature.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                TagTreeFeature.class);

        TagTreeFeature instance = builder.build();
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
        TagTreeFeature instance = TagTreeFeature.builder()
                .withName(name)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getName()).as("name").isEqualTo(name);
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        TagTreeFeature instance = new TagTreeFeatureBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<TagTreeFeature>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }

    @Test
    void stringPresentation() {
        TagTreeFeature instance = new TagTreeFeatureBuilder().build();
        String asString = instance.toString();
        assertThat(asString).as("String presentation").isEqualTo("TagTreeFeature[sample feature]");
    }

    @Test
    void equalObjects() {
        TagTreeFeature tagTreeFeature1 = new TagTreeFeatureBuilder().build();
        TagTreeFeature tagTreeFeature2 = new TagTreeFeatureBuilder().build();
        assertThat(tagTreeFeature1).isEqualTo(tagTreeFeature2);
    }

    @Test
    void twoEqualHashes() {
        TagTreeFeature instance1 = new TagTreeFeatureBuilder().build();
        TagTreeFeature instance2 = new TagTreeFeatureBuilder().build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void unequalNames() {
        TagTreeFeature instance1 = new TagTreeFeatureBuilder().build();
        TagTreeFeature instance2 = new TagTreeFeatureBuilder().withName("unequal name").build();
        assertThat(instance1).isNotEqualTo(instance2);
    }

    @Test
    void unequalNameHashes() {
        TagTreeFeature instance1 = new TagTreeFeatureBuilder().build();
        TagTreeFeature instance2 = new TagTreeFeatureBuilder().withName("unequal name").build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    @SuppressWarnings("squid:S5845")    // I want to test different object type
    void otherObjectType() {
        TagTreeFeature instance1 = new TagTreeFeatureBuilder().build();
        assertThat(instance1).isNotEqualTo(false);
    }

    @Test
    void addScenarios() {
        TagTreeScenario tagTreeScenario1 = new TagTreeScenarioBuilder().build();
        TagTreeScenario tagTreeScenario2 = new TagTreeScenarioBuilder().withName("1").build();
        TagTreeFeature instance = new TagTreeFeatureBuilder().build();
        instance.addScenario(tagTreeScenario1);
        instance.addScenario(tagTreeScenario2);
        instance.addScenario(tagTreeScenario1);
        SortedSet<TagTreeScenario> tagTreeScenarios = instance.getScenarios();
        assertThat(tagTreeScenarios).extracting("name").containsExactly("1", "sample scenario");
    }

    @Test
    void sort() {
        TagTreeFeature tagTreeFeature1 = new TagTreeFeatureBuilder().withName("1").build();
        TagTreeFeature tagTreeFeature4 = new TagTreeFeatureBuilder().withName("4").build();
        TagTreeFeature tagTreeFeature2 = new TagTreeFeatureBuilder().withName("2").build();
        List<TagTreeFeature> list = new ArrayList<>();
        list.add(tagTreeFeature1);
        list.add(tagTreeFeature4);
        list.add(tagTreeFeature2);
        Collections.sort(list);
        assertThat(list).extracting("name").containsExactly("1", "2", "4");
    }
}