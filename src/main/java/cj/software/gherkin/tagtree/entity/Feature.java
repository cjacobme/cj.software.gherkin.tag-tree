package cj.software.gherkin.tagtree.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serial;
import java.io.Serializable;

public class Feature implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String name;

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

    public static Builder builder() {
        return new Builder();
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