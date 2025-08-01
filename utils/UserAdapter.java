package utils;

import com.google.gson.*;
import models.*;

import java.lang.reflect.Type;

public class UserAdapter implements JsonSerializer<User>, JsonDeserializer<User> {

    @Override
    public JsonElement serialize(User user, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = context.serialize(user).getAsJsonObject();
        json.addProperty("type", user.getType());
        return json;
    }

    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        JsonElement typeElement = obj.get("role");
        if (typeElement == null || typeElement.isJsonNull()) {
            throw new JsonParseException("Missing 'type' field in user JSON");
        }

        String type = typeElement.getAsString();

        switch (type.toLowerCase()) {
            case "customer":
                return context.deserialize(obj, Customer.class);
            case "admin":
                return context.deserialize(obj, Admin.class);
            case "staff":
                return context.deserialize(obj, Staff.class);
            default:
                throw new JsonParseException("Unknown user type: " + type);
        }
    }
}
