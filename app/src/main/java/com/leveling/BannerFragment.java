package com.leveling;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.leveling.adapter.MyPagerAdapter;
import com.leveling.entity.BannerInfo;
import com.leveling.entity.Url;
import com.leveling.personcenter.PublicActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;


public class BannerFragment extends Fragment {

    private TextView tv_jump;
    private ViewPager mViewPager;
    // 记录当前的页数
    private int mCount = 0;
    // 开始
    public static final int START = -1;
    // 停止
    public static final int STOP = -2;
    // 更新
    public static final int UPDATE = -3;
    // 接受传过来的当前页面数
    public static final int RECORD = -4;
    private List<ImageView> mList;
    private MyPagerAdapter mAdapter;
    private List<String> urlList;
    private ImageView dot1, dot2, dot3, dot0;
    private ImageView[] dots = new ImageView[4];
    private int length1;
    private String name0,name1,name2,name3;
    public static int j;
    private int m ;
    private Handler  handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START:
                    handler.sendEmptyMessageDelayed(UPDATE, 3000);
                    break;
                case STOP:
                    handler.removeMessages(UPDATE);
                    break;
                case UPDATE:
                    mCount++;
                    mViewPager.setCurrentItem(mCount);
                    break;
                case RECORD:
                    mCount = msg.arg1;
                    break;
                default:
                    break;
            }

        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.banner_layout, container, false);

        initView(view);
        init();
        setListener();
        mAdapter = new MyPagerAdapter(mList);
        mViewPager.setAdapter(mAdapter);
        int i = Integer.MAX_VALUE / 2 % mList.size();
        // 默认在中间，让用户看不到边界
        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2 - i);
        handler.sendEmptyMessage(START);
        return view;
    }



    private void init() {
        // TODO Auto-generated method stub
        urlList = new ArrayList<String>();
        mList = new ArrayList<ImageView>();
        urlList.add(Url.urlShort+"/api/File/GetBanner?filename="+BannerInfo.name0);
        urlList.add(Url.urlShort+"/api/File/GetBanner?filename="+BannerInfo.name1);
        urlList.add(Url.urlShort+"/api/File/GetBanner?filename="+BannerInfo.name2);
        urlList.add(Url.urlShort+"/api/File/GetBanner?filename="+BannerInfo.name3);
        m = urlList.size();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).displayer(new RoundedBitmapDisplayer(50))
                .displayer(new FadeInBitmapDisplayer(100)).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageView imageView;
        for (int i = 0; i < m; i++) {
            imageView = new ImageView(getActivity());
            imageView.setScaleType(ScaleType.FIT_XY);
            // 使用的ImageLoader网络加载图片，需先在Application和清单文件中配置在使用
            ImageLoader.getInstance().displayImage(urlList.get(i), imageView,
                    options);
            mList.add(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    System.out.println("==========m==" + mCount % mList.size());
                    Intent intent  = new Intent(getActivity(),PublicActivity.class);
                    startActivity(intent);
                    j++;
                    // 这里写点击图片的操作 mCount % mList.size()这个点击的第几个图片
                }
            });
        }
    }

    private void initView(View view) {
        // TODO Auto-generated method stub
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        dot0 = (ImageView) view.findViewById(R.id.dot1);
        dot1 = (ImageView) view.findViewById(R.id.dot2);
        dot2 = (ImageView) view.findViewById(R.id.dot3);
        dot3 = (ImageView) view.findViewById(R.id.dot4);
        dots[0] = dot0;
        dots[1] = dot1;
        dots[2] = dot2;
        dots[3] = dot3;
        dot0.setSelected(true);
    }
    private void setListener() {
        // TODO Auto-generated method stub
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                handler.sendMessage(Message.obtain(handler, RECORD, arg0, 0));
                // 下面是控制点的变化
                int j = arg0 % mList.size();
                for (int i = 0; i < dots.length; i++) {
                    dots[i].setSelected(false);
                }
                dots[j].setSelected(true);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }
            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
                switch (arg0) {
                    // 当滑动时让当前轮播停止
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        handler.sendEmptyMessage(STOP);
                        break;
                    // 滑动停止时继续轮播
                    case ViewPager.SCROLL_STATE_IDLE:
                        handler.sendEmptyMessage(START);
                        break;
                }
            }
        });

    }




}
