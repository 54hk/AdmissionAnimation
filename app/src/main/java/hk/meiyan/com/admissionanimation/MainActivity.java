package hk.meiyan.com.admissionanimation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements ViewPager.OnPageChangeListener {

    RelativeLayout rlRoot;
    Button btnExperience;
    LinearLayout llGuidePoint;
    View viewRedPoint;
    GuideViewPager vpGuide;
    private int mPointWidth;// 圆点间的距离
    private boolean isFirst;
    private int[] mImageIds;
    private ArrayList<View> imageList;
    private ImageView ivAnimator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyAPP.context = this;
        //  找资源ID
        initId();
        initLintener();
        //  判断是否第一次登录 isFirst
        initPointViews();
        vpGuide.setAdapter(new GuideAdapter());
        vpGuide.setOffscreenPageLimit(0);


        imageList = new ArrayList<>();
        for (int i = 0; i < mImageIds.length; i++) {
            View  llGuide = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_guide_pager, null);
            imageList.add(llGuide);
        }
//       开始时候，索引是0的动画
//        ImageView ivGuide = imageList.get(0).findViewById(R.id.iv_guide);
//        startImageAnimation(ivGuide, 2000, 1.2f);
    }


    private void initLintener() {
        btnExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                jumpToNextPager();
            }
        });
        vpGuide.setOnLastListener(new GuideViewPager.OnLastLeftMoveListener() {
            @Override
            public void onLastLeftMove() {
//                跳转下一个Activity
                jumpToNextPager();
            }
        });
        vpGuide.addOnPageChangeListener(this);
    }
    // 跳转下一个Activity
    private void jumpToNextPager() {
    }

    private void initId() {
        rlRoot = findViewById(R.id.rl_root);
        btnExperience = findViewById(R.id.btn_experience);
        llGuidePoint = findViewById(R.id.ll_guide_point);
        viewRedPoint = findViewById(R.id.view_red_point);
        vpGuide = findViewById(R.id.vp_guide);
    }
    //  img图片放大的效果
    private void startImageAnimation(ImageView img, int duration, float f) {
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(img, "scaleX", 1.0f, f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(img, "scaleY", 1.0f, f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(duration);
        animSet.setInterpolator(new LinearInterpolator());
        //两个动画同时执行
        animSet.playTogether(scaleXAnim, scaleYAnim);
        animSet.start();
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // System.out.println("当前位置:" + position + ";百分比:" + positionOffset
        // + ";移动距离:" + positionOffsetPixels);
        int len = (int) (mPointWidth * positionOffset) + position
                * mPointWidth;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewRedPoint
                .getLayoutParams();// 获取当前红点的布局参数
        params.leftMargin = len;// 设置左边距

        viewRedPoint.setLayoutParams(params);// 重新给小红点设置布局参数
    }

    @Override
    public void onPageSelected(int position) {
//        ImageView viewById = imageList.get(position).findViewById(R.id.iv_guide);
//            startImageAnimation(viewById, 5000, 1.2f);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    /**
     * ViewPager数据适配器
     */
    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageIds.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = imageList.get(position);
            container.addView(view);
            ivAnimator = view.findViewById(R.id.iv_guide);
            ivAnimator.setImageResource(mImageIds[position]);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    // ViewPager
    private void initPointViews() {

        mImageIds = new int[]{
                R.mipmap.guide1, R.mipmap.guide2,
                R.mipmap.guide3, R.mipmap.guide4};

        // 初始化引导页的小圆点
        for (int i = 0; i < mImageIds.length; i++) {

            View point = new View(this);
            point.setBackgroundResource(R.drawable.shape_indicator_unselected_oval);// 设置引导页默认圆点

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DensityUtils.dp2px(this, 18), DensityUtils.dp2px(this, 1));
            if (i > 0) {
                params.leftMargin = DensityUtils.dp2px(this, 5);// 设置圆点间隔
            }

            point.setLayoutParams(params);// 设置圆点的大小

            llGuidePoint.addView(point);// 将圆点添加给线性布局
        }

        // 获取视图树, 对layout结束事件进行监听
        llGuidePoint.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    // 当layout执行结束后回调此方法
                    @Override
                    public void onGlobalLayout() {
                        llGuidePoint.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                        mPointWidth = llGuidePoint.getChildAt(1).getLeft()
                                - llGuidePoint.getChildAt(0).getLeft();
                    }
                });
        vpGuide.addOnPageChangeListener(this);
    }
}
