package ru.korgov.tasker.core.app.spring;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import play.Application;
import play.Logger;
import play.Play;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

/**
 * Author: Kirill Korgov (kirill@korgov.ru))
 * Date: 4/7/13 4:33 AM
 */
public class SpringContext {
    private static final String SPRING_CONTEXT_PATH = "spring.context";
    private static final String SPRING_ADD_PLAY_PROPERTIES = "spring.add-play-properties";
    public static final String DEFAULT_CONTEXT_PATH = "application-context.xml";

    @SuppressWarnings("StaticNonFinalField")
    private static ApplicationContext context = null;

    private SpringContext() {
    }

    @Nullable
    public static void init(final Application app, final String... args) {
        final ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(app.classloader());
        try {
            context = initContext(app, args);
        } catch (BeanCreationException e) {
            throw new RuntimeException("Unable to instantiate Spring application context", e);
        } finally {
            Thread.currentThread().setContextClassLoader(originalClassLoader);
        }
    }

    private static ApplicationContext initContext(final Application app, final String[] args) {
        final FileSystemXmlApplicationContext fileSystemXmlApplicationContext = new FileSystemXmlApplicationContext(
                new String[]{getContextPath(app)}, getEnvironmentApplicationContext(args));
        fileSystemXmlApplicationContext.setAllowBeanDefinitionOverriding(false);
        fileSystemXmlApplicationContext.setAllowCircularReferences(false);
        fileSystemXmlApplicationContext.setClassLoader(Play.application().classloader());

        addPropertiesConfig(app, fileSystemXmlApplicationContext);

        return fileSystemXmlApplicationContext;
    }

    private static void addPropertiesConfig(final Application app, final FileSystemXmlApplicationContext applicationContext) {
        final String addPlayProperties = app.configuration().getString(SPRING_ADD_PLAY_PROPERTIES);
        if (!"false".equalsIgnoreCase(addPlayProperties)) {
            Logger.debug("Adding PropertyPlaceholderConfigurer with Play properties");
            // Convert play's configuration to plain old java properties
            final Properties properties = new Properties();
            final Set<String> keys = app.configuration().keys();
            for (final String key : keys) {
                try {
                    final String value = app.configuration().getString(key);
                    properties.setProperty(key, value);
                } catch (Throwable ignored) {
                    // Some config items are complex, so we'll just skip those for now.
                }
            }

            final PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
            configurer.setProperties(properties);
            applicationContext.addBeanFactoryPostProcessor(configurer);
        } else {
            Logger.debug("PropertyPlaceholderConfigurer with Play properties NOT added");
        }
    }

    private static String getContextPath(final Application app) {
        final String contextPath = app.configuration().getString(SPRING_CONTEXT_PATH);
        final URL contextFile = app.classloader().getResource(contextPath == null ? DEFAULT_CONTEXT_PATH : contextPath);
        if (contextFile == null) {
            throw new IllegalStateException("incorrect context-file-path: " + contextFile);
        }
        final String urlStr = contextFile.toString();
        Logger.debug("Loading spring-context from: " + urlStr);
        return urlStr;
    }

    private static ApplicationContext getEnvironmentApplicationContext(final String[] args) {
        final StaticApplicationContext sac = new StaticApplicationContext(null);
        final ConstructorArgumentValues cav = new ConstructorArgumentValues();
        cav.addGenericArgumentValue(Arrays.asList(args));
        sac.setAllowBeanDefinitionOverriding(false);
        sac.setAllowCircularReferences(false);
        sac.registerBeanDefinition(
                "mainArgs",
                new RootBeanDefinition(
                        ArrayList.class,
                        new ConstructorArgumentValues(cav),
                        null));
        sac.refresh();
        return sac;
    }

    public static ApplicationContext getContext() {
        if (context == null) {
            throw new IllegalStateException("context is null!");
        }
        return context;
    }
}
