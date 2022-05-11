package com.chat.im.im_serviceprovider.component;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 日志 Mapper
 * @author: chovychan in 2022/5/9
 */
@Log4j2
public class LogMapper {

    public static void info(String TAG, String msg) {
        info(TAG, "", msg);
    }

    public static void info(String TAG, String funcName, String msg) {
        log(LogType.INFO, TAG, funcName, msg);
    }

    public static RuntimeException error(String TAG, String msg) {
        error(TAG, "", msg);
        return new RuntimeException(msg);
    }

    public static void error(String TAG, String funcName, String msg) {
        log(LogType.ERROR, TAG, funcName, msg);
    }

    private static void log(LogType type, String TAG, String funcName, String msg) {
        if (!Strings.isEmpty(funcName)) funcName = "[" + funcName + "]";
        String logTAG = "[" + TAG + "] " + funcName + "-- " + type.toString() + " Msg: {}";
        logMap.get(type).accept(logTAG, msg);
    }

    public enum LogType {
        INFO,
        ERROR,
        DEBUG;
    }

    private static final Map<LogType, BiConsumer<String, String>> logMap = new HashMap<>() {{
        put(LogType.INFO, log::info);
        put(LogType.DEBUG, log::debug);
        put(LogType.ERROR, log::error);
    }};


}
