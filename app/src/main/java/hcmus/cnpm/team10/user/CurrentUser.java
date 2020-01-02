package hcmus.cnpm.team10.user;

public class CurrentUser extends User {
    private static final CurrentUser ourInstance = new CurrentUser();
    private static String userId;


    static CurrentUser getInstance() {
        return ourInstance;
    }

    private CurrentUser() {
    }


    public static void setUserId(String userId){
        CurrentUser.userId = userId;
    }

    public static String getUserId(){
        return CurrentUser.userId;
    }

}
