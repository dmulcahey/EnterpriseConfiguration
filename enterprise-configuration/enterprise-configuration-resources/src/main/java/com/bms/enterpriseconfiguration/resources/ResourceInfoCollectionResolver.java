package com.bms.enterpriseconfiguration.resources;

import java.util.Set;

import com.bms.enterpriseconfiguration.core.AbstractPrintable;
import com.bms.enterpriseconfiguration.core.AbstractResolver;
import com.bms.enterpriseconfiguration.resources.ResourceInfoCollectionResolver.ResourceDefinition;
import com.bms.enterpriseconfiguration.resources.filter.ResourceFilter;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ResourceInfo;

public class ResourceInfoCollectionResolver extends AbstractResolver<ResourceDefinition, ResourceInfoCollection>{

	@Override
	protected ResourceInfoCollection doResolution(final ResourceDefinition resourceDefinition) {
		ResourceInfoCollection resourceInfoCollection = new ResourceInfoCollection();
		resourceInfoCollection.setResourceLocatorProvider(resourceDefinition.getResourceLocatorProvider());
		resourceInfoCollection.getResources().addAll(Sets.filter(resourceDefinition.getResources(), new Predicate<ResourceInfo>(){
			@Override
			public boolean apply(ResourceInfo input) {
				boolean accept = true;
				for(ResourceFilter filter : resourceDefinition.getResourceLocatorProvider().getFilters()){
					accept = accept && filter.accept(input.getResourceName());
				}
				return accept;
			}
		}));
		return resourceInfoCollection;
	}
	
	public static class ResourceDefinition extends AbstractPrintable {
		
		public ResourceDefinition(){
		}
		
		public ResourceDefinition(ResourceLocatorProvider resourceLocatorProvider, ClassPath classPath, ResourceInfoCollection resourceInfoCollection) {
			this.setResourceLocatorProvider(resourceLocatorProvider);
			this.setClassPath(classPath);
			this.setResourceInfoCollection(resourceInfoCollection);
		}

		private ResourceLocatorProvider resourceLocatorProvider;
		private Optional<ClassPath> classPath;
		private Optional<ResourceInfoCollection> resourceInfoCollection;
		
		public ResourceLocatorProvider getResourceLocatorProvider() {
			return resourceLocatorProvider;
		}
		
		public void setResourceLocatorProvider(ResourceLocatorProvider resourceLocatorProvider) {
			this.resourceLocatorProvider = resourceLocatorProvider;
		}
		
		public Optional<ClassPath> getClassPath() {
			return classPath;
		}
		
		public void setClassPath(ClassPath classPath) {
			this.classPath = Optional.fromNullable(classPath);
		}

		public Optional<ResourceInfoCollection> getResourceInfoCollection() {
			return resourceInfoCollection;
		}

		public void setResourceInfoCollection(ResourceInfoCollection resourceInfoCollection) {
			this.resourceInfoCollection = Optional.fromNullable(resourceInfoCollection);
		}
		
		public Set<ResourceInfo> getResources(){
			if(this.getResourceInfoCollection().isPresent()){
				return this.getResourceInfoCollection().get().getResources();
			}
			return this.getClassPath().get().getResources();
		}
		
	}

}
