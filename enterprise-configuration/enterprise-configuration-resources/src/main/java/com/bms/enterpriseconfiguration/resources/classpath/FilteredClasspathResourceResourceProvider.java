package com.bms.enterpriseconfiguration.resources.classpath;

import java.util.Set;

import com.bms.enterpriseconfiguration.resources.ResourceProvider;
import com.bms.enterpriseconfiguration.resources.classpath.filter.ResourceFilter;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ResourceInfo;

public class FilteredClasspathResourceResourceProvider implements ResourceProvider<Set<ClasspathResource>> {

	private static final ClassPath CLASSPATH;
	
	private Set<ResourceFilter> resourceFilters;
	private int order = 0;
	private boolean secure;
	
	static{
		try{
			CLASSPATH = ClassPath.from(Thread.currentThread().getContextClassLoader());
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public FilteredClasspathResourceResourceProvider() {
		super();
	}
	
	public FilteredClasspathResourceResourceProvider(int order) {
		this.order = order;
	}
	
	public FilteredClasspathResourceResourceProvider(int order, boolean secure) {
		super();
		this.order = order;
		this.secure = secure;
	}

	@Override
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
	public boolean isSecure() {
		return secure;
	}

	public void setSecure(boolean secure) {
		this.secure = secure;
	}

	@Override
	public Set<ClasspathResource> getResources() {
		Set<ResourceInfo> resources = Sets.filter(CLASSPATH.getResources(), new Predicate<ResourceInfo>(){
			@Override
			public boolean apply(ResourceInfo resourceInfo) {
				boolean accept = true;
				for(ResourceFilter filter : getResourceFilters()){
					accept = accept && filter.accept(resourceInfo.getResourceName());
				}
				return accept;
			}
		});
		Set<ClasspathResource> classpathResources = Sets.newHashSetWithExpectedSize(resources.size());
		for(ResourceInfo resourceInfo : resources){
			classpathResources.add(new ClasspathResource(resourceInfo, this));
		}
		return classpathResources;
	}
	
}
