package com.bms.enterpriseconfiguration.resources;

import java.lang.reflect.Type;

import com.google.common.reflect.ClassPath.ResourceInfo;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public abstract class AbstractPrintableResource {

	private static final Gson GSON_INSTANCE = new GsonBuilder().setPrettyPrinting()
			.registerTypeAdapter(ResourceInfo.class, new JsonSerializer<ResourceInfo>(){
				@Override
				public JsonElement serialize(ResourceInfo src, Type typeOfSrc, JsonSerializationContext context) {
					JsonObject returnValue = new JsonObject();
					returnValue.addProperty("resourceName", src.getResourceName());
					returnValue.add("url", context.serialize(src.url().toExternalForm()));
					return returnValue;
				}
			})
			.registerTypeHierarchyAdapter(ResourceProvider.class, new JsonSerializer<ResourceProvider<?>>(){
				@Override
				public JsonElement serialize(ResourceProvider<?> src, Type typeOfSrc, JsonSerializationContext context) {
					JsonObject returnValue = new JsonObject();
					returnValue.addProperty("order", src.getOrder());
					return returnValue;
				}
			})
			.addSerializationExclusionStrategy(new ExclusionStrategy(){
				@Override
				public boolean shouldSkipField(FieldAttributes f) {
					return ClassLoader.class.equals(f.getDeclaredClass());
				}
				@Override
				public boolean shouldSkipClass(Class<?> clazz) {
					return ClassLoader.class.equals(clazz);
				}
				
			})
			.create();
	
	@Override
	public String toString() {
		return GSON_INSTANCE.toJson(this);
	}
}
