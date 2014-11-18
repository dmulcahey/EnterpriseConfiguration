package com.bms.enterpriseconfiguration.configuration.properties;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration2.ex.ConfigurationException;

import com.bms.enterpriseconfiguration.configuration.properties.util.PropertiesConfigurationUtil;
import com.bms.enterpriseconfiguration.core.ResolutionActivity;
import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;
import com.google.common.collect.Maps;

public class CombinedClasspathPropertiesConfigurationImportHandler implements ResolutionActivity<Set<CombinedClasspathPropertiesConfiguration>> {

	@Override
	public void perform(Set<CombinedClasspathPropertiesConfiguration> input) {
		Map<String, CombinedClasspathPropertiesConfiguration> configurationsByName = Maps.newHashMap();
		for(CombinedClasspathPropertiesConfiguration configuration : input){
			configurationsByName.put(configuration.getConfigurationDescriptor().getName(), configuration);
		}
		for(CombinedClasspathPropertiesConfiguration configuration : input){
			for(ClasspathResource resource : configuration.getConfigurationDescriptor().getResources()){
				try {
					List<String> configurationsToImport = PropertiesConfigurationUtil.buildPropertiesConfiguration(resource).getList(String.class, "importConfiguration", Collections.<String>emptyList());
					if(!configurationsToImport.isEmpty()){
						for(String configurationToImport : configurationsToImport){
							configuration.importConfiguration(configurationsByName.get(configurationToImport));
						}
					}
				} catch (ConfigurationException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

}
