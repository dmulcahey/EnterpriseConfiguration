package com.bms.enterpriseconfiguration.core.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath.ResourceInfo;

public class ResourceInfoUtil {

	public static Collection<String> getSubdirectoryNamesFromResourceInfo(Collection<ResourceInfo> resources, String parentDirectoryName) throws IOException{
		Set<String> subdirectoryNames = Sets.newHashSet();
		String previousSegment = "";
		for(ResourceInfo resource : resources){
			String[] segments = resource.getResourceName().split("/");
			for(String segment : segments){
				if(previousSegment.equalsIgnoreCase(parentDirectoryName)){
					//make sure it isn't a "file"
					if(!segment.contains(".")){
						subdirectoryNames.add(segment);
					}
					previousSegment = "";
					break;
				}
				previousSegment = segment;
			}
		}
		return subdirectoryNames;
	}
	
	public static String getSimpleName(ResourceInfo resourceInfo){
		return resourceInfo.getResourceName().substring(resourceInfo.getResourceName().lastIndexOf("/") + 1);
	}
	
}
