package com.ltc.gank.view;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ltc.gank.R;
import com.ltc.gank.bean.GanHuo;
import com.ltc.gank.server.GankRetrofit;
import com.ltc.gank.server.GankService;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GirlFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<GanHuo.Result> ganHuoList;


    private TextView content;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GirlFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GirlFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GirlFragment newInstance(String param1, String param2) {
        GirlFragment fragment = new GirlFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_girl, container, false);
        content = (TextView) view.findViewById(R.id.content);
        getData("福利",10,1);
        return view;
    }

    private String getDataString(List<GanHuo.Result> ganHuoList) {
        StringBuilder builder = new StringBuilder();
        for (GanHuo.Result result: ganHuoList ) {
            builder.append(result.toString()).append('\n');
        }
        return builder.toString();
    }

    private void getData(String type,int count,int page) {
        GankRetrofit.getRetrofit()
                .create(GankService.class)
                .getGanHuo(type,count,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GanHuo>() {
                    @Override
                    public void onCompleted()  {
                        Log.e("666","onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(content,"NO WIFI，不能愉快的看妹纸啦..",Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(GanHuo ganHuo) {
                        ganHuoList = ganHuo.getResults();
                        content.setText(getDataString(ganHuoList));
                    }
                });
    }


}
