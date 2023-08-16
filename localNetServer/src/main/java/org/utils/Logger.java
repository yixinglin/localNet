package org.utils;

import org.slf4j.LoggerFactory;

public class Logger {

    Class<?> clazz;

    private Logger(Class<?> clazz) {
        this.clazz = clazz;
    }

    public static Logger loggerBuilder(Class<?> clazz) {
        return new Logger(clazz);
    }

    public void debug(String msg) {
        LoggerFactory.getLogger(clazz).debug(msg);
    }

    public void error(String msg) {
        LoggerFactory.getLogger(clazz).error(msg);
    }

    public void info(String msg) {
        LoggerFactory.getLogger(clazz).info(msg);
    }

    public void warn(String msg) {
        LoggerFactory.getLogger(clazz).warn(msg);
    }

    public void trace(String msg) {
        LoggerFactory.getLogger(clazz).trace(msg);
    }

}
