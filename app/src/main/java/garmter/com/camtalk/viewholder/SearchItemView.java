package garmter.com.camtalk.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import garmter.com.camtalk.R;
import garmter.com.camtalk.item.ItemSearch;
import butterknife.Bind;
import butterknife.ButterKnife;
import garmter.com.camtalk.utils.CTActivityUtil;

/**
 * Created by MinSik on 2017-02-02.
 */
/**
 * View for a {@link ItemSearch} model.
 */
public class SearchItemView extends RelativeLayout {
    @Bind(R.id.lecture_name)
    TextView lecture_name;

    @Bind(R.id.lecture_id)
    TextView lecture_id;

    public SearchItemView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.search_item_view, this);
        ButterKnife.bind(this);
    }

    public void bind(ItemSearch blog) {
        lecture_id.setText(blog.getLecture_id());
        lecture_id.setVisibility(GONE);
        lecture_name.setText(blog.getLecture_name());
        lecture_name.setOnClickListener(clickClick);
    }

    private View.OnClickListener clickClick=new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.lecture_name :
                    CTActivityUtil ca=new CTActivityUtil();
                    ca.startLectureDetailActivity(getContext(), lecture_id.getText().toString(), lecture_name.getText().toString());
            }
        }
    };

}
