package fantasybaseball.ranker.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fantasybaseball.ranker.deserializers.PositionDeserializer;

import java.util.logging.Level;
import java.util.logging.Logger;

@JsonDeserialize(using = PositionDeserializer.class)
public enum Position {
    @JsonProperty("1B")
    FIRST("1B"),
    @JsonProperty("2B")
    SECOND("2B"),
    @JsonProperty("3B")
    THIRD("3B"),
    @JsonProperty("SS")
    SS("SS"),
    @JsonProperty("LF")
    LF("LF"),
    @JsonProperty("RF")
    RF("RF"),
    @JsonProperty("CF")
    CF("CF"),
    @JsonProperty("DH")
    DH("DH"),
    @JsonProperty("C")
    C("C"),
    @JsonProperty("RP")
    RP("RP"),
    @JsonProperty("SP")
    SP("SP");

    private final String name;

    Position(final String name) {
        this.name = name;
    }

    @JsonCreator
    public static Position fromString(final String str) {
        for (Position p : Position.values()) {
            Logger.getLogger("position").log(Level.SEVERE, "str=" + str + ", testing p=" + p);
            if (p.toString().equals(str)) {
                Logger.getLogger("position").log(Level.SEVERE, "FOUND MATCH! str=" + str + ", testing p=" + p);
                return p;
            }
        }
        Logger.getLogger("position").log(Level.SEVERE, "NO MATCH! str=" + str);
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        return this.name;
    }
}
