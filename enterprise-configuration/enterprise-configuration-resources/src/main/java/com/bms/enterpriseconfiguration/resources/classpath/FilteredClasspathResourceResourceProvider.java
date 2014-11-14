package com.bms.enterpriseconfiguration.resources.classpath;

import java.util.Set;

import com.bms.enterpriseconfiguration.resources.ResourceProvider;
import com.bms.enterpriseconfiguration.resources.classpath.filter.ResourceFilter;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

public class FilteredClasspathResourceResourceProvider implements ResourceProvider<Set<ClasspathResource>> {

	private ClasspathResourceResourceProvider classpathResourceSource;
	private Set<ResourceFilter> resourceFilters;
	private int order;
	
	public FilteredClasspathResourceResourceProvider(ClasspathResourceResourceProvider classpathResourceSource, int order) {
		this.classpathResourceSource = classpathResourceSource;
		this.order = order;
	}
	
	public FilteredClasspathResourceResourceProvider() {
		super();
	}

	public ClasspathResourceResourceProvider getClasspathResourceSource() {
		return classpathResourceSource;
	}

	public void setClasspathResourceSource(ClasspathResourceResourceProvider classpathResourceSource) {
		this.classpathResourceSource = classpathResourceSource;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	public Set<ResourceFilter> getResourceFilters() {
		if(resourceFilters == null){
			resourceFilters = Sets.newHashSet();
		}
		return resourceFilters;
	}
	
	public boolean add(ResourceFilter resourceFilter) {
		return getResourceFilters().add(resourceFilter);
	}

	@Override
	public Set<ClasspathResource> getResources() {
		return Sets.filter(classpathResourceSource.getResources(), new Predicate<ClasspathResource>(){
			@Override
			public boolean apply(ClasspathResource classpathResource) {
				boolean accept = true;
				for(ResourceFilter filter : getResourceFilters()){
					accept = accept && filter.accept(classpathResource.getResourceName());
				}
				return accept;
			}
		});
	}
	
}
