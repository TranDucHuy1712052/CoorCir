package hcmus.cnpm.team10.user;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import org.json.JSONException;
import org.json.JSONObject;

import hcmus.cnpm.team10.R;

public class User {

    protected User(){}

    protected String mUid;
    protected int mAvatarResource;

    public User(String uid){
        mUid = uid;

        mAvatarResource = getAvatarResourceId(mUid);
    }

    protected int getAvatarResourceId(String uid){
        if(uid.equals("1712060"))
            return R.drawable.sample_ava1;
        else if(uid.equals("1712052"))
            return R.drawable.sample_ava2;
        else if(uid.equals("1712122"))
            return R.drawable.ntphat;
        else if(uid.equals("1712212"))
            return R.drawable.ltan;
        else
            return R.drawable.ic_launcher_background;
    }

    public User(JSONObject object) throws JSONException {
        mUid = object.getString("uid");

        mAvatarResource = getAvatarResourceId(mUid);
    }

    public Drawable getAvatar(Context context){
        Resources r = context.getResources();
        Drawable d = r.getDrawable(mAvatarResource, null);
        return d;
    }
}
