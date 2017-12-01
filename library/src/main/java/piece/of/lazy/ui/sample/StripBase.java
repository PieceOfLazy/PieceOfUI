package piece.of.lazy.ui.sample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public abstract class StripBase {

    private Context mContext = null;
    private View mStripView = null;

    public StripBase() {
    }

    public View getStripView() {
        return mStripView;
    }

    public View createStripView(@NonNull Context context, ViewGroup parent, boolean addView) {
        mContext = context;
        mStripView = null;

        if(parent == null) {
            return null;
        }

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(inflater == null) {
            return null;
        }

        mStripView = onCreateView(inflater, parent);
        if(mStripView != null) {
            mStripView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    StripBase.this.onViewAttachedToWindow();
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    StripBase.this.onViewDetachedFromWindow();
                }
            });
            mStripView.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            mStripView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            StripBase.this.onGlobalLayout();
                        }
                    });

            if(addView) {
                parent.addView(mStripView);
            }
        }

        return mStripView;
    }

    protected abstract View onCreateView(LayoutInflater inflater, ViewGroup container);

    protected void onViewAttachedToWindow() {
    }

    protected void onViewDetachedFromWindow() {
    }

    protected void onGlobalLayout() {
    }

    protected Context getContext() {
        return mContext;
    }

    public boolean isVisible() {
        return mStripView != null && mStripView.getVisibility() == View.VISIBLE;
    }

    public int getVisibility() {
        if(mStripView != null) {
            return mStripView.getVisibility();
        }
        return View.GONE;
    }

    public void setVisibility(int visibility) {
        if(mStripView != null) {
            mStripView.setVisibility(visibility);
        }
    }
}
