package com.bms.enterpriseconfiguration.configuration;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.bms.enterpriseconfiguration.core.AbstractResolver;
import com.bms.enterpriseconfiguration.resources.AbstractResource;
import com.bms.enterpriseconfiguration.resources.ResourceProvider;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class CombinedConfigurationDescriptorResolver<T extends AbstractResource> extends AbstractResolver<Set<? extends ResourceProvider<Set<T>>>, Set<CombinedConfigurationDescriptor<T>>> {

	@Override
	protected Set<CombinedConfigurationDescriptor<T>> doResolution(Set<? extends ResourceProvider<Set<T>>> input) {
		Set<CombinedConfigurationDescriptor<T>> combinedConfigurationDescriptors = Sets.newHashSet();
		Map<String, Set<T>> allResourcesByName = arrangeResourcesByName(input);
		
		for(Entry<String, Set<T>> resourceSetEntry : allResourcesByName.entrySet()){
			CombinedConfigurationDescriptor<T> combinedConfigurationDescriptor = new CombinedConfigurationDescriptor<T>(resourceSetEntry.getKey());
			combinedConfigurationDescriptor.getResources().addAll(resourceSetEntry.getValue());
			combinedConfigurationDescriptors.add(combinedConfigurationDescriptor);
		}
		
		return combinedConfigurationDescriptors;
	}
	
	private Map<String, Set<T>> arrangeResourcesByName(Set<? extends ResourceProvider<Set<T>>> input){
		Map<String, Set<T>> allResourcesByName = Maps.newHashMap();
		
		for(ResourceProvider<Set<T>> resourceProvider : input){
			for(T resource : resourceProvider.getResources()){
				if(!allResourcesByName.containsKey(resource.getName())){
					allResourcesByName.put(resource.getName(), Sets.<T>newHashSet());
				}
				allResourcesByName.get(resource.getName()).add(resource);
			}
		}
		
		return allResourcesByName;
	}

}
