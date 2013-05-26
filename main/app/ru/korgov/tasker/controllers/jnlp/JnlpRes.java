package ru.korgov.tasker.controllers.jnlp;

import play.api.libs.MimeTypes;

import java.util.List;

/**
 * Author: Kirill Korgov (korgov@korgov.ru)
 * Date: 23.05.13 9:40
 */
public class JnlpRes {

    public static final String CONTENT_TYPE = MimeTypes.forExtension("jnlp").get();


    private final String hostname;
    final String fullClassName;
    final String jarName;
    private final List<String> args;
    final String descr;

    public JnlpRes(final String hostname, final String fullClassName, final String jarName, final List<String> args, final String descr) {
        this.hostname = hostname;
        this.fullClassName = fullClassName;
        this.jarName = jarName;
        this.args = args;
        this.descr = descr;
    }

    public String getHostname() {
        return hostname;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public String getJarName() {
        return jarName;
    }

    public List<String> getArgs() {
        return args;
    }

    public String getDescr() {
        return descr;
    }
}
