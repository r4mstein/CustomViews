package com.example.r4mst.customviews;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.r4mst.customviews.util.logger.LogManager;
import com.example.r4mst.customviews.util.logger.Logger;
import com.example.r4mst.customviews.views.CircleMenu;
import com.example.r4mst.customviews.views.ProgressView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Logger mLogger;

    private ProgressView mProgressView;
    private CircleMenu mCircleMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogger = LogManager.getLogger();

//        mProgressView = (ProgressView) findViewById(R.id.progress_view);
//        mProgressView.startAnim();

        mCircleMenu = (CircleMenu) findViewById(R.id.circle_menu);

        List<Integer> icons = new ArrayList<>();
        icons.add(R.drawable.ic_n1);
        icons.add(R.drawable.ic_n2);
        icons.add(R.drawable.ic_n3);
        icons.add(R.drawable.ic_n4);
        icons.add(R.drawable.ic_n5);
//        icons.add(R.drawable.ic_n6);

        mCircleMenu.setIconsForMenu(icons);

        mCircleMenu.setItemClickListener(new CircleMenu.OnMenuItemClickListener() {
            @Override
            public void onItemClick(int itemId) {
                switch (itemId) {
                    case R.drawable.ic_n1:
                        mLogger.d(TAG, "onItemClick: ic_n1 clicked");
                        break;
                    case R.drawable.ic_n2:
                        mLogger.d(TAG, "onItemClick: ic_n2 clicked");
                        break;
                    case R.drawable.ic_n3:
                        mLogger.d(TAG, "onItemClick: ic_n3 clicked");
                        break;
                    case R.drawable.ic_n4:
                        mLogger.d(TAG, "onItemClick: ic_n4 clicked");
                        break;
                    case R.drawable.ic_n5:
                        mLogger.d(TAG, "onItemClick: ic_n5 clicked");
                        break;
//                    case R.drawable.ic_n6:
//                        mLogger.d(TAG, "onItemClick: ic_n6 clicked");
//                        break;
                }
            }
        });
    }
}
