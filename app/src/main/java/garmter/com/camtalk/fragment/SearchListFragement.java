package garmter.com.camtalk.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import co.moonmonkeylabs.realmsearchview.RealmSearchAdapter;
import co.moonmonkeylabs.realmsearchview.RealmSearchView;
import co.moonmonkeylabs.realmsearchview.RealmSearchViewHolder;
import garmter.com.camtalk.R;
import garmter.com.camtalk.activity.LectureDetailActivity;
import garmter.com.camtalk.adapter.SearchListRvAdapter;
import garmter.com.camtalk.db.LectureDB;
import garmter.com.camtalk.item.DKClass;
import garmter.com.camtalk.item.ItemComment;
import garmter.com.camtalk.item.ItemLecture;
import garmter.com.camtalk.item.ItemSearch;
import garmter.com.camtalk.network.JsonUtils;
import garmter.com.camtalk.network.NetworkUtil;
import garmter.com.camtalk.network.OnNetworkCallback;
import garmter.com.camtalk.utils.CTActivityUtil;
import garmter.com.camtalk.viewholder.LectureListItemViewHolder;
import garmter.com.camtalk.viewholder.SearchItemView;
import garmter.com.camtalk.viewholder.SearchListItemViewHolder;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchListFragement.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchListFragement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchListFragement extends Fragment {
    private ArrayList<ItemSearch> ais;
    private RealmSearchView realmSearchView;
    private SearchRecyclerViewAdapter adapter;
    private Realm realm;

    private SearchListRvAdapter mAdapter;
    private OnFragmentInteractionListener mListener;
    private JSONArray jsa;

    public SearchListFragement() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchListFragement.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchListFragement newInstance() {
        return new SearchListFragement();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        resetRealm();
        View view=inflater.inflate(R.layout.search, container, false);
        mAdapter=new SearchListRvAdapter(getContext(),null,onItemClickListener);

        realmSearchView = (RealmSearchView)view.findViewById(R.id.search_view);
        realm = Realm.getInstance(getRealmConfig());
        adapter = new SearchRecyclerViewAdapter(getActivity(), realm, "lecture_name");
        realmSearchView.setAdapter(adapter);

        initData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
            realm = null;
        }
    }

    private void loadBlogData() {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonFactory jsonFactory = new JsonFactory();
            try {
                JsonParser jsonParserBlog = jsonFactory.createParser(jsa.toString());
                System.out.println("팔설 : "+jsonParserBlog);
                System.out.println("팔설2 : "+jsonParserBlog.toString().length());
                System.out.println("팔설3 : "+jsonParserBlog.toString());
                List<ItemSearch> entries = objectMapper.readValue(jsonParserBlog, new TypeReference<List<ItemSearch>>() {});
                Realm realm = Realm.getInstance(getRealmConfig());
                realm.beginTransaction();
                realm.copyToRealm(entries);
                realm.commitTransaction();
                realm.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalStateException("Could not load blog data.");
            }
    }

    public class SearchRecyclerViewAdapter
            extends RealmSearchAdapter<ItemSearch, SearchRecyclerViewAdapter.ViewHolder> {

        public SearchRecyclerViewAdapter(
                Context context,
                Realm realm,
                String filterColumnName) {
            super(context, realm, filterColumnName);
        }

        public void setAis(ArrayList<ItemSearch> list) throws JSONException{
            ais = list;
                jsa = new JSONArray();
                for (int i = 0; i < ais.size(); i++) {
                    JSONObject jo = new JSONObject();
                    jo.put("lecture_name", ais.get(i).getLecture_name());
                    jo.put("timestamp", ais.get(i).getTimestamp());
                    jo.put("lec_comment", ais.get(i).getLec_comment());
                    jo.put("lec_prof", ais.get(i).getLec_prof());
                    jo.put("lecture_id", ais.get(i).getLecture_id());
                    jsa.put(jo);
                }

            Log.d("제이슨어레이",jsa.toString());
            System.out.println("제이슨어레이2 : "+jsa.length());
            System.out.println("ais사이즈 : "+ais.size());
            notifyDataSetChanged();
        }

        public class ViewHolder extends RealmSearchViewHolder {

            private SearchItemView blogItemView;

            public ViewHolder(FrameLayout container, TextView footerTextView) {
                super(container, footerTextView);
            }

            public ViewHolder(SearchItemView blogItemView) {
                super(blogItemView);
                this.blogItemView = blogItemView;
            }

        }

        @Override
        public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
            ViewHolder vh = new ViewHolder(new SearchItemView(viewGroup.getContext()));
            return vh;
        }

        @Override
        public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {
            final ItemSearch blog = realmResults.get(position);
            viewHolder.blogItemView.bind(blog);
        }

        @Override
        public ViewHolder onCreateFooterViewHolder(ViewGroup viewGroup) {
            View v = inflater.inflate(R.layout.footer_view, viewGroup, false);
            return new ViewHolder(
                    (FrameLayout) v,
                    (TextView) v.findViewById(R.id.footer_text_view));
        }

        @Override
        public void onBindFooterViewHolder(ViewHolder holder, int position) {
            super.onBindFooterViewHolder(holder, position);
            holder.itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }
            );
        }
    }

    private RealmConfiguration getRealmConfig() {
        return new RealmConfiguration
                .Builder(getActivity())
                .deleteRealmIfMigrationNeeded()
                .build();
    }

    private void resetRealm() {
        Realm.deleteRealm(getRealmConfig());
    }

    private void initData() {
        NetworkUtil nu=new NetworkUtil();
        nu.requestLectureSearch(SearchListFragement.this, onNetworkCallback);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private SearchListItemViewHolder.OnItemClickListener onItemClickListener = new SearchListItemViewHolder.OnItemClickListener() {
        @Override
        public void onItemClicked(String code) {
            CTActivityUtil activityUtil = new CTActivityUtil();
            activityUtil.startLectureDetailActivity(getContext(), code);
        }
    };
    private boolean loading = false;

    private OnNetworkCallback onNetworkCallback = new OnNetworkCallback() {
        @Override
        public void onSuccess(String msg) {
            JSONObject obj = null;
            JsonUtils utils = new JsonUtils();
            try {
                obj = new JSONObject(msg);


                ArrayList<ItemSearch> listOfSearch = utils.getListOfSearchsFromJsonObject(obj);
//                mAdapter.setListOfSearchs(listOfSearch);
                adapter.setAis(listOfSearch);
                loadBlogData();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                loading = false;
            }
        }

        @Override
        public void onFail(String msg) {
            loading = false;
            Toast.makeText(getActivity(), "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    };
}