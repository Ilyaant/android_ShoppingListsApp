package com.example.shoppinglistapp.Database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Converters {
    @TypeConverter
    public String fromList(List<String> list) {
        return list.stream().collect(Collectors.joining(","));
    }

    @TypeConverter
    public List<String> toList(String data) {
        return new ArrayList<>(Arrays.asList(data.split(",")));
    }
}
