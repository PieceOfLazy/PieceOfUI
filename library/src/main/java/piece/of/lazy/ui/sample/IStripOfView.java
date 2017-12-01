package piece.of.lazy.ui.sample;

import android.content.Context;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class IStripOfView<Q> {
    protected View mView = null;
    protected Q mItem = null;

    public final View createView(Context context, ViewGroup container) {
        if(context == null) {
            return null;
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(inflater == null) {
            return null;
        }

        View view = onCreateStripOfView(inflater, container);
        if(view == null) {
            return null;
        }
        container.addView(view);
        bindView(view);

        return view;
    }

    public void bindView(View parent, @IdRes int id) {
        if(parent != null) {
            mView = parent.findViewById(id);
            if(mView != null) {
                onBindStripOfView(mView);
            }
        }
    }

    public void bindView(@NonNull View view) {
        mView = view;
        onBindStripOfView(mView);
    }

    public void bindItem(@NonNull Context context, Q item) {
        if(mView == null) {
            return;
        }
        mItem = item;
        onBindStripOfItem(context, item);
    }

    public void setVisibility(int visibility) {
        if(mView != null) {
            mView.setVisibility(visibility);
        }
    }

    public int getVisibility() {
        if(mView != null) {
            return mView.getVisibility();
        }
        return View.GONE;
    }

    public void setAlpha(@FloatRange(from=0.0, to=1.0) float alpha) {
        if(mView != null) {
            mView.setAlpha(alpha);
        }
    }

    public Q getItem() {
        return mItem;
    }

    @LayoutRes
    protected abstract int stripOfLayout();

    protected final View onCreateStripOfView(@NonNull LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(stripOfLayout(), container, false);
    }

    protected abstract void onBindStripOfView(@NonNull View view);

    protected abstract void onBindStripOfItem(@NonNull Context context, Q item);
}
