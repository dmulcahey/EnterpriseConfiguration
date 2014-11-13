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
			.registerTypeHierarchyAdapter(ResourceInfoCollection.class, new JsonSerializer<ResourceInfoCollection>(){
				@Override
				public JsonElement serialize(ResourceInfoCollection src, Type typeOfSrc, JsonSerializationContext context) {
					JsonObject returnValue = new JsonObject();
					returnValue.addProperty("resourceLocatorProvider", src.getResourceLocatorProvider().toString());
					returnValue.add("resources", context.serialize(src.getResources()));
					return returnValue;
				}
			})
			.registerTypeAdapter(ResourceInfo.class, new JsonSerializer<ResourceInfo>(){
				@Override
				public JsonElement serialize(ResourceInfo src, Type typeOfSrc, JsonSerializationContext context) {
					JsonObject returnValue = new JsonObject();
					returnValue.addProperty("resourceName", src.getResourceName());
					returnValue.add("url", context.serialize(src.url().toExternalForm()));
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
