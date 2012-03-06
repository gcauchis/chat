package com.acp.vision.security;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import com.acp.vision.security.Utils.PropertiesUtils;

/**
 * The Class SecurityOfficerGenerator.
 */
public class SecurityOfficerGenerator {

    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args) {
        final XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("officerGenerator-spring-config.xml"));
        PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
        cfg.setProperties(PropertiesUtils.getProperties());
        cfg.postProcessBeanFactory(beanFactory);
        final SecurityOfficerGeneratorRunnable officerGenerator = (SecurityOfficerGeneratorRunnable) beanFactory.getBean("officerGenerator");
        officerGenerator.run();
    }


}
