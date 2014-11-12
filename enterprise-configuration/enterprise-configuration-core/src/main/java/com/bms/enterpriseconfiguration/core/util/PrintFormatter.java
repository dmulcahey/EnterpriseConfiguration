package com.bms.enterpriseconfiguration.core.util;

import java.lang.reflect.Type;

import com.bms.enterpriseconfiguration.core.resource.ResourceInfoCollection;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PrintFormatter {

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
	
	private PrintFormatter(){};
	
	/**
	 * @param object
	 * @return
	 */
	public static String format(final Object object){
		return GSON_INSTANCE.toJson(object);
	}
	
}
