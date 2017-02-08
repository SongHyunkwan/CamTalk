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

    @Bind(R.id.lec_prof)
    TextView lec_prof;

    @Bind(R.id.tag_a)
    TextView tag_a;

    @Bind(R.id.tag_b)
    TextView tag_b;

    @Bind(R.id.tag_c)
    TextView tag_c;

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
        tag_a.setText(blog.getTag_a());
        tag_a.setVisibility(GONE);
        tag_b.setText(blog.getTag_b());
        tag_b.setVisibility(GONE);
        tag_c.setText(blog.getTag_c());
        tag_c.setVisibility(GONE);
        lecture_name.setText(blog.getLecture_name());
        lec_prof.setText(blog.getLec_prof());
        lecture_name.setOnClickListener(clickClick);
    }

    private View.OnClickListener clickClick=new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.lecture_name :
                    CTActivityUtil ca=new CTActivityUtil();
                    ca.startLectureDetailActivity(getContext(), lecture_id.getText().toString(), lecture_name.getText().toString(), lec_prof.getText().toString());
            }
        }
    };

}
