package hcmus.cnpm.team10.circle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hcmus.cnpm.team10.R;
import hcmus.cnpm.team10.user.User;

public class CircleListAdapter extends ArrayAdapter<Circle> {


    private List<Circle> mCircles;
    final View.OnClickListener onConfigButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), "Config btn clicked", Toast.LENGTH_LONG).show();
        }
    };

    public CircleListAdapter(Context context, int resource, List<Circle> objects) {
        super(context, resource, objects);
        mCircles = objects;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView != null)
            return convertView;

        Circle currentCircle = mCircles.get(position);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.list_item_circle, parent, false);


        setText(convertView, R.id.text_view_circle_name, currentCircle.getName());
        ImageView btnConfig = convertView.findViewById(R.id.image_btn_circle_option);
        btnConfig.setOnClickListener(onConfigButtonClick);

        //Show circle icon. Not supported yet....
        ImageView circleAvatarView = convertView.findViewById(R.id.image_view_circle_avatar);
        circleAvatarView.setImageDrawable(currentCircle.getAvatar(getContext()));

        // Show avatar. Not supported yet...
        ArrayList<User> circleMembers = currentCircle.getCircleMemberList();
        int numAvatarShow = Math.min(circleMembers.size(), 4);

        for(int i = 1; i <= numAvatarShow; ++i){
            int imageViewId = 0;
            switch (i){
                case 1: imageViewId = R.id.image_view_user1; break;
                case 2: imageViewId = R.id.image_view_user2; break;
                case 3: imageViewId = R.id.image_view_user3; break;
                case 4: imageViewId = R.id.image_view_user4; break;
            }
            ImageView avatarView = convertView.findViewById(imageViewId);
            avatarView.setImageDrawable(circleMembers.get(i - 1).getAvatar(getContext()));

        }

        return convertView;
    }


    private void setText(View v, int id, String value){
        ((TextView) v.findViewById(id)).setText(value);
    }


    public void addCircle(Circle c){
        this.mCircles.add(c);
    }

}
