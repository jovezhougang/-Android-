package com.csii.simpleone;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class MyApplication extends Application {
    /**
     * log4j 日志系统
     */
    final static Logger LOGGER = LoggerFactory.getLogger(MyApplication.class);
    @Override
    public void onCreate() {
        super.onCreate();
        configureLogbackDirectly(getApplicationContext());
        Toast.makeText(getApplicationContext(),
                Environment.getExternalStorageDirectory().getAbsolutePath(),
                Toast.LENGTH_LONG).show();
    }

    /**
     * 配置系统日志
     * 
     * 调用方法 LoggerFactory.getLogger(HttpUtil.class).debug(result);
     * 
     * @param context
     */
    static void configureLogbackDirectly(Context context) {
        StringBuilder LOGBACK_XML = new StringBuilder();
        LOGBACK_XML.append("<configuration>");
        LOGBACK_XML
                .append("<appender name='FILE' class='ch.qos.logback.core.rolling.RollingFileAppender'>");
        LOGBACK_XML
                .append("<rollingPolicy class='ch.qos.logback.core.rolling.TimeBasedRollingPolicy'>");
        LOGBACK_XML.append("<fileNamePattern>");
        LOGBACK_XML.append(Environment.getExternalStorageDirectory()
                .getAbsolutePath());
        LOGBACK_XML.append(File.separator);
        LOGBACK_XML.append(context.getPackageName());
        LOGBACK_XML.append(File.separator);
        LOGBACK_XML.append("Log");
        LOGBACK_XML.append(File.separator);
        LOGBACK_XML.append("logrouterFile.%d{yyyy-MM-dd}.log");
        LOGBACK_XML.append("</fileNamePattern>");
        LOGBACK_XML.append("<maxHistory>30</maxHistory>");
        LOGBACK_XML.append("</rollingPolicy>");
        LOGBACK_XML.append("<encoder>");
        LOGBACK_XML
                .append("<pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>");
        LOGBACK_XML.append("</encoder>");
        LOGBACK_XML.append("</appender>");
        LOGBACK_XML.append("<root level='ALL'>");
        LOGBACK_XML.append("<appender-ref ref='FILE' />");
        LOGBACK_XML.append("</root>");
        LOGBACK_XML.append("</configuration>");
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.reset();
        JoranConfigurator config = new JoranConfigurator();
        config.setContext(lc);
        InputStream stream = new ByteArrayInputStream(LOGBACK_XML.toString()
                .getBytes());
        try {
            config.doConfigure(stream);
            LOGGER.info("Logback configure succeed!");
        } catch (JoranException e) {
            e.printStackTrace();
        } finally {
            Util.closeable(stream);
        }
    }
}
