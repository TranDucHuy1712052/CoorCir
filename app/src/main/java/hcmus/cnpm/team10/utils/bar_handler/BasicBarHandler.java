package hcmus.cnpm.team10.utils.bar_handler;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import hcmus.cnpm.team10.R;

public class BasicBarHandler implements IBarHandler{

    private final        String               mTitle;
    private final        int                  mLeftImageId;
    private final        View.OnClickListener mLeftListener;
    private final        int                  mRightImageId;
    private final        View.OnClickListener mRightListener;
    public BasicBarHandler(String title, int leftImageId, View.OnClickListener leftListener,
                           int rightImageId, View.OnClickListener rightListener){

        this.mTitle = title;
        this.mLeftImageId = leftImageId;
        this.mRightImageId = rightImageId;
        this.mLeftListener = leftListener;
        this.mRightListener = rightListener;

    }

    public BasicBarHandler(String title, int leftImageId, View.OnClickListener leftListener){
        this.mTitle = title;
        this.mLeftImageId = leftImageId;
        this.mRightImageId = 0;
        this.mLeftListener = leftListener;
        this.mRightListener = null;

    }

    @Override
    public void setUp(View v) {
        TextView textViewTitle = v.findViewById(R.id.text_view_title);
        textViewTitle.setText(this.mTitle);

        setUpImageBtn(v, mLeftImageId, R.id.image_btn_left, mLeftListener);
        setUpImageBtn(v, mRightImageId, R.id.image_btn_right, mRightListener);
    }


    protected void setUpImageBtn(View v, int imageId, int btnId, View.OnClickListener listener){
        ImageButton btn = v.findViewById(btnId);

        if(imageId != 0)
            btn.setImageResource(imageId);
        else
            btn.setBackgroundColor(Color.TRANSPARENT);
        if(listener != null)
            btn.setOnClickListener(listener);
    }
}