package com.chat.im.im_chatserver.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CommonUtils {

    public static String encodeStr(String stx) {
        // return base64(ustr.hashCode)
        return Base64.getUrlEncoder().encodeToString(String.valueOf(stx.hashCode())
                .getBytes(StandardCharsets.UTF_8));
    }
}
