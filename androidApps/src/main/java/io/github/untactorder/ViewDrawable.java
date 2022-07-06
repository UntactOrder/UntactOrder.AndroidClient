package io.github.untactorder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * https://stackoverflow.com/questions/3972445/how-to-put-text-in-a-drawable
 */
public class ViewDrawable extends Drawable {
    final View mView;

    public ViewDrawable(final Context context, final @LayoutRes int layoutId) {
        this(LayoutInflater.from(context).inflate(layoutId, null));
    }

    public ViewDrawable(final @NonNull View view) {
        mView = view;
    }

    public View getView() {
        return mView;
    }

    public void setId(@IdRes int id) {
        mView.setId(id);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(right - left, View.MeasureSpec.EXACTLY);
        final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(bottom - top, View.MeasureSpec.EXACTLY);
        mView.measure(widthMeasureSpec, heightMeasureSpec);
        mView.layout(left, top, right, bottom);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        mView.draw(canvas);
    }

    @Override
    public void setAlpha(int alpha) {
        mView.setAlpha(alpha/255f);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
