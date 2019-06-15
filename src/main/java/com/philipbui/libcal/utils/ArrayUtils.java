package com.philipbui.libcal.utils;

public class ArrayUtils {

    public static String join(int[] nums, CharSequence delimiter) {
        if (nums == null || nums.length == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < nums.length - 1; i++) {
            stringBuilder.append(nums[i]);
            stringBuilder.append(delimiter);
        }
        stringBuilder.append(nums[nums.length - 1]);
        return stringBuilder.toString();
    }
}
