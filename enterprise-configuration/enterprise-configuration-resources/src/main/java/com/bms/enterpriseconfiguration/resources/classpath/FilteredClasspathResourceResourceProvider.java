package com.bms.enterpriseconfiguration.resources.classpath;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

import lombok.extern.java.Log;

import com.bms.enterpriseconfiguration.resources.ResourceProvider;
import com.bms.enterpriseconfiguration.resources.classpath.ClassPath.ResourceInfo;
import com.bms.enterpriseconfiguration.resources.classpath.filter.ResourceFilter;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

@Log
public class FilteredClasspathResourceResourceProvider implements ResourceProvider<Set<ClasspathResource>> {

	private static final ClassPath CLASSPATH;
	private static final Class<?>[] PARAMETERS = new Class<?>[] {URL.class};
	
	private Set<ResourceFilter> resourceFilters;
	private int order = 0;
	private boolean secure;
	
	static{
		try{
			String classpathAugment = System.getProperty("cfgmgr.classpath");
			if(!Strings.isNullOrEmpty(classpathAugment)){
				String[] pathAugmentsToAdd = classpathAugment.split(File.pathSeparator);
				URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		        Class<URLClassLoader> sysclass = URLClassLoader.class;
		        Method method = null;
		        try {
		        	method = sysclass.getDeclaredMethod("addURL", PARAMETERS);
		        }catch(Exception e){
		        	throw new RuntimeException("Error, can not augment system classloader!", e);
		        }
	            method.setAccessible(true);
				for(String classpathAugmentToAdd : pathAugmentsToAdd){
					try {
			            method.invoke(sysloader, new Object[] {new File(classpathAugmentToAdd).toURI().toURL()});
			            log.info("Augmented runtime classpath: " + classpathAugmentToAdd + " was added to the System class loader...");
			        } catch (Throwable t) {
			            t.printStackTrace();
			            throw new RuntimeException("Error, could not add url to system classloader: " + classpathAugmentToAdd);
			        }
				}
			}
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
