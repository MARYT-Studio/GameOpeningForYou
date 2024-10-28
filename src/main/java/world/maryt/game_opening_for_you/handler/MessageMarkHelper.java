package world.maryt.game_opening_for_you.handler;

import com.google.gson.*;
import net.minecraft.util.text.ITextComponent;
import world.maryt.game_opening_for_you.GameOpeningForYou;
import static world.maryt.game_opening_for_you.GameOpeningForYou.DEBUG;

public class MessageMarkHelper {
    private static final JsonParser jsonParser = new JsonParser();
//    private static final JsonElement MARKER = parser.parse(String.format("{\"text\" : \"[%s]\",\"color\" : \"gold\"}", GameOpeningForYou.MOD_NAME));
    private static final String MARKER_TEXT = String.format("[%s]", GameOpeningForYou.MOD_NAME);
    private static final ITextComponent MARKER = ITextComponent.Serializer.jsonToComponent(
        String.format("{\"text\" : \"%s\" , \"color\" : \"gold\"}", MARKER_TEXT)
    );

    public static ITextComponent markMessage(ITextComponent message) {
        if (MARKER != null) {
            return message.appendSibling(MARKER);
        }
        GameOpeningForYou.LOGGER.error("MARKER is null, mark-adding fails.");
        return message;
    }

    public static boolean messageNotMarked(ITextComponent message) {
        return !message.getSiblings().contains(MARKER);
    }

    public static ITextComponent removeMessageMark(ITextComponent message) {
        if (messageNotMarked(message)) return message;
        String messageJson = ITextComponent.Serializer.componentToJson(message);
        try {
            JsonElement jsonElement = jsonParser.parse(messageJson);

            if (DEBUG) {
                GameOpeningForYou.LOGGER.info("message {} is JsonObj: {}", messageJson, jsonElement.isJsonObject());
                GameOpeningForYou.LOGGER.info("message {} is JsonArray: {}", messageJson, jsonElement.isJsonArray());
            }

            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (jsonObject.has("extra") && jsonObject.get("extra").isJsonArray()) {

                    JsonArray clearedExtraArray = removeMarkFromExtraArray(jsonObject.get("extra").getAsJsonArray());

                    jsonObject.remove("extra");
                    // Re-add the remaining other extras, only the mark is removed
                    if (clearedExtraArray.size() > 0) jsonObject.add("extra", clearedExtraArray);

                    return ITextComponent.Serializer.jsonToComponent(jsonObject.toString());
                }
            }

            return message;
        } catch (JsonSyntaxException e) {
            GameOpeningForYou.LOGGER.error("Failed to remove mark message", e);
        }
        return message;
    }

    // Tool function: Remove mark from "extra" JsonArray
    private static JsonArray removeMarkFromExtraArray(JsonArray extraArray) {
        while(extraArray.iterator().hasNext()){
            JsonElement next = extraArray.iterator().next();
            if (next.isJsonObject()) {
                JsonObject nextJsonObject = next.getAsJsonObject();
                if (nextJsonObject.has("text") &&
                        nextJsonObject.get("text").isJsonPrimitive() &&
                        nextJsonObject.get("text").getAsString().equals(MARKER_TEXT)) {
                    extraArray.remove(nextJsonObject);
                }
            }
        }
        return extraArray;
    }
}
