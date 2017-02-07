package garmter.com.camtalk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import garmter.com.camtalk.R;
import garmter.com.camtalk.item.DKClass;
import garmter.com.camtalk.item.ItemCard;
import garmter.com.camtalk.item.ItemCardCover;
import garmter.com.camtalk.utils.CTUtils;
import garmter.com.camtalk.viewholder.CardListItemViewHolder;

/**
 * Created by Youjung on 2016-12-16.
 */
public class CardListRvAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private CardListItemViewHolder.OnItemClickListener mListener;
    private boolean isCover = false;
    private ArrayList<ItemCardCover> listOfFiles;

    int mHeight;

    public CardListRvAdapter(Context context, CardListItemViewHolder.OnItemClickListener listener) {
        super();
        mContext = context;
        listOfFiles = new ArrayList<>();

        float windowHeight = CTUtils.getWindowHeight(context);
        float dp100 = CTUtils.convertDpToPixel(context, 100);
        mHeight = (int)((windowHeight - dp100 ) / 4);

        mListener = listener;
    }

    public void setCover(boolean cover) {
        isCover = cover;
    }
    public boolean getIsCover() { return isCover; }

    boolean deleteMode = false;
    public void setDeleteMode(boolean deleteMode) {
        this.deleteMode = deleteMode;
        notifyDataSetChanged();
    }

    public boolean getDeleteMode() {
        return deleteMode;
    }

    public void setListOfFiles(boolean isCover, ArrayList<ItemCardCover> list) {
        this.isCover = isCover;
        listOfFiles = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewholder_card, parent, false);
        return CardListItemViewHolder.newInstance(view, mHeight);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CardListItemViewHolder viewHolder = (CardListItemViewHolder) holder;
        viewHolder.setView(mContext, listOfFiles.get(position), deleteMode, mListener);
    }

    @Override
    public int getItemCount() {
        return getCount();
    }

    private int getCount() {
        return listOfFiles == null ? 0 : listOfFiles.size();
    }
}
