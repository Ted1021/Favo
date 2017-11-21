package taewon.navercorp.integratedsns.customview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by tedkim on 2017. 11. 11..
 */

public class ParallaxViewPager extends ViewPager {

    public ParallaxViewPager(Context context) {
        super(context);
        initTransformer();
    }

    public ParallaxViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTransformer();
    }

    private void initTransformer(){
        setPageTransformer(true, new ParallaxTransformer());
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public class ParallaxTransformer implements PageTransformer {

        @Override
        public void transformPage(View page, float position) {
            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.setAlpha(0.1f);
                page.setScaleX(0.8f);
                page.setScaleY(0.8f);

            } else if (position < 0) { // [-1,0]
                // Fade the page in.
                page.setAlpha(1 + position * 0.5f);
                page.setScaleX(1 + 0.3f * position);
                page.setScaleY(1 + 0.3f * position);

            } else if (position == 0) {
                // Page in stable (on Center)
                page.setAlpha(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                page.setAlpha(1 - position * 0.5f);
                page.setScaleX(1 - 0.3f * position);
                page.setScaleY(1 - 0.3f * position);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                page.setAlpha(0.1f);
                page.setScaleX(0.8f);
                page.setScaleY(0.8f);
            }
        }
    }
}
