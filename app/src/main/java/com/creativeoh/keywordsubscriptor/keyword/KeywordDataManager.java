package com.creativeoh.keywordsubscriptor.keyword;

import android.content.Context;


import com.creativeoh.keywordsubscriptor.keyword.vo.KeywordVO;

import java.util.ArrayList;

/**
 * Created by cyber on 2017-07-06.
 */

public class KeywordDataManager {
    Context mCtx = null;
    int mLimit = 0;
    ArrayList<KeywordVO> dataList = new ArrayList();

    public KeywordDataManager(Context ctx) {
        mCtx = ctx;
    }

    public KeywordDataManager(Context ctx, int limit) {
        mCtx = ctx;
        mLimit = limit;
    }

    public void setDataList(ArrayList<KeywordVO> dataList) {
        ArrayList<KeywordVO> arr = new ArrayList<KeywordVO>();
        if(mLimit > 0){
            arr.addAll(dataList.subList(0, mLimit));
            this.dataList = arr;
        }
        else{
            this.dataList = dataList;
        }
    }

    public int getCount(){
        return this.dataList.size();
    }

    public KeywordVO getItem(int position){
        return this.dataList.get(position);
    }

}
