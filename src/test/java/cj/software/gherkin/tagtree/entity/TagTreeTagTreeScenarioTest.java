package cj.software.gherkin.tagtree.entity;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TagTreeTagTreeScenarioTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = TagTreeScenario.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }

    @Test
    void builderIsXmlTransient() {
        XmlTransient annotation = TagTreeScenario.Builder.class.getAnnotation(XmlTransient.class);
        assertThat(annotation).as("Builder XmlTransient").isNotNull();
    }


    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        TagTreeScenario.Builder builder = TagTreeScenario.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                TagTreeScenario.class);

        TagTreeScenario instance = builder.build();
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
        TagTreeScenario instance = TagTreeScenario.builder()
                .withName(name)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getName()).as("name").isEqualTo(name);
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        TagTreeScenario instance = new TagTreeScenarioBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<TagTreeScenario>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }

    @Test
    void stringPresentation() {
        TagTreeScenario instance = new TagTreeScenarioBuilder().build();
        String asString = instance.toString();
        assertThat(asString).as("String presentation").isEqualTo("TagTreeScenario[sample scenario]");
    }

    @Test
    void sort() {
        TagTreeScenario tagTreeScenario1 = new TagTreeScenarioBuilder().withName("1").build();
        TagTreeScenario tagTreeScenario4 = new TagTreeScenarioBuilder().withName("4").build();
        TagTreeScenario tagTreeScenario2 = new TagTreeScenarioBuilder().withName("2").build();
        List<TagTreeScenario> list = new ArrayList<>();
        list.add(tagTreeScenario1);
        list.add(tagTreeScenario4);
        list.add(tagTreeScenario2);
        Collections.sort(list);
        assertThat(list).extracting("name").containsExactly("1", "2", "4");
    }

    @Test
    void twoEqualObjects() {
        TagTreeScenario instance1 = new TagTreeScenarioBuilder().build();
        TagTreeScenario instance2 = new TagTreeScenarioBuilder().build();
        assertThat(instance1).isEqualTo(instance2);
    }

    @Test
    void twoEqualHashes() {
        TagTreeScenario instance1 = new TagTreeScenarioBuilder().build();
        TagTreeScenario instance2 = new TagTreeScenarioBuilder().build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void unequalNames() {
        TagTreeScenario instance1 = new TagTreeScenarioBuilder().build();
        TagTreeScenario instance2 = new TagTreeScenarioBuilder().withName("unequal name").build();
        assertThat(instance1).isNotEqualTo(instance2);
    }

    @Test
    void unequalNameHashes() {
        TagTreeScenario instance1 = new TagTreeScenarioBuilder().build();
        TagTreeScenario instance2 = new TagTreeScenarioBuilder().withName("unequal name").build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    void otherObjectType() {
        TagTreeScenario instance1 = new TagTreeScenarioBuilder().build();
        Object instance2 = Boolean.FALSE;
        assertThat(instance1).isNotEqualTo(instance2);
    }
}