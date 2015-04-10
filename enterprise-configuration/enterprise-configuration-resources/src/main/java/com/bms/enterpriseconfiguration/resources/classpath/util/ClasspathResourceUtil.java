package com.bms.enterpriseconfiguration.resources.classpath.util;

import java.io.IOException;
import java.util.Set;

import com.bms.enterpriseconfiguration.resources.classpath.ClassPath.ResourceInfo;
import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;
import com.bms.enterpriseconfiguration.resources.classpath.FilteredClasspathResourceResourceProvider;
import com.bms.enterpriseconfiguration.resources.classpath.filter.PathFilter;
import com.bms.enterpriseconfiguration.resources.util.ResourceInfoUtil;
import com.google.common.collect.Sets;

public class ClasspathResourceUtil {

	public static Set<String> getSubdirectoryNamesFromParentDirectory(final String parentDirectoryName) throws IOException{
		FilteredClasspathResourceResourceProvider classpathResourceResourceProvider = FilteredClasspathResourceResourceProvider.builder()
			.order(100)
			.withResourceFilter(new PathFilter(parentDirectoryName))
			.build();
		Set<ClasspathResource> resources = classpathResourceResourceProvider.getResources();
		Set<ResourceInfo> resourceInfos = Sets.newHashSetWithExpectedSize(resources.size());
		for(ClasspathResource resource : resources){
			resourceInfos.add(resource.getResourceInfo());
		}
		return ResourceInfoUtil.getSubdirectoryNamesFromResourceInfo(resourceInfos, parentDirectoryName);
	}
}
