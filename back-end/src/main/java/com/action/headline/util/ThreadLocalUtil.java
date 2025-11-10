package com.action.headline.util;

/**
 * ThreadLocal工具类，用于存储当前线程的用户ID
 */
public class ThreadLocalUtil {
    
    private static final ThreadLocal<Integer> USER_ID_HOLDER = new ThreadLocal<>();
    
    /**
     * 设置当前线程的用户ID
     */
    public static void setUserId(Integer userId) {
        USER_ID_HOLDER.set(userId);
    }
    
    /**
     * 获取当前线程的用户ID
     */
    public static Integer getUserId() {
        return USER_ID_HOLDER.get();
    }
    
    /**
     * 清除当前线程的用户ID
     */
    public static void removeUserId() {
        USER_ID_HOLDER.remove();
    }
}

