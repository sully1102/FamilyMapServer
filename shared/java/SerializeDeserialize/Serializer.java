package SerializeDeserialize;

import XPOJOS.Request.LoginRequest;
import XPOJOS.Request.RegisterRequest;
import XPOJOS.Response.*;
import com.google.gson.Gson;

public class Serializer {

    public static String serialize(ClearResponse returnType) {
        return (new Gson()).toJson(returnType);
    }

    public static String serialize(EventAllResponse returnType) {
        return (new Gson()).toJson(returnType);
    }

    public static String serialize(EventIDResponse returnType) {
        return (new Gson()).toJson(returnType);
    }

    public static String serialize(FillResponse returnType) {
        return (new Gson()).toJson(returnType);
    }

    public static String serialize(LoadResponse returnType) {
        return (new Gson()).toJson(returnType);
    }

    public static String serialize(LoginRequest returnType) {
        return (new Gson()).toJson(returnType);
    }

    public static String serialize(LoginResponse returnType) {
        return (new Gson()).toJson(returnType);
    }

    public static String serialize(PersonIDResponse returnType) {
        return (new Gson()).toJson(returnType);
    }

    public static String serialize(PersonAllResponse returnType) {
        return (new Gson()).toJson(returnType);
    }

    public static String serialize(RegisterRequest returnType) {
        return (new Gson()).toJson(returnType);
    }

    public static String serialize(RegisterResponse returnType) {
        return (new Gson()).toJson(returnType);
    }
}
