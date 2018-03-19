package hk.meiyan.com.admissionanimation;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 54hk on 2018/3/19.
 */

public class GuideViewPager extends ViewPager {
    private int startX;
    private int startY;

    public GuideViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GuideViewPager(Context context) {
        super(context);
    }


    /**
     * 重写onTouchEvent事件,什么都不用做
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);// 不要拦截,
                // 这样是为了保证ACTION_MOVE调用
                startX = (int) ev.getRawX();
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:

                int endX = (int) ev.getRawX();
                int endY = (int) ev.getRawY();

                if (Math.abs(endX - startX) > Math.abs(endY - startY)) {// 左右滑动
                    if (startX - endX > 100) {/// 左划
                        if (getCurrentItem() == getAdapter().getCount() - 1) {// 最后一个页面,
                            // 需要拦截
                            if (mLastLeftMoveListener != null) {
                                mLastLeftMoveListener.onLastLeftMove();
                            }
                        }
                    }
                }
                break;
        }

        return super.onTouchEvent(ev);
    }

    OnLastLeftMoveListener mLastLeftMoveListener;

    public void setOnLastListener(OnLastLeftMoveListener mLastLeftMoveListener) {
        this.mLastLeftMoveListener = mLastLeftMoveListener;
    }

    public interface OnLastLeftMoveListener {
        void onLastLeftMove();
    }
}
