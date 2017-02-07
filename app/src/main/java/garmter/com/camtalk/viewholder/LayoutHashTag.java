package garmter.com.camtalk.viewholder;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import garmter.com.camtalk.R;

/**
 * TODO: document your custom view class.
 */
public class LayoutHashTag extends LinearLayout {
    private TextView mTag;
    private TextView mCount;

    public LayoutHashTag(Context context) {
        super(context);
        init(null, 0);
    }

    public LayoutHashTag(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public LayoutHashTag(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.LayoutHashTag, defStyle, 0);

        boolean clickable = a.getBoolean(R.styleable.LayoutHashTag_clickable, true);
        boolean countVisible = a.getBoolean(R.styleable.LayoutHashTag_countVisible, false);
        String text = a.getString(R.styleable.LayoutHashTag_text);
        int count = a.getInt(R.styleable.LayoutHashTag_count, 0);

        // inflate views
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(infService);
        View v = inflater.inflate(R.layout.view_hash_tag, this, false);
        addView(v);

        mTag = (TextView) v.findViewById(R.id.tvTag);
        mTag.setClickable(clickable);
        mTag.setText( "#" + text);

        mCount = (TextView) v.findViewById(R.id.tvCount);
        mCount.setVisibility(countVisible ? VISIBLE : GONE);
        mCount.setText(String.valueOf(count));


        a.recycle();
    }

    public void setText(String text) {
        if ( text == null ) return;

        if ( !text.startsWith("#") ) text = "#" + text;

        if( mTag != null ) mTag.setText(text);
    }
    public String getText() {
        if ( mTag == null ) return "";
        String text = mTag.getText().toString();

        if ( text != null ) {
            if (text.startsWith("#")) text = text.substring(1);
            return text;
        } else {
            return "";
        }
    }

    public void setCount(int count) {
        if ( mCount != null ) mCount.setText(String.valueOf(count));
    }

    public void setCount(String count) {
        if ( mCount != null ) mCount.setText(count);
    }

    public int getCount() {
        if ( mCount != null ) return Integer.valueOf(mCount.getText().toString());
        return 0;
    }
}
