package com.bms.enterpriseconfiguration.resources.classpath.filter;

import java.util.Set;

import com.google.common.collect.Sets;

public class PathFilter implements ResourceFilter {

	private Set<String> paths;
	
	public PathFilter(String... pathsToInclude) {
		paths = Sets.newHashSet(pathsToInclude);
	}

	@Override
	public boolean accept(String resourcePath) {
		boolean accept = true;
		for(String path : paths){
			accept = accept && resourcePath.contains(path);
		}
		return accept;
	}

	@Override
	public String toString() {
		return "PathFilter [paths(" + paths + ")]";
	}

}
