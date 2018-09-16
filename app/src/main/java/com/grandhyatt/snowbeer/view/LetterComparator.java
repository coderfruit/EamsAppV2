package com.grandhyatt.snowbeer.view;

import com.grandhyatt.snowbeer.entity.CustomerEntity;

import java.util.Comparator;


/**
 * @author wulifu
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/7/24 00:35
 */
public class LetterComparator implements Comparator<CustomerEntity> {

    @Override
    public int compare(CustomerEntity left, CustomerEntity right) {
        if (left == null || right == null) {
            return 0;
        }

        String lhsSortLetters = left.getPys().substring(0, 1).toUpperCase();
        String rhsSortLetters = right.getPys().substring(0, 1).toUpperCase();
        if (lhsSortLetters == null || rhsSortLetters == null) {
            return 0;
        }
        return lhsSortLetters.compareTo(rhsSortLetters);
    }
}