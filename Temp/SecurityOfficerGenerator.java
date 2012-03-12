package com.acp.vision.security;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import com.acp.vision.security.utils.PropertiesUtils;

/**
 * The Class SecurityOfficerGenerator.
 */
public class SecurityOfficerGenerator {

    /** The Constant SPRING_CONFIG_FILE. */
    private static final String SPRING_CONFIG_FILE = "officerGenerator-spring-config.xml";

    /** The Constant OFFICER_GENERATOR_BEAN_ID. */
    private static final String OFFICER_GENERATOR_BEAN_ID = "officerGenerator";

    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args) {
        final XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource(SPRING_CONFIG_FILE));
        final PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
        cfg.setProperties(PropertiesUtils.getProperties());
        cfg.postProcessBeanFactory(beanFactory);
        final SecurityOfficerGeneratorRunnable officerGenerator = (SecurityOfficerGeneratorRunnable) beanFactory.getBean(OFFICER_GENERATOR_BEAN_ID);
        officerGenerator.run();
    }

}
