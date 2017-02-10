package com.lh.daily.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by home on 2017/2/8.
 */

public class ZhihuDataUtils {

    /**
     * 将系统的时间（毫秒）转换为知乎时间（yyyyMMdd）
     * @param time time
     * @return time
     */
    public static long long2date(long time){
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String value = simpleDateFormat.format(date);
        return Long.parseLong(value);
    }
}
