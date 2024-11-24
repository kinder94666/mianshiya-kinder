package com.kason.mianshiya.constant;

public interface RedisConstant {
    String USER_SIGN_IN_REDIS_KEY_PREFIX = "user:signins:";

    /**
     *  获取用户签到记录的key
     */
    static String getUserSignInKey(int year,long userId) {
        return String.format("%s:%s:%s", USER_SIGN_IN_REDIS_KEY_PREFIX, year, userId);
    }
}
