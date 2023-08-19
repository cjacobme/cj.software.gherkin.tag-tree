package cj.software.gherkin.tagtree.entity;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.Set;

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
}