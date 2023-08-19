package cj.software.gherkin.tagtree.entity;

import org.apache.commons.lang3.builder.*;

import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serial;
import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

public class ParsedScenario implements Serializable, Comparable<ParsedScenario> {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String name;

    private final SortedSet<String> tags = new TreeSet<>();

    private ParsedScenario() {
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
    public int compareTo(ParsedScenario other) {
        CompareToBuilder builder = new CompareToBuilder()
                .append(this.name, other.name);
        int result = builder.build();
        return result;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder =new HashCodeBuilder().append(name);
        int result = builder.build();
        return result;
    }

    @Override
    public boolean equals(Object otherObject) {
        boolean result;
        if (otherObject instanceof ParsedScenario other) {
            EqualsBuilder builder = new EqualsBuilder()
                    .append(name, other.name);
            result = builder.build();
        } else {
            result = false;
        }
        return result;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public SortedSet<String> getTags() {
        return tags;
    }

    @XmlTransient
    public static class Builder {
        protected ParsedScenario instance;

        protected Builder() {
            instance = new ParsedScenario();
        }

        public ParsedScenario build() {
            ParsedScenario result = instance;
            instance = null;
            return result;
        }

        public Builder withName(String name) {
            instance.name = name;
            return this;
        }
    }
}