package garmter.com.camtalk;

/**
 * Created by MinSik on 2017-01-29.
 */

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;


public class BlogItemView extends RelativeLayout {


    @Bind(R.id.lecture_name)
    TextView title;


    public BlogItemView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.search_item_view, this);
        ButterKnife.bind(this);
    }


}