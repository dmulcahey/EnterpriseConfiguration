package com.bms.enterpriseconfiguration.configuration.combined;

import java.util.Properties;

import org.apache.commons.configuration2.ConfigurationConverter;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.NodeCombiner;
import org.apache.commons.configuration2.tree.OverrideCombiner;

import com.bms.enterpriseconfiguration.configuration.Configuration;
import com.bms.enterpriseconfiguration.configuration.ConfigurationDescriptor;
import com.bms.enterpriseconfiguration.configuration.combined.util.PropertiesConfigurationUtil;
import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;

public class CombinedClasspathPropertiesConfiguration extends org.apache.commons.configuration2.CombinedConfiguration implements Configuration<ClasspathResource> {
	
	private ConfigurationDescriptor<ClasspathResource> combinedConfigurationDescriptor;
	
	public CombinedClasspathPropertiesConfiguration() {
		this(new OverrideCombiner());
	}

	public CombinedClasspathPropertiesConfiguration(NodeCombiner nodeCombiner) {
		super(nodeCombiner);
		this.getInterpolator().setEnableSubstitutionInVariables(true);
	}

	@Override
	public ConfigurationDescriptor<ClasspathResource> getConfigurationDescriptor() {
		return combinedConfigurationDescriptor;
	}

	public void setCombinedConfigurationDescriptor(ConfigurationDescriptor<ClasspathResource> combinedConfigurationDescriptor) {
		this.combinedConfigurationDescriptor = combinedConfigurationDescriptor;
	}

	@Override
	public Properties getAsProperties() {
		return ConfigurationConverter.getProperties(this);
	}

	@Override
	public Properties getAsPrintableProperties() {
		CombinedClasspathPropertiesConfiguration clone = (CombinedClasspathPropertiesConfiguration) this.clone();
		clone.getInterpolator().setEnableSubstitutionInVariables(true);
		for(ClasspathResource resource : clone.getConfigurationDescriptor().getResources()){
			if(resource.isSecure()){
				try {
					PropertiesConfiguration propertiesConfiguration = PropertiesConfigurationUtil.buildPropertiesConfiguration(resource);
					Properties properties = ConfigurationConverter.getProperties(propertiesConfiguration);
					for(Object key : properties.keySet()){
						clone.setProperty((String)key, "[SECURED:VALUE_NOT_SHOWN]");
					}
				} catch (ConfigurationException e) {
					throw new RuntimeException("Unable to clone CombinedClasspathPropertiesConfiguration: " + getConfigurationDescriptor().getName(), e);
				}
			}
		}
		return ConfigurationConverter.getProperties(clone);
	}

	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append(combinedConfigurationDescriptor.toString());
		sb.append("\n\nProperties:\n");
		sb.append(getAsPrintableProperties().toString());
		sb.append("\n\n");
		return sb.toString();
	}
	
}
