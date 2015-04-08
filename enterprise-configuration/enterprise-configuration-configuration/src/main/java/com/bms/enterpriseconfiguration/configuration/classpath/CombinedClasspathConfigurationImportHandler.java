package com.bms.enterpriseconfiguration.configuration.classpath;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration2.ex.ConfigurationException;

import com.bms.enterpriseconfiguration.configuration.classpath.util.CommonsConfigurationUtil;
import com.bms.enterpriseconfiguration.core.ResolutionActivity;
import com.bms.enterpriseconfiguration.resources.classpath.ClasspathResource;
import com.google.common.collect.Maps;

public class CombinedClasspathConfigurationImportHandler implements ResolutionActivity<Set<CombinedClasspathConfiguration>> {
	
	private static final String IMPORT_KEY = "importConfiguration";
	private static final String DEPRECATED_IMPORT_KEY = "importConfig";

	@Override
	public void perform(Set<CombinedClasspathConfiguration> input) {
		Map<String, CombinedClasspathConfiguration> configurationsByName = Maps.newHashMap();
		for(CombinedClasspathConfiguration configuration : input){
			configurationsByName.put(configuration.getConfigurationDescriptor().getName(), configuration);
		}
		for(CombinedClasspathConfiguration configuration : input){
			for(ClasspathResource resource : configuration.getConfigurationDescriptor().getResources()){
				try {
					List<String> configurationsToImport = CommonsConfigurationUtil.buildConfiguration(resource).getList(String.class, IMPORT_KEY, Collections.<String>emptyList());
					configurationsToImport.addAll(CommonsConfigurationUtil.buildConfiguration(resource).getList(String.class, DEPRECATED_IMPORT_KEY, Collections.<String>emptyList()));
					if(!configurationsToImport.isEmpty()){
						for(String configurationToImport : configurationsToImport){
							configuration.importConfiguration(configurationsByName.get(configurationToImport));
						}
					}
				} catch (ConfigurationException e) {
					throw new RuntimeException("Experienced an issue attempting to process a configuration import", e);
				}
			}
		}
	}

}
