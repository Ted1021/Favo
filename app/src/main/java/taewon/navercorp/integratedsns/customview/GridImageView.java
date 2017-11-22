package taewon.navercorp.integratedsns.customview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import taewon.navercorp.integratedsns.model.favo.Photo;

import static android.view.Gravity.FILL_HORIZONTAL;
import static android.view.Gravity.FILL_VERTICAL;

/**
 * Created by tedkim on 2017. 11. 18..
 */

public class GridImageView extends GridLayout {

    private GridLayout.LayoutParams mHeaderItemParam;
    private ArrayList<GridLayout.LayoutParams> mBodyItemParam = new ArrayList<>();

    public GridImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public GridImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public GridImageView(Context context) {
        super(context);

    }

    public void initView(ArrayList<Photo> imageset) {

        int width, height, imageCount;
        int orientation;
        ArrayList<Photo> mImageset = imageset;

        width = mImageset.get(0).getWidth();
        height = mImageset.get(0).getHeight();
        imageCount = mImageset.size();
        orientation = (width > height) ? FILL_HORIZONTAL : FILL_VERTICAL;

        if (orientation == FILL_HORIZONTAL) {
            this.setOrientation(HORIZONTAL);

        } else {
            this.setOrientation(VERTICAL);
        }

        if (imageCount == 2 || imageCount == 3) {

            this.setColumnCount(2);
            this.setRowCount(2);

        } else {
            this.setColumnCount(3);
            this.setRowCount(3);
        }

        initHeaderImageFrame(imageCount, orientation);
        initBodyImageFrame(imageCount, orientation);
        setImages(imageCount, imageset);
    }

    private void initHeaderImageFrame(int imageCount, int orientation) {

        GridLayout.Spec headerRow, headerCol;
        if (orientation == FILL_HORIZONTAL) {

            if (imageCount == 2 || imageCount == 3) {
                headerRow = GridLayout.spec(0, 1, 1.0f);
                headerCol = GridLayout.spec(0, 2, 2.0f);

            } else {
                headerRow = GridLayout.spec(0, 2, 2.0f);
                headerCol = GridLayout.spec(0, 3, 3.0f);
            }
        } else {

            if (imageCount == 2 || imageCount == 3) {
                headerRow = GridLayout.spec(0, 2, 2.0f);
                headerCol = GridLayout.spec(0, 1, 1.0f);
            } else {
                headerRow = GridLayout.spec(0, 3, 3.0f);
                headerCol = GridLayout.spec(0, 2, 2.0f);
            }
        }
        mHeaderItemParam = new GridLayout.LayoutParams(headerRow, headerCol);
        mHeaderItemParam.setGravity(orientation);
    }

    private void initBodyImageFrame(int imageCount, int orientation) {

        mBodyItemParam.clear();
        int spanCount = (imageCount <= 4) ? imageCount : 4;
        for (int i = 0; i < spanCount - 1; i++) {

            GridLayout.Spec bodyRow, bodyCol;
            GridLayout.LayoutParams data;

            if (orientation == FILL_HORIZONTAL) {

                if (imageCount == 2) {

                    bodyRow = GridLayout.spec(1, 1, 1.0f);
                    bodyCol = GridLayout.spec(i, 2, 1.0f);

                } else if (imageCount == 3) {

                    bodyRow = GridLayout.spec(1, 1, 1.0f);
                    bodyCol = GridLayout.spec(i, 1, 1.0f);

                } else {

                    bodyRow = GridLayout.spec(2, 1, 1.0f);
                    bodyCol = GridLayout.spec(i, 1, 1.0f);
                }

            } else {

                if (imageCount == 2) {

                    bodyRow = GridLayout.spec(i, 2, 1.0f);
                    bodyCol = GridLayout.spec(1, 1, 1.0f);

                } else if (imageCount == 3) {

                    bodyRow = GridLayout.spec(i, 1, 1.0f);
                    bodyCol = GridLayout.spec(1, 1, 1.0f);

                } else {

                    bodyRow = GridLayout.spec(i, 1, 1.0f);
                    bodyCol = GridLayout.spec(2, 1, 1.0f);
                }
            }

            data = new GridLayout.LayoutParams(bodyRow, bodyCol);
            data.setGravity(orientation);
            mBodyItemParam.add(data);
        }
    }

    private void setImages(int imageCount, ArrayList<Photo> imageset) {

        int spanCount = (imageCount <= 4) ? imageCount : 4;
        for (int i = 0; i < spanCount; i++) {

            ImageView imageView = new ImageView(getContext());
            imageView.setPadding(4, 4, 4, 4);

            if (i == 0) {
                imageView.setLayoutParams(mHeaderItemParam);
                this.addView(imageView, mHeaderItemParam);
            } else {
                imageView.setLayoutParams(mBodyItemParam.get(i - 1));
                this.addView(imageView, mBodyItemParam.get(i - 1));
            }

            Glide.with(getContext())
                    .load(imageset.get(i).getSrc())
                    .apply(new RequestOptions().placeholder(new ColorDrawable(Color.BLACK)))
                    .apply(new RequestOptions().centerCrop())
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(imageView);
        }
    }
}
