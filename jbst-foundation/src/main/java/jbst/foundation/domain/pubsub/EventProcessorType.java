package jbst.foundation.domain.pubsub;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EventProcessorType {
    PUBLISHER("Pub"),
    SUBSCRIBER("Sub");

    private final String value;

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
