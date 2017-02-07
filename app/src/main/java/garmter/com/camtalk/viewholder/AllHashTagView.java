package garmter.com.camtalk.viewholder;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import garmter.com.camtalk.R;

/**
 * TODO: document your custom view class.
 */
public class AllHashTagView extends LinearLayout {

    private LayoutHashTag[] mHashTag;
    private int[] idLayout = { R.id.htTag0, R.id.htTag1, R.id.htTag2, R.id.htTag3,
                                R.id.htTag4, R.id.htTag5, R.id.htTag6, R.id.htTag7,
                                R.id.htTag8, R.id.htTag9, R.id.htTag10, R.id.htTag11,
                                R.id.htTag12, R.id.htTag13, R.id.htTag14, R.id.htTag15 };
    public AllHashTagView(Context context) {
        super(context);
        init(null, 0);
    }

    public AllHashTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.AllHashTagView, defStyle, 0);

        // inflate views
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(infService);
        View v = inflater.inflate(R.layout.view_all_hash_tag, this, false);
        addView(v);

        mHashTag = new LayoutHashTag[16];
        for(int i=0; i<mHashTag.length; i++) {
            mHashTag[i] = (LayoutHashTag) v.findViewById(idLayout[i]);
        }


        a.recycle();
    }

    public void setTagCount(int[] tagCount) {
        for(int i=0; i<tagCount.length; i++) {
           if ( mHashTag[i] != null ) mHashTag[i].setCount(tagCount[i]);
        }
    }


}
