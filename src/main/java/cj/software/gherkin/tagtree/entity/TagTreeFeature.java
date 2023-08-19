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

public class TagTreeFeature implements Serializable, Comparable<TagTreeFeature> {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String name;

    private final SortedSet<@Valid TagTreeScenario> tagTreeScenarios = new TreeSet<>();

    private TagTreeFeature() {
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
        if (otherObject instanceof TagTreeFeature other) {
            EqualsBuilder builder = new EqualsBuilder()
                    .append(name, other.name);
            result = builder.build();
        } else {
            result = false;
        }
        return result;
    }

    public void addScenario(TagTreeScenario tagTreeScenario) {
        tagTreeScenarios.add(tagTreeScenario);
    }

    public static Builder builder() {
        return new Builder();
    }

    public SortedSet<TagTreeScenario> getScenarios() {
        return Collections.unmodifiableSortedSet(tagTreeScenarios);
    }

    @Override
    public int compareTo(TagTreeFeature other) {
        CompareToBuilder builder = new CompareToBuilder()
                .append(this.name, other.name);
        int result = builder.build();
        return result;
    }

    @XmlTransient
    public static class Builder {
        protected TagTreeFeature instance;

        protected Builder() {
            instance = new TagTreeFeature();
        }

        public TagTreeFeature build() {
            TagTreeFeature result = instance;
            instance = null;
            return result;
        }

        public Builder withName(String name) {
            instance.name = name;
            return this;
        }
    }
}