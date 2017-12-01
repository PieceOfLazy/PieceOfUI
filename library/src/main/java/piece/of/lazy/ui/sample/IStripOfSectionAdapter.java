package piece.of.lazy.ui.sample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

/**
 * Created by zpdl
 */

public abstract class IStripOfSectionAdapter<T extends IStripOfSectionAdapter.Section> extends IStripOfAdapter {

    public IStripOfSectionAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    public final Object getBindItem(int position) {
        SectionPosition sectionPosition = getSectionPosition(position);
        if(sectionPosition != null) {
            T section = getSection(sectionPosition.getSectionPosition());
            if(sectionPosition.getFieldPosition() < 0) {
                return section;
            } else {
                return getField(sectionPosition.getSectionPosition(), sectionPosition.getFieldPosition());
            }
        }
        return null;
    }

    @Override
    public final int getItemCount() {
        int sectionCount = getSectionCount();
        int itemCount = 0;
        for(int i = 0; i < sectionCount; i++) {
            T section = getSection(i);
            if(section != null) {
                itemCount += ((section.isExpand() ? getFieldCount(i) : 0) + (section.hasView() ? 1 : 0));
            }
        }
        return itemCount;
    }

    @Override
    public final int getItemViewType(int position) {
        SectionPosition sectionPosition = getSectionPosition(position);
        if(sectionPosition != null) {
            if(sectionPosition.getFieldPosition() < 0) {
                return getSectionViewType(sectionPosition.getSectionPosition());
            } else {
                return getFieldViewType(sectionPosition.getSectionPosition(), sectionPosition.getFieldPosition());
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public final long getItemId(int position) {
        SectionPosition sectionPosition = getSectionPosition(position);
        if(sectionPosition != null) {
            int sectionId = getSectionId(sectionPosition.getSectionPosition());
            if(sectionPosition.getFieldPosition() < 0) {
                return composeSectionId(sectionId);
            } else {
                return composeFieldId(sectionId, getFieldId(sectionPosition.getSectionPosition(), sectionPosition.getFieldPosition()));
            }
        }
        return super.getItemViewType(position);
    }

    protected abstract int getSectionCount();

    protected abstract T getSection(int sectionPosition);

    protected abstract int getSectionViewType(int sectionPosition);

    protected abstract int getFieldCount(int sectionPosition);

    protected abstract Object getField(int sectionPosition, int fieldPosition);

    protected abstract int getFieldViewType(int sectionPosition, int fieldPosition);

    protected int getSectionId(int sectionPosition) {
        return sectionPosition;
    }

    protected int getFieldId(int sectionPosition, int fieldPosition) {
        return fieldPosition;
    }

    private long composeSectionId(int sectionId) {
        return (((long) sectionId) << 33) | 1L << 32;
    }

    private long composeFieldId(int sectionId, int fieldId) {
        return (((long) sectionId) << 33) | fieldId;
    }

    public Object getBindItem(RecyclerView.ViewHolder holder) {
        if(holder == null) {
            return null;
        }
        return getBindItem(holder.getAdapterPosition());
    }

    public SectionPosition getSectionPosition(int position) {
        int sectionCount = getSectionCount();
        int itemCount = 0;
        for(int i = 0; i < sectionCount; i++) {
            T section = getSection(i);
            if(section != null) {
                int sectionViewCount = section.hasView() ? 1 : 0;

                if(position < itemCount + sectionViewCount) {
                    return new SectionPosition(i, -1);
                } else if(section.isExpand()) {
                    int fieldCount = getFieldCount(i);
                    if(position < itemCount + fieldCount + sectionViewCount) {
                        return new SectionPosition(i, position - itemCount - sectionViewCount);
                    }
                    itemCount += (fieldCount + sectionViewCount);
                } else {
                    itemCount += sectionViewCount;
                }
            }
        }
        return null;
    }

    private int convertPositionFromSectionPosition(int sectionPosition) {
        int sectionCount = getSectionCount();
        int itemCount = 0;
        for(int i = 0; i < sectionCount; i++) {
            T section = getSection(i);
            if(section != null) {
                if(i == sectionPosition) {
                    return itemCount;
                }
                itemCount += ((section.isExpand() ? getFieldCount(i) : 0) + (section.hasView() ? 1 : 0));
            }
        }
        return -1;
    }

    public class SectionPosition {
        private final int mSectionPosition;
        private final int mFieldPosition;

        public SectionPosition(int sectionPosition, int fieldPosition) {
            this.mSectionPosition = sectionPosition;
            this.mFieldPosition = fieldPosition;
        }

        public int getSectionPosition() {
            return mSectionPosition;
        }

        public int getFieldPosition() {
            return mFieldPosition;
        }
    }

    public void expandSection(int sectionPosition) {
        T section = getSection(sectionPosition);
        int sectionStartPosition = convertPositionFromSectionPosition(sectionPosition);
        if(section != null && sectionStartPosition >= 0) {
            if(!section.isExpand()) {
                section.setExpand(true);
                int fieldCount = getFieldCount(sectionPosition);
                if(section.hasView()) {
                    Object obj = getBindItem(sectionStartPosition);
                    if(obj != null)
                        notifyItemChanged(sectionStartPosition);
                    sectionStartPosition++;
                }

                if(fieldCount > 0) {
                    notifyItemRangeInserted(sectionStartPosition, fieldCount);
                }
            }
        }
    }

    public void collapseSection(int sectionPosition) {
        T section = getSection(sectionPosition);
        int sectionStartPosition = convertPositionFromSectionPosition(sectionPosition);
        if(section != null && sectionStartPosition >= 0) {
            if(section.isExpand()) {
                section.setExpand(false);
                int fieldCount = getFieldCount(sectionPosition);
                if(section.hasView()) {
                    notifyItemChanged(sectionStartPosition);
                    sectionStartPosition++;
                }

                if(fieldCount > 0) {
                    notifyItemRangeRemoved(sectionStartPosition, fieldCount);
                }
            }
        }
    }

    public void notifySectionChanged(int sectionPosition) {
        T section = getSection(sectionPosition);
        int sectionStartPosition = convertPositionFromSectionPosition(sectionPosition);
        if(section != null && sectionStartPosition >= 0) {
            if(section.hasView()) {
                notifyItemChanged(sectionStartPosition);
            }
        }
    }

    public void notifySectionInserted(int sectionPosition) {
        T section = getSection(sectionPosition);
        int sectionStartPosition = convertPositionFromSectionPosition(sectionPosition);
        if(section != null && sectionStartPosition >= 0) {
            int insertedCount = section.hasView() ? 1 : 0;
            if(section.isExpand()) {
                insertedCount += getFieldCount(sectionPosition);
            }
            if(insertedCount > 0) {
                notifyItemRangeInserted(sectionStartPosition, insertedCount);
            }
        }
    }

    public void notifySectionRemoved(int sectionPosition, int sectionItemCount) {
        if(sectionItemCount > 0) {
            int sectionStartPosition = convertPositionFromSectionPosition(sectionPosition);
            if (sectionStartPosition < 0) {
                int itemCount = getItemCount();
                if(itemCount <= 0) {
                    notifyDataSetChanged();
                } else {
                    notifyItemRangeRemoved(itemCount, sectionItemCount);
                }
            } else {
                notifyItemRangeRemoved(sectionStartPosition, sectionItemCount);
            }
        }
    }

    public void notifyFieldChanged(int sectionPosition, int fieldPosition) {
        notifyFieldRangeChanged(sectionPosition, fieldPosition, 1);
    }

    public void notifyFieldRangeChanged(int sectionPosition, int fieldPosition, int count) {
        T section = getSection(sectionPosition);
        int sectionStartPosition = convertPositionFromSectionPosition(sectionPosition);
        if(section != null && sectionStartPosition >= 0) {
            if(section.isExpand()) {
                int fieldCount = getFieldCount(sectionPosition);
                if(section.hasView()) {
                    sectionStartPosition++;
                }

                if(fieldPosition >= 0 && fieldPosition + count <= fieldCount) {
                    notifyItemRangeChanged(sectionStartPosition + fieldPosition, count);
                }
            }
        }
    }

    public void notifyFieldInserted(int sectionPosition, int fieldPosition) {
        notifyFieldRangeInserted(sectionPosition, fieldPosition, 1);
    }

    public void notifyFieldRangeInserted(int sectionPosition, int fieldPosition, int count) {
        T section = getSection(sectionPosition);
        int sectionStartPosition = convertPositionFromSectionPosition(sectionPosition);
        if(section != null && sectionStartPosition >= 0) {
            if(section.isExpand()) {
                int fieldCount = getFieldCount(sectionPosition);
                if(section.hasView()) {
                    sectionStartPosition++;
                }

                if(fieldPosition >= 0 && fieldPosition + count <= fieldCount) {
                    notifyItemRangeInserted(sectionStartPosition + fieldPosition, count);
                }
            }
        }
    }

    public void notifyFieldRemoved(int sectionPosition, int fieldPosition) {
        notifyFieldRangeRemoved(sectionPosition, fieldPosition, 1);
    }

    public void notifyFieldRangeRemoved(int sectionPosition, int fieldPosition, int count) {
        T section = getSection(sectionPosition);
        int sectionStartPosition = convertPositionFromSectionPosition(sectionPosition);
        if(section != null && sectionStartPosition >= 0) {
            if(section.isExpand()) {
                if(section.hasView()) {
                    sectionStartPosition++;
                }

                if(fieldPosition >= 0) {
                    notifyItemRangeRemoved(sectionStartPosition + fieldPosition, count);
                }
            }
        }
    }

    public interface Section {

        boolean isExpand();

        void setExpand(boolean expand);

        boolean hasView();
    }
}
