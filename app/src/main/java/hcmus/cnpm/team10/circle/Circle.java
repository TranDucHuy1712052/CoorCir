package hcmus.cnpm.team10.circle;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import hcmus.cnpm.team10.R;
import hcmus.cnpm.team10.user.User;

public class Circle {

    public static final int PERMISSION_PRIVATE = 0;

    private final String mCID;
    private String mName;
    private int mAccessPermission;
    private String mInviteURL;
    private String mDescription;
    private ArrayList<User> mCircleMemberList = null;

    public Circle(JSONObject data) throws JSONException {
        mCID = data.getString("CID");
        mName = data.getString("Name");
        mAccessPermission =  Integer.valueOf(data.getString("AccessPermission"));
        mInviteURL = data.getString("InviteURL");
        mDescription = data.getString("Description");
    }

    public Circle(String CID, String name, int accessPermission, String inviteURL, String description){
        mCID  = CID;
        mName = name;
        mAccessPermission = accessPermission;
        mInviteURL = inviteURL;
        mDescription = description;
    }


    public String getCID() {
        return mCID;
    }

    public String getName() {
        return mName;
    }

    public int getAccessPermission() {
        return mAccessPermission;
    }

    public String getInviteURL() {
        return mInviteURL;
    }

    public String getDescription() {
        return mDescription;
    }


    public ArrayList<User> getCircleMemberList(){
        // For Debugging
        if (mCircleMemberList == null) {
            mCircleMemberList = new ArrayList<User>();
        }
        return mCircleMemberList;
    }


    public boolean addUser(User u){
        getCircleMemberList().add(u);
        return true;
    }


    public Drawable getAvatar(Context context){
        Drawable d = context.getResources().getDrawable(R.drawable.ic_launcher_background, null);
        return d;
    }
}
