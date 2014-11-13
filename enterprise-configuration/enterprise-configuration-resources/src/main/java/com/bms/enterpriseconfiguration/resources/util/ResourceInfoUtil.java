package com.bms.enterpriseconfiguration.resources.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.VFS;

import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath.ResourceInfo;

public class ResourceInfoUtil {

	public static Collection<String> getSubdirectoryNamesFromResourceInfo(Collection<ResourceInfo> resources, String parentDirectoryName) throws IOException{
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
	
	public static String getSimpleName(ResourceInfo resourceInfo){
		return resourceInfo.getResourceName().substring(resourceInfo.getResourceName().lastIndexOf("/") + 1);
	}
	
	public static boolean isDirectory(ResourceInfo resourceInfo) throws FileSystemException{
		return VFS.getManager().resolveFile(resourceInfo.url().toExternalForm()).getType().equals(FileType.FOLDER);
	}
	
}
