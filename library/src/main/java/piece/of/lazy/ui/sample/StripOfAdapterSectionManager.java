package piece.of.lazy.ui.sample;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by zpdl.kim.
 */

public class StripOfAdapterSectionManager {

    public interface StripOfAdapterSectionManagerListener {
        void onNotifyDataSetChanged();

        void onNotifyItemRangeChanged(int positionStart, int itemCount);

        void onNotifyItemRangeInserted(int positionStart, int itemCount);

        void onNotifyItemRangeRemoved(int positionStart, int itemCount);

        List<StripOfAdapterSection> onGetSectionList();
    }

    @NonNull
    private StripOfAdapterSectionManagerListener mListener;

    public StripOfAdapterSectionManager(@NonNull StripOfAdapterSectionManagerListener listener) {
        this.mListener = listener;
    }

    public StripOfAdapterSection getSectionFromId(int id) {
        List<StripOfAdapterSection> list = mListener.onGetSectionList();
        if(list != null) {
            for (StripOfAdapterSection section : list) {
                if(section.getId() == id) {
                    return section;
                }
            }
        }
        return null;
    }

    public StripOfAdapterSection getSectionFromIndex(int index) {
        List<StripOfAdapterSection> list = mListener.onGetSectionList();
        if(list != null && index >= 0 && index < list.size()) {
            return list.get(index);
        }
        return null;
    }

    public int getItemCount() {
        List<StripOfAdapterSection> list = mListener.onGetSectionList();
        int itemCount = 0;
        if(list != null) {
            for (StripOfAdapterSection section : list) {
                itemCount += section.getSize();
            }
        }
        return itemCount;
    }

    public int getViewType(int position) {
        List<StripOfAdapterSection> list = mListener.onGetSectionList();
        if(list != null) {
            int itemCount = 0;
            for (StripOfAdapterSection section : list) {
                if (position < itemCount + section.getSize()) {
                    return section.getViewType(position - itemCount);
                }
                itemCount += section.getSize();
            }
        }
        return 0;
    }

    public StripOfAdapterSection.SectionItem getViewItem(int position) {
        List<StripOfAdapterSection> list = mListener.onGetSectionList();
        if(list != null) {
            int itemCount = 0;
            for (StripOfAdapterSection section : list) {
                if (position < itemCount + section.getSize()) {
                    return section.getViewData(position - itemCount);
                }
                itemCount += section.getSize();
            }
        }
        return null;
    }

    public SectionPosition getSectionPosition(int position) {
        List<StripOfAdapterSection> list = mListener.onGetSectionList();
        if(list != null) {
            int itemCount = 0;
            for (int i = 0; i > list.size(); i++) {
                StripOfAdapterSection section = list.get(i);
                if (position < itemCount + section.getSize()) {
                    return new SectionPosition(section.getId(), i, position - itemCount);
                }
                itemCount += section.getSize();
            }
        }
        return null;
    }

    public void notifyDataSetChanged() {
        mListener.onNotifyDataSetChanged();
    }

    public void notifyItemRangeChanged(int id, int positionStart, int itemCount) {
        int startPosition = getSectionStartPosition(id);
        if(startPosition >= 0) {
            mListener.onNotifyItemRangeChanged(startPosition + positionStart, itemCount);
        }
    }

    public void notifyItemRangeInserted(int id, int positionStart, int itemCount) {
        int startPosition = getSectionStartPosition(id);
        if(startPosition >= 0) {
            mListener.onNotifyItemRangeInserted(startPosition + positionStart, itemCount);
        }
    }

    public void notifyItemRangeRemoved(int id, int positionStart, int itemCount) {
        int startPosition = getSectionStartPosition(id);
        if(startPosition >= 0) {
            mListener.onNotifyItemRangeRemoved(startPosition + positionStart, itemCount);
        }
    }

    private int getSectionStartPosition(int id) {
        List<StripOfAdapterSection> list = mListener.onGetSectionList();
        if(list != null) {
            int itemCount = 0;
            for (StripOfAdapterSection section : list) {
                if(section.getId() == id) {
                    return itemCount;
                }
                itemCount += section.getSize();
            }
        }
        return -1;
    }

    public class SectionPosition {
        private final int mSectionId;
        private final int mSectionIndex;
        private final int mItemIndex;

        public SectionPosition(int sectionId, int sectionIndex, int itemIndex) {
            this.mSectionId = sectionId;
            this.mSectionIndex = sectionIndex;
            this.mItemIndex = itemIndex;
        }

        public int getSectionId() {
            return mSectionId;
        }

        public int getSectionIndex() {
            return mSectionIndex;
        }

        public int getItemIndex() {
            return mItemIndex;
        }
    }
}
