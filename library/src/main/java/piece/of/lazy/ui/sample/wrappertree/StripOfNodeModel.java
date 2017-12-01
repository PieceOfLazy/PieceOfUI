package piece.of.lazy.ui.sample.wrappertree;

/**
 * Created by zpdl
 */

public interface StripOfNodeModel {

    interface OnNodeModelListener {
        StripOfNode onGetNodeInstance();
        void onChangedView();
        void onChangedExpand();
    }

    void setOnNodeModelListener(OnNodeModelListener l);

    boolean isView();

    boolean isExpand();

    void setView(boolean view);

    void setExpand(boolean expand);
}