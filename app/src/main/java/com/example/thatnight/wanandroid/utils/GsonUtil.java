package com.example.thatnight.wanandroid.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ThatNight on 2017.5.13.
 */

public class GsonUtil {
    private static Gson sGson;
    private static GsonBuilder sBuilder = null;

    static {
        if (sGson == null) {
            synchronized (GsonUtil.class) {
                if (sGson == null) {
                    sGson = new Gson();
                }
            }
        }
    }

    public static Gson getGson() {
        if (sGson != null) {
            return sGson;
        }
        return new Gson();
    }

    public static Gson getGsonHasBuilder() {
        Gson gson = null;
        if (sBuilder == null) {
            sBuilder = new GsonBuilder();
        }
        gson = sBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson;
    }

    /**
     * 转成json
     *
     * @param o
     * @return
     */
    public static String gsonToJson(Object o) {
        String json = null;
        if (sGson != null) {
            try {
                json = sGson.toJson(o);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return json;
    }

    public static <T> String gsonToJsonArray(List<T> objectList) {
        String json = "[";
        try {
            for (int i = 0; i < objectList.size(); i++) {
                if (i != objectList.size() - 1) {
                    json += sGson.toJson(objectList.get(i)) + ",";
                } else {
                    json += sGson.toJson(objectList.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json + "]";
    }

    /**
     * 转成bean类
     *
     * @param response
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> T gsonToBean(String response, Class<T> bean) {
        T t = null;
        if (sGson != null) {
            try {
                t = sGson.fromJson(response, bean);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    /**
     * 转成list
     *
     * @param response
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> gsonToList(String response, Class<T> cls) {
        List<T> list = new ArrayList<>();
        if (sGson != null) {
            try {
                JsonArray array = new JsonParser().parse(response).getAsJsonArray();
                for (JsonElement element : array) {
                    list.add(sGson.fromJson(element, cls));
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static <T> Set<T> gsonToSet(String set, Class<T> cls) {
        Set<T> hashSet = new HashSet<>();
        if (sGson != null) {
            try {
                hashSet = sGson.fromJson(set, new TypeToken<HashSet<T>>() {
                }.getType());
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return hashSet;
    }

    /**
     * 转成list中有map的
     *
     * @param response
     * @param <T>
     * @return
     */
    public static <T> List<Map<String, T>> gsonToListMaps(String response) {
        List<Map<String, T>> list = null;
        if (sGson != null) {
            try {
                list = sGson.fromJson(response, new TypeToken<List<Map<String, T>>>() {
                }.getType());
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 转成map
     *
     * @param response
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> gsonToMaps(String response) {
        Map<String, T> map = null;
        if (sGson != null) {
            try {
                map = sGson.fromJson(response, new TypeToken<Map<String, T>>() {
                }.getType());
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static Gson getGsonHasTypeAdapterBuilder() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<Map<String, Object>>() {
                }.getType(), new JsonDeserializer<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        Map<String, Object> map = new HashMap<>();
                        JsonObject jsonObject = json.getAsJsonObject();
                        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                        for (Map.Entry<String, JsonElement> stringJsonElementEntry : entrySet) {
                            map.put(stringJsonElementEntry.getKey(), stringJsonElementEntry.getValue());
                        }
                        return map;
                    }
                }).create();
        return gson;
    }

}
