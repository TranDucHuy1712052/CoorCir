package hcmus.cnpm.team10.user;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import hcmus.cnpm.team10.R;

public class User {

    public Drawable getAvatar(Context context){
        Resources r = context.getResources();
        Drawable d = r.getDrawable(R.drawable.sample_ava1, null);
        return d;
    }
}
