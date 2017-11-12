package taewon.navercorp.integratedsns.today;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import taewon.navercorp.integratedsns.R;

/**
 * Created by USER on 2017-11-10.
 */

public class VerticalCardStackViewPager extends ViewPager {

    private ImageView mPicture;

    public VerticalCardStackViewPager(Context context) {
        super(context);
        initTransformer();
    }

    public VerticalCardStackViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTransformer();
    }

    private void initTransformer(){
        setPageTransformer(true, new CardStackTransformer());
        setOverScrollMode(OVER_SCROLL_NEVER);

        mPicture = (ImageView) this.findViewById(R.id.imageView_picture);
    }

    private class CardStackTransformer implements PageTransformer {

        @Override
        public void transformPage(View page, float position) {

            if (position < -1) {
//                page.setAlpha(1 + position * 0.2f);

            } else {
//                page.setAlpha(1);
                float yPosition = position * page.getHeight();
                page.setTranslationX(page.getWidth() * -position);
                page.setTranslationY(yPosition);
            }

            if (position >= 0) {
                page.setScaleX(0.8f - 0.04f * position);
                page.setScaleY(0.8f - 0.04f * position);
                page.setTranslationX(-page.getWidth() * position);
                page.setTranslationY(80 * position);

            }

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                page.setAlpha(1 + position * 0.5f);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.

            }


        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if(ev.getAction()==MotionEvent.ACTION_MOVE){

        }

        return super.onTouchEvent(swapXY(ev));
    }

    private MotionEvent swapXY(MotionEvent ev) {

        float width = getWidth();
        float height = getHeight();
        float newX = (ev.getY() / height) * width;
        float newY = (ev.getX() / width) * height;
        ev.setLocation(newX, newY);
        return ev;
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return false;
    }
}