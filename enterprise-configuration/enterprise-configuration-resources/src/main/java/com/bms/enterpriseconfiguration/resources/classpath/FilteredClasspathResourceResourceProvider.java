package com.bms.enterpriseconfiguration.resources.classpath;

import java.util.Set;

import com.bms.enterpriseconfiguration.resources.ResourceProvider;
import com.bms.enterpriseconfiguration.resources.classpath.filter.ResourceFilter;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableSet;
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
	
	public static Builder builder(){
		return new Builder();
	}

	@Override
	public int getOrder() {
		return order;
	}
	
	@Override
	public boolean isSecure() {
		return secure;
	}

	@Override
	public Set<ClasspathResource> getResources() {
		Set<ResourceInfo> resources = Sets.filter(CLASSPATH.getResources(), new Predicate<ResourceInfo>(){
			@Override
			public boolean apply(ResourceInfo resourceInfo) {
				boolean accept = true;
				for(ResourceFilter filter : resourceFilters){
					accept = accept && filter.accept(resourceInfo.getResourceName());
				}
				return accept;
			}
		});
		Set<ClasspathResource> classpathResources = Sets.newHashSetWithExpectedSize(resources.size());
		for(ResourceInfo resourceInfo : resources){
			classpathResources.add(new ClasspathResource(resourceInfo, this));
		}
		return ImmutableSet.copyOf(classpathResources);
	}
	
	public static class Builder {
		private int order = 0;
		private boolean secure;
		private Set<ResourceFilter> resourceFilters = Sets.newHashSet();
		
		private Builder(){
			
		}
		
		public Builder order(int order){
			this.order = order;
			return this;
		}
		
		public Builder secure(boolean secure){
			this.secure = secure;
			return this;
		}
		
		public Builder withResourceFilter(ResourceFilter filter){
			this.resourceFilters.add(filter);
			return this;
		}
		
		public FilteredClasspathResourceResourceProvider build(){
			return new FilteredClasspathResourceResourceProvider(this);
		}
		
	}
	
	private FilteredClasspathResourceResourceProvider(Builder builder){
		this.order = builder.order;
		this.secure = builder.secure;
		this.resourceFilters = ImmutableSet.copyOf(builder.resourceFilters);
	}
	
}
