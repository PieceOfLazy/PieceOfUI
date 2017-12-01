package piece.of.lazy.ui.sample.treemodel;

/**
 * Created by zpdl
 */

public interface IStripOfTreeModelLeaf {
    void setOnGetTreeListener(IStripOfTreeModelListener l);

    IStripOfLeaf getTreeInstance();
}