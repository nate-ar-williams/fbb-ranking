package fantasybaseball.ranker.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import fantasybaseball.ranker.model.Position;

import java.io.IOException;

public class PositionDeserializer extends JsonDeserializer<Position> {
    @Override
    public Position deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        ObjectCodec oc = p.getCodec();
        JsonNode node = oc.readTree(p);

        if (node == null) {
            return null;
        }

        String text = node.textValue();

        if (text == null) {
            return null;
        }

        return Position.fromString(text);
    }
}
