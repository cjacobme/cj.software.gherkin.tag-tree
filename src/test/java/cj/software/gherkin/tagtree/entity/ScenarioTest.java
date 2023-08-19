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
}