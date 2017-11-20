package taewon.navercorp.integratedsns.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static android.view.Gravity.FILL_HORIZONTAL;
import static android.view.Gravity.FILL_VERTICAL;

/**
 * Created by tedkim on 2017. 11. 18..
 */

public class GridImageView extends GridLayout {

    private int mWidth, mHeight, mImageCount;
    private ArrayList<Photo> mImageset = new ArrayList<>();

    private GridLayout.LayoutParams mHeaderItemParam;
    private ArrayList<GridLayout.LayoutParams> mBodyItemParam = new ArrayList<>();

    private int mOrientation;

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

        mImageset = imageset;
        mWidth = mImageset.get(0).getWidth();
        mHeight = mImageset.get(0).getHeight();
        mImageCount = mImageset.size();
        mOrientation = (mWidth > mHeight) ? FILL_HORIZONTAL : FILL_VERTICAL;

        if (mOrientation == FILL_HORIZONTAL) {
            this.setOrientation(HORIZONTAL);

        } else {
            this.setOrientation(VERTICAL);
        }

        if (mImageCount == 2 || mImageCount == 3) {
            this.setColumnCount(2);
            this.setRowCount(2);

        } else {
            this.setColumnCount(3);
            this.setRowCount(3);
        }

        initHeaderImageFrame();
        initBodyImageFrame();
        setImages();
    }

    private void initHeaderImageFrame() {

        GridLayout.Spec headerRow, headerCol;
        if (mOrientation == FILL_HORIZONTAL) {

            if (mImageCount == 2 || mImageCount == 3) {
                headerRow = GridLayout.spec(0, 1, 1.0f);
                headerCol = GridLayout.spec(0, 2, 2.0f);

            } else {
                headerRow = GridLayout.spec(0, 2, 2.0f);
                headerCol = GridLayout.spec(0, 3, 3.0f);
            }
        } else {

            if (mImageCount == 2 || mImageCount == 3) {
                headerRow = GridLayout.spec(0, 2, 2.0f);
                headerCol = GridLayout.spec(0, 1, 1.0f);
            } else {
                headerRow = GridLayout.spec(0, 3, 3.0f);
                headerCol = GridLayout.spec(0, 2, 2.0f);
            }
        }
        mHeaderItemParam = new GridLayout.LayoutParams(headerRow, headerCol);
        mHeaderItemParam.setGravity(mOrientation);
    }

    private void initBodyImageFrame() {

        int spanCount = (mImageCount <= 4) ? mImageCount : 4;
        for (int i = 0; i < spanCount - 1; i++) {

            GridLayout.Spec bodyRow, bodyCol;
            GridLayout.LayoutParams data;

            if (mOrientation == FILL_HORIZONTAL) {

                if (mImageCount == 2) {

                    bodyRow = GridLayout.spec(1, 1, 1.0f);
                    bodyCol = GridLayout.spec(i,2,1.0f);

                } else if (mImageCount == 3) {

                    bodyRow = GridLayout.spec(1,1,1.0f);
                    bodyCol = GridLayout.spec(i,1,1.0f);

                } else {

                    bodyRow = GridLayout.spec(2,1,1.0f);
                    bodyCol = GridLayout.spec(i,1,1.0f);
                }

            } else {

                if (mImageCount == 2) {

                    bodyRow = GridLayout.spec(i,2,1.0f);
                    bodyCol = GridLayout.spec(1,1,1.0f);

                } else if (mImageCount == 3) {

                    bodyRow = GridLayout.spec(i,1,1.0f);
                    bodyCol = GridLayout.spec(1,1,1.0f);

                } else {

                    bodyRow = GridLayout.spec(i,1,1.0f);
                    bodyCol = GridLayout.spec(2,1,1.0f);
                }
            }

            data = new GridLayout.LayoutParams(bodyRow, bodyCol);
            data.setGravity(mOrientation);
            mBodyItemParam.add(data);
        }
    }

    private void setImages() {

        int spanCount = (mImageCount <= 4) ? mImageCount : 4;

        for (int i = 0; i < spanCount; i++) {

            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(4,4,4,4);

            if (i == 0) {
                imageView.setLayoutParams(mHeaderItemParam);
                this.addView(imageView, mHeaderItemParam);
            } else {
                imageView.setLayoutParams(mBodyItemParam.get(i - 1));
                this.addView(imageView, mBodyItemParam.get(i - 1));
            }

            Glide.with(getContext()).load(mImageset.get(i).getSrc()).into(imageView);
        }
    }
}
