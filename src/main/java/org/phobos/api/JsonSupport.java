package org.phobos.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public interface JsonSupport {
   Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
}
