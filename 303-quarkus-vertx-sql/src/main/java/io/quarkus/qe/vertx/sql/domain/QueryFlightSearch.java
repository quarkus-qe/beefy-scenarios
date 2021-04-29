package io.quarkus.qe.vertx.sql.domain;

import java.util.Objects;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.vertx.core.json.Json;

@RegisterForReflection
public class QueryFlightSearch {
    @Min(0)
    public int adult;
    @Min(0)
    public int child;
    @Min(0)
    public int infant;
    @NotNull(message = "From must be not empty")
    public String from;
    @NotNull(message = "To must be not empty")
    public String to;
    @Min(0)
    public int daysToDeparture;

    public QueryFlightSearch() {
    }

    public QueryFlightSearch(Builder builder) {
        this.adult = builder.adult;
        this.child = builder.child;
        this.infant = builder.infant;
        this.from = builder.from;
        this.to = builder.to;
        this.daysToDeparture = builder.daysToDeparture;
    }

    public static class Builder {
        private int adult;
        private int child;
        private int infant;
        private String from;
        private String to;
        private int daysToDeparture;

        public static Builder newInstance() {
            return new Builder();
        }

        private Builder() {
        }

        public Builder withAdult(int adults) {
            this.adult = adults;
            return this;
        }

        public Builder withChild(int childs) {
            this.child = childs;
            return this;
        }

        public Builder withInfant(int infants) {
            this.infant = infants;
            return this;
        }

        public Builder withFrom(String from) {
            this.from = from;
            return this;
        }

        public Builder withTo(String to) {
            this.to = to;
            return this;
        }

        public Builder withDaysToDeparture(int daysToDeparture) {
            this.daysToDeparture = daysToDeparture;
            return this;
        }

        public QueryFlightSearch build() {
            return new QueryFlightSearch(this);
        }
    }

    public String toJsonStringify() {
        return Json.encode(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        QueryFlightSearch that = (QueryFlightSearch) o;
        return adult == that.adult &&
                child == that.child &&
                infant == that.infant &&
                daysToDeparture == that.daysToDeparture &&
                from.equals(that.from) &&
                to.equals(that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adult, child, infant, from, to, daysToDeparture);
    }
}
