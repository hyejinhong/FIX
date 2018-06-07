package com.example.hhj73.fix;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MatchingAdapter extends BaseAdapter{

    ArrayList<User> users;
    ArrayList<Uri> roomPic;
    Context context;

    public MatchingAdapter(Context context, ArrayList<User> users, ArrayList<Uri> roomPic) {
        this.context = context;
        this.users = users;
        this.roomPic = roomPic;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.matching_row, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        ImageView rowImage = (ImageView) convertView.findViewById(R.id.rowImage);
        TextView rowTitle = (TextView) convertView.findViewById(R.id.rowTitle);
        TextView rowAddress = (TextView) convertView.findViewById(R.id.rowAddress);
        TextView rowPrice = (TextView) convertView.findViewById(R.id.rowPrice);

//        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
//        User mUser = users.get(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        // rowImage.setImageDrawable(myItem.getIcon()); // 이미지 어케 갖고와요
//        rowTitle.setText(mUser.getName()+"님의 집");
//        rowAddress.setText(mUser.getAddress());
//        rowPrice.setText(mUser.getCost());

        rowTitle.setText(users.get(position).getName()+"님의 집");
        rowAddress.setText(users.get(position).getAddress());
        rowPrice.setText(users.get(position).getCost());

        Glide.with(context)
                .load(roomPic.get(position))
                .centerCrop()
                .into(rowImage);
//        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */


        return convertView;
    }
}
