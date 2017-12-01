package piece.of.lazy.ui.sample;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by zpdl.kim.
 */

public class StripOfAdapterSection {

    private final int mId;
    @NonNull
    private final List<SectionItem> mItems;

    public StripOfAdapterSection(int id, @NonNull List<SectionItem> items) {
        this.mId = id;
        this.mItems = items;
    }

    public int getId() {
        return mId;
    }

    @NonNull
    public List<SectionItem> getItems() {
        return mItems;
    }

    public int getSize() {
        return mItems.size();
    }

    public int getViewType(int index) {
        if(index >= 0 && index < mItems.size()) {
            return mItems.get(index).getViewType();
        }
        return 0;
    }

    public SectionItem getViewData(int index) {
        if(index >= 0 && index < mItems.size()) {
            return mItems.get(index);
        }
        return null;
    }

    public interface SectionItem {
        int getViewType();
    }
}
