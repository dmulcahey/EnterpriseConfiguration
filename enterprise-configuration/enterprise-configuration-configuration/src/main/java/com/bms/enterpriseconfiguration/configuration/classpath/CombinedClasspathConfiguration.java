package com.bms.enterpriseconfiguration.configuration.classpath;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.configuration2.ConfigurationConverter;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.interpol.Lookup;
import org.apache.commons.configuration2.tree.NodeCombiner;
import org.apache.commons.configuration2.tree.OverrideCombiner;

import com.bms.enterpriseconfiguration.configuration.Configuration;
import com.bms.enterpriseconfiguration.configuration.ConfigurationDescriptor;
import com.bms.enterpriseconfiguration.configuration.FileBasedConfiguration;
import com.bms.enterpriseconfiguration.configuration.classpath.util.CommonsConfigurationUtil;
import com.bms.enterpriseconfiguration.configuration.lookup.DecryptionLookup;
import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class CombinedClasspathConfiguration extends org.apache.commons.configuration2.CombinedConfiguration implements FileBasedConfiguration<ClasspathResource> {
	
	private ConfigurationDescriptor<ClasspathResource> combinedConfigurationDescriptor;
	private Set<String> importedConfigurations = Sets.newHashSet();
	private static final Map<String, Lookup> LOOKUPS = Maps.newHashMap();
	
	static{
		LOOKUPS.put("decrypt", new DecryptionLookup());
	}
	
	public CombinedClasspathConfiguration() {
		this(new OverrideCombiner());
		this.setPrefixLookups(LOOKUPS);
		this.getInterpolator().setEnableSubstitutionInVariables(true);
	}

	public CombinedClasspathConfiguration(NodeCombiner nodeCombiner) {
		super(nodeCombiner);
		this.setPrefixLookups(LOOKUPS);
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
		CombinedClasspathConfiguration clone = (CombinedClasspathConfiguration) this.clone();
		clone.getInterpolator().setEnableSubstitutionInVariables(true);
		for(ClasspathResource resource : clone.getConfigurationDescriptor().getResources()){
			if(resource.isSecure()){
				try {
					org.apache.commons.configuration2.Configuration configuration = CommonsConfigurationUtil.buildConfiguration(resource);
					Properties properties = ConfigurationConverter.getProperties(configuration);
					for(Object key : properties.keySet()){
						clone.setProperty((String)key, "[SECURED:VALUE_NOT_SHOWN]");
					}
				} catch (ConfigurationException e) {
					throw new RuntimeException("Unable to clone CombinedClasspathConfiguration: " + getConfigurationDescriptor().getName(), e);
				}
			}
		}
		return ConfigurationConverter.getProperties(clone);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append(combinedConfigurationDescriptor.toString());
		if(!importedConfigurations.isEmpty()){
		sb.append("\n\nImported Configurations:\n");
			for(String importedConfiguration : importedConfigurations){
				sb.append(((Configuration<ClasspathResource>)this.getConfiguration(importedConfiguration)).getConfigurationDescriptor().toString());
			}
		}
		sb.append("\n\nProperties:\n");
		sb.append(getAsPrintableProperties().toString());
		sb.append("\n\n");
		return sb.toString();
	}
	
	void importConfiguration(CombinedClasspathConfiguration configuration){
		importedConfigurations.add(configuration.getConfigurationDescriptor().getName());
		this.addConfiguration(configuration, configuration.getConfigurationDescriptor().getName());
	}
	
}
