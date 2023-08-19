package cj.software.gherkin.tagtree.entity;

import org.apache.commons.lang3.builder.*;

import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serial;
import java.io.Serializable;

public class Coordinate implements Serializable, Comparable<Coordinate> {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String feature;

    @NotBlank
    private String scenario;

    private Coordinate() {
    }

    public String getFeature() {
        return feature;
    }

    public String getScenario() {
        return scenario;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("feature", feature)
                .append("scenario", scenario);
        String result = builder.build();
        return result;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder()
                .append(feature)
                .append(scenario);
        int result = builder.build();
        return result;
    }

    @Override
    public boolean equals(Object otherObject) {
        boolean result;
        if (otherObject instanceof Coordinate other) {
            EqualsBuilder builder = new EqualsBuilder()
                    .append(this.feature, other.feature)
                    .append(this.scenario, other.scenario);
            result = builder.build();
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public int compareTo(Coordinate other) {
        CompareToBuilder builder = new CompareToBuilder()
                .append(this.feature, other.feature)
                .append(this.scenario, other.scenario);
        int result = builder.build();
        return result;
    }

    public static Builder builder() {
        return new Builder();
    }

    @XmlTransient
    public static class Builder {
        protected Coordinate instance;

        protected Builder() {
            instance = new Coordinate();
        }

        public Coordinate build() {
            Coordinate result = instance;
            instance = null;
            return result;
        }

        public Builder withFeature(String feature) {
            instance.feature = feature;
            return this;
        }

        public Builder withScenario(String scenario) {
            instance.scenario = scenario;
            return this;
        }
    }
}