package com.bms.enterpriseconfiguration.resources.classpath;

import java.net.URL;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.VFS;

import com.bms.enterpriseconfiguration.resources.AbstractPrintableResource;
import com.bms.enterpriseconfiguration.resources.ResourceProvider;
import com.google.common.reflect.ClassPath.ResourceInfo;

public class ClasspathResource extends AbstractPrintableResource {
	
	private ResourceInfo resourceInfo;
	private ResourceProvider<?> resourceProvider;
	private boolean secure = false;
	
	public ClasspathResource(ResourceInfo resourceInfo) {
		super();
		this.resourceInfo = resourceInfo;
	}
	
	public ClasspathResource(ResourceInfo resourceInfo, boolean isSecure) {
		super();
		this.resourceInfo = resourceInfo;
		this.secure = isSecure;
	}
	
	public ClasspathResource(ResourceInfo resourceInfo, boolean secure, ResourceProvider<?> resourceProvider) {
		super();
		this.resourceInfo = resourceInfo;
		this.resourceProvider = resourceProvider;
		this.secure = secure;
	}

	public ClasspathResource(ResourceInfo resourceInfo, ResourceProvider<?> resourceProvider) {
		super();
		this.resourceInfo = resourceInfo;
		this.resourceProvider = resourceProvider;
	}

	public ResourceInfo getResourceInfo() {
		return resourceInfo;
	}
	
	public final URL getURL() {
		return resourceInfo.url();
	}

	public final String getResourceName() {
		return resourceInfo.getResourceName();
	}

	public void setResourceInfo(ResourceInfo resourceInfo) {
		this.resourceInfo = resourceInfo;
	}
	
	public String getSimpleName(){
		return resourceInfo.getResourceName().substring(resourceInfo.getResourceName().lastIndexOf("/") + 1);
	}
	
	public boolean isDirectory() throws FileSystemException{
		return VFS.getManager().resolveFile(resourceInfo.url().toExternalForm()).getType().equals(FileType.FOLDER);
	}
	
	public boolean isFile() throws FileSystemException{
		return VFS.getManager().resolveFile(resourceInfo.url().toExternalForm()).getType().equals(FileType.FILE);
	}

	public boolean isSecure() {
		return secure;
	}

	public void setSecure(boolean secure) {
		this.secure = secure;
	}

	public ResourceProvider<?> getResourceProvider() {
		return resourceProvider;
	}

	public void setResourceProvider(ResourceProvider<?> resourceProvider) {
		this.resourceProvider = resourceProvider;
	}
	
}
