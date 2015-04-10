package com.bms.enterpriseconfiguration.resources.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.VFS;

import com.bms.enterpriseconfiguration.resources.classpath.ClassPath.ResourceInfo;
import com.google.common.collect.Sets;

public class ResourceInfoUtil {

	public static Set<String> getSubdirectoryNamesFromResourceInfo(Collection<ResourceInfo> resources, String parentDirectoryName) throws IOException{
		Set<String> subdirectoryNames = Sets.newHashSet();
		Set<String> uniqueParrentDirectories = Sets.newHashSet();
		for(ResourceInfo resourceInfo : resources){
			String url = resourceInfo.url().toExternalForm();
			url = url.substring(0, url.indexOf(parentDirectoryName) + parentDirectoryName.length());
			uniqueParrentDirectories.add(url);
		}
		
		for(String uniqueParent : uniqueParrentDirectories){
			FileObject fileObject = VFS.getManager().resolveFile(uniqueParent);
			if(fileObject.getType().equals(FileType.FOLDER)){
				FileObject[] children = fileObject.getChildren();
				for(FileObject child : children){
					if(child.getType().equals(FileType.FOLDER)){
						subdirectoryNames.add(child.getName().getBaseName());
					}
				}
			}
		}
		
		return subdirectoryNames;
	}
	
}
