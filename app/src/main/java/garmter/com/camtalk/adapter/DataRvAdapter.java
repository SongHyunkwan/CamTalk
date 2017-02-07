package garmter.com.camtalk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import garmter.com.camtalk.R;
import garmter.com.camtalk.item.ItemData;
import garmter.com.camtalk.viewholder.DataListItemViewHolder;

/**
 * Created by Youjung on 2016-12-14.
 */
public class DataRvAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private DataListItemViewHolder.OnItemClickListener mListener;
    private ArrayList<ItemData> listOfMaterials;

    public DataRvAdapter(Context context, DataListItemViewHolder.OnItemClickListener listener) {
        mContext = context;
        mListener = listener;
        listOfMaterials = new ArrayList<>();
    }

    public void setListOfMaterials(ArrayList<ItemData> list) {
        listOfMaterials = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewholder_material, parent, false);
        return DataListItemViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if ( listOfMaterials != null && listOfMaterials.size() > position ) {
            ItemData item = listOfMaterials.get(position);
            DataListItemViewHolder viewHolder = (DataListItemViewHolder) holder;
            viewHolder.setView(item, mListener);
        }
    }

    @Override
    public int getItemCount() {
        return listOfMaterials == null ? 0 : listOfMaterials.size();
    }
}
