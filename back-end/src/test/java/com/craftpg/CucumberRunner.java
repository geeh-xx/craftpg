package com.craftpg;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

import com.craftpg.configuration.ContainerTestConfig;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.SelectDirectories;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@SelectDirectories("src/test/resources")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.craftpg.features.steps, com.craftpg.configuration")
public class CucumberRunner extends ContainerTestConfig {
}
