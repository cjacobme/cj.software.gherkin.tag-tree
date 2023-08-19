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

class ScenarioTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = Scenario.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }

    @Test
    void builderIsXmlTransient() {
        XmlTransient annotation = Scenario.Builder.class.getAnnotation(XmlTransient.class);
        assertThat(annotation).as("Builder XmlTransient").isNotNull();
    }


    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        Scenario.Builder builder = Scenario.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                Scenario.class);

        Scenario instance = builder.build();
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
        Scenario instance = Scenario.builder()
                .withName(name)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getName()).as("name").isEqualTo(name);
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        Scenario instance = new ScenarioBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Scenario>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }

    @Test
    void stringPresentation() {
        Scenario instance = new ScenarioBuilder().build();
        String asString = instance.toString();
        assertThat(asString).as("String presentation").isEqualTo("Scenario[sample scenario]");
    }

    @Test
    void sort() {
        Scenario scenario1 = new ScenarioBuilder().withName("1").build();
        Scenario scenario4 = new ScenarioBuilder().withName("4").build();
        Scenario scenario2 = new ScenarioBuilder().withName("2").build();
        List<Scenario> list = new ArrayList<>();
        list.add(scenario1);
        list.add(scenario4);
        list.add(scenario2);
        Collections.sort(list);
        assertThat(list).extracting("name").containsExactly("1", "2", "4");
    }

    @Test
    void twoEqualObjects() {
        Scenario instance1 = new ScenarioBuilder().build();
        Scenario instance2 = new ScenarioBuilder().build();
        assertThat(instance1).isEqualTo(instance2);
    }

    @Test
    void twoEqualHashes() {
        Scenario instance1 = new ScenarioBuilder().build();
        Scenario instance2 = new ScenarioBuilder().build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void unequalNames() {
        Scenario instance1 = new ScenarioBuilder().build();
        Scenario instance2 = new ScenarioBuilder().withName("unequal name").build();
        assertThat(instance1).isNotEqualTo(instance2);
    }

    @Test
    void unequalNameHashes() {
        Scenario instance1 = new ScenarioBuilder().build();
        Scenario instance2 = new ScenarioBuilder().withName("unequal name").build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    void otherObjectType() {
        Scenario instance1 = new ScenarioBuilder().build();
        Object instance2 = Boolean.FALSE;
        assertThat(instance1).isNotEqualTo(instance2);
    }
}