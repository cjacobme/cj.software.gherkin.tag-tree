package cj.software.gherkin.tagtree.entity;

import org.apache.commons.lang3.builder.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

public class Feature implements Serializable, Comparable<Feature> {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String name;

    private final SortedSet<@Valid Scenario> scenarios = new TreeSet<>();

    private Feature() {
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(name);
        String result = builder.build();
        return result;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder()
                .append(name);
        int result = builder.build();
        return result;
    }

    @Override
    public boolean equals (Object otherObject) {
        boolean result;
        if (otherObject instanceof Feature other) {
            EqualsBuilder builder = new EqualsBuilder()
                    .append(name, other.name);
            result = builder.build();
        } else {
            result = false;
        }
        return result;
    }

    public void addScenario(Scenario scenario) {
        scenarios.add(scenario);
    }

    public static Builder builder() {
        return new Builder();
    }

    public SortedSet<Scenario> getScenarios() {
        return Collections.unmodifiableSortedSet(scenarios);
    }

    @Override
    public int compareTo(Feature other) {
        CompareToBuilder builder = new CompareToBuilder()
                .append(this.name, other.name);
        int result = builder.build();
        return result;
    }

    @XmlTransient
    public static class Builder {
        protected Feature instance;

        protected Builder() {
            instance = new Feature();
        }

        public Feature build() {
            Feature result = instance;
            instance = null;
            return result;
        }

        public Builder withName(String name) {
            instance.name = name;
            return this;
        }
    }
}