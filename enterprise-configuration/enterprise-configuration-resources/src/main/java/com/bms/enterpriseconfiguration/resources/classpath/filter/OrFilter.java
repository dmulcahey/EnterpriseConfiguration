package com.bms.enterpriseconfiguration.resources.classpath.filter;

import java.util.Collections;
import java.util.Set;

import com.google.common.collect.Sets;

public class OrFilter implements ResourceFilter {
	
	private Set<ResourceFilter> resourceFilters;
	
	public OrFilter(){
		super();
	}
	
	public OrFilter(ResourceFilter... filters){
		super();
		Collections.addAll(getResourceFilters(), filters);
	}

	@Override
	public boolean accept(String resourcePath) {
		boolean accept = false;
		
		for(ResourceFilter filter : getResourceFilters()){
			accept = accept || filter.accept(resourcePath);
			//short circuit out once one is true
			if(accept){
				return true;
			}
		}
		
		return accept;
	}

	public Set<ResourceFilter> getResourceFilters() {
		if(resourceFilters == null){
			resourceFilters = Sets.newHashSet();
		}
		return resourceFilters;
	}

}
