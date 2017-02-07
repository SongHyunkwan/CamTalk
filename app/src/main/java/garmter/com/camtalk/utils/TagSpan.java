package garmter.com.camtalk.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.LineHeightSpan;
import android.text.style.RelativeSizeSpan;

import garmter.com.camtalk.R;

/**
 * Created by Youjung on 2017-01-01.
 */
public class TagSpan extends RelativeSizeSpan {
    private Context mContext;
    private int mSize;
    private String mText;
    private static float sProportion = 0;

    public TagSpan(Context context, String text, int size) {
        super(1.0f);
        mContext = context;
        mText = text;
        mSize = size;
    }


    public void chooseHeight(CharSequence text, int start, int end,
                             int spanstartv, int v,
                             Paint.FontMetricsInt fm, TextPaint paint) {
        int size = mSize;
        if (paint != null) {
            size *= paint.density;
        }

        if (fm.bottom - fm.top < size) {
            fm.top = fm.bottom - size;
            fm.ascent = fm.ascent - size;
        } else {
            if (sProportion == 0) {
                /*
                 * Calculate what fraction of the nominal ascent
                 * the height of a capital letter actually is,
                 * so that we won't reduce the ascent to less than
                 * that unless we absolutely have to.
                 */

                Paint p = new Paint();
                p.setColor(mContext.getResources().getColor(R.color.ct_light_grey));
                p.setTextSize(mSize);
                p.setColor(mContext.getResources().getColor(R.color.ct_light_grey));
                Rect r = new Rect();
                p.getTextBounds(mText, 0, mText.length(), r);
                Canvas canvas = new Canvas();
                canvas.drawRect(r, p);
                sProportion = (r.top) / p.ascent();
            }

//            int need = (int) Math.ceil(-fm.top * sProportion);
//
//            if (size - fm.descent >= need) {
//                /*
//                 * It is safe to shrink the ascent this much.
//                 */
//
//                fm.top = fm.bottom - size;
//                fm.ascent = fm.descent - size;
//            } else if (size >= need) {
//                /*
//                 * We can't show all the descent, but we can at least
//                 * show all the ascent.
//                 */
//
//                fm.top = fm.ascent = -need;
//                fm.bottom = fm.descent = fm.top + size;
//            } else {
//                /*
//                 * Show as much of the ascent as we can, and no descent.
//                 */
//
//                fm.top = fm.ascent = -size;
//                fm.bottom = fm.descent = 0;
//            }
        }
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        Paint p = new Paint();
        p.setColor(mContext.getResources().getColor(R.color.ct_light_grey));
        p.setTextSize(mSize);
        Rect r = new Rect();
        p.getTextBounds(mText, 0, mText.length(), r);
        Canvas canvas = new Canvas();
        canvas.drawRect(r, p);
        sProportion = (r.top) / p.ascent();


    }
}