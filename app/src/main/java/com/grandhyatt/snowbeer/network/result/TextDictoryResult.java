package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.TextDictionaryEntity;

/**
 * Created by ycm on 2018/8/16.
 */

public class TextDictoryResult extends Result {
    private TextDictionaryEntity data;

    public TextDictionaryEntity getData() {
        return data;
    }

    public void setData(TextDictionaryEntity data) {
        this.data = data;
    }
}
