package com.bms.enterpriseconfiguration.resources.classpath.filter;

import lombok.SneakyThrows;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.VFS;

public class DirectoryFilter implements ResourceFilter {

	@Override
	@SneakyThrows
	public boolean accept(String resourcePath) {
		FileObject fileObject = VFS.getManager().resolveFile(resourcePath);
		return fileObject.getType().equals(FileType.FOLDER);
	}

}
