package com.creativeoh.keywordsubscriptor.keyword;

/**
 * Created by cyber on 2017-07-06.
 */

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.creativeoh.keywordsubscriptor.R;
import com.creativeoh.keywordsubscriptor.keyword.vo.KeywordVO;


public class KeywordListAdapter extends BaseAdapter {
    private KeywordDataManager mManager;
    private LayoutInflater inflater;
    private Context mCtx;
    private boolean isDashboard = false;

    private ListView mListView;

    public KeywordListAdapter(Context ctx, KeywordDataManager mManager) {
        this.mManager = mManager;
        mCtx = ctx;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public KeywordListAdapter(Context ctx, KeywordDataManager mManager, boolean isDashboard) {
        this.mManager = mManager;
        this.isDashboard = isDashboard;
        mCtx = ctx;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mManager.getCount();
    }

    @Override
    public Object getItem(int position) {
        return mManager.getItem(position).getKeyword();
    }

    @Override
    public long getItemId(int position) {
        return mManager.getItem(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final KeywordVO vo = mManager.getItem(position);

        if(convertView == null){
                convertView = inflater.inflate(R.layout.keyword_view, parent, false);
        }
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvKeywordTitle);
        TextView tvRank = (TextView) convertView.findViewById(R.id.tvPortalRank);
        if(vo.getTypeCode() == 2){
            tvRank.setVisibility(View.GONE);
        }else{
            tvRank.setVisibility(View.VISIBLE);
            String rankText = "순위: ";
            if(!TextUtils.isEmpty(vo.getRankNAVER()))
                rankText += " N: " + vo.getRankNAVER();
            if(!TextUtils.isEmpty(vo.getRankDAUM()))
                rankText += " D: " + vo.getRankDAUM();
            if(!TextUtils.isEmpty(vo.getRankZUM()))
                rankText += " Z: " + vo.getRankZUM();
            tvRank.setText(rankText);
        }

        tvTitle.setText(vo.getRank() + " : " + vo.getKeyword());
        return convertView;
    }

    public void setListVIew(ListView listVIew) {
        mListView = listVIew;
    }
    public ListView getListVIew() {
        return mListView;
    }
}
