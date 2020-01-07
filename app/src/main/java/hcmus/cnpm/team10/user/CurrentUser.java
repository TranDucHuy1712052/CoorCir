package hcmus.cnpm.team10.user;

import java.util.ArrayList;

import hcmus.cnpm.team10.circle.Circle;

public class CurrentUser extends User {
    private static final CurrentUser       ourInstance = new CurrentUser();
    private static       String            userId;

    public static CurrentUser getInstance() {
        return ourInstance;
    }

    private CurrentUser() {

    }


    public static void setUserId(String userId){
        CurrentUser.userId = userId;
        ourInstance.mUid = userId;
        ourInstance.mAvatarResource = ourInstance.getAvatarResourceId(userId);
    }

    public static String getUserId(){
        return CurrentUser.userId;
    }

}
