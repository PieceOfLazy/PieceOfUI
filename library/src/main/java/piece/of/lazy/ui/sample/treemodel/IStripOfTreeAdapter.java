package piece.of.lazy.ui.sample.treemodel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;

import com.seilcre.lib.stripUI.IStripOfAdapter;
import com.seilcre.lib.stripUI.IStripOfHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zpdl
 */

public abstract class IStripOfTreeAdapter extends IStripOfAdapter implements IStripOfTreeNotify {

    public IStripOfTreeAdapter(@NonNull Context context) {
        super(context);
    }

    protected abstract IStripOfNode getRootNode();

    public IStripOfLeaf getLeaf(RecyclerView.ViewHolder holder) {
        if(holder == null) {
            return null;
        }

        Object obj = getBindItem(holder.getAdapterPosition());
        if(obj != null && obj instanceof IStripOfLeaf) {
            return (IStripOfLeaf) obj;
        }

        return null;
    }

    @Override
    protected final void onBindHolderInterface(SparseArray<IStripOfHolder> list) {
        List<IStripOfHolder> bindHolder = new ArrayList<>();
        onBindHolderInterface(bindHolder);

        for(IStripOfHolder holder : bindHolder) {
            list.put(holder.getViewType(), holder);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        IStripOfHolder recyclerHolderInterface = mRecyclerHolderInterfaceList.get(holder.getItemViewType());
        if(recyclerHolderInterface != null) {
            Index index = getIndex(getRootNode(), position);
            IStripOfLeaf leaf = getLeaf(getRootNode(), index);
            recyclerHolderInterface.onBindViewHolder(mContext, holder, leaf != null ? leaf.getModel() : null, index);
        }
    }

    @Override
    public Object getBindItem(int position) {
        IStripOfLeaf leaf = getLeaf(getRootNode(), position);
        if(leaf != null) {
            return leaf.getModel();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        IStripOfNode node = getRootNode();
        if(node != null) {
            return node.getCount();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        IStripOfLeaf leaf = getLeaf(getRootNode(), position);
        if (leaf != null) {
            for(int i = 0; i < mRecyclerHolderInterfaceList.size(); i++) {
                IStripOfHolder holderInterface = mRecyclerHolderInterfaceList.valueAt(i);
                if(holderInterface != null && holderInterface.isBindItem(leaf.getModel())) {
                    return holderInterface.getViewType();
                }
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        Index index = getIndex(getRootNode(), position);
        if(index != null) {
            IStripOfNode node = getNode(getRootNode(), getIndex(getRootNode(), position));
            if (node != null) {
                if(index.getLeafIndex() >= 0) {
                    if(node instanceof StripOfNode) {
                        IStripOfLeaf leaf = ((StripOfNode) node).getLeaf(index.getLeafIndex());
                        if (leaf != null) {
                            return (((long) node.getStripId()) << Integer.SIZE) | leaf.getStripId();
                        }
                    }
                } else {
                    return (((long) node.getStripId()) << Integer.SIZE);
                }
            }
        }
        return super.getItemId(position);
    }

    @Override
    public int getNodePosition(IStripOfNode node) {
        return getNodePosition(getRootNode(), node);
    }

    @Override
    public void notifyDataSetChangedAll() {
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataChanged(int position, int count) {
        notifyItemRangeChanged(position, count);
    }

    @Override
    public void notifyDataInserted(int position, int count) {
        notifyItemRangeInserted(position, count);
    }

    @Override
    public void notifyDataRemoved(int position, int count) {
        notifyItemRangeRemoved(position, count);
    }

    public abstract void onBindHolderInterface(List<IStripOfHolder> list);

    private IStripOfLeaf getLeaf(IStripOfNode node, int position) {
        if(node == null || position < 0) {
            return null;
        }

        int leafCount = 0;
        if(node.hasViewType()) {
            if(position == 0) {
                return node;
            }
            leafCount++;
        }

        if(node instanceof StripOfTree) {
            StripOfTree tree = (StripOfTree) node;
            for(int i = 0; i < tree.getNodeCount(); i++) {
                IStripOfNode child = tree.getNode(i);
                if(child != null) {
                    if (position < (leafCount + child.getCount())) {
                        return getLeaf(child, position - leafCount);
                    }
                    leafCount += child.getCount();
                }
            }
        } else if(node instanceof StripOfNode) {
            if(position < node.getCount()) {
                return ((StripOfNode) node).getLeaf(position - leafCount);
            }
        }

        return null;
    }

    private IStripOfLeaf getLeaf(IStripOfNode parent, Index index) {
        IStripOfNode node = getNode(parent, index);
        if(node != null) {
            int leafIndex = index.getLeafIndex();
            if(leafIndex < 0) {
                return node;
            } else if(node instanceof StripOfNode){
                return ((StripOfNode) node).getLeaf(leafIndex);
            }
        }

        return null;
    }

    private IStripOfNode getNode(IStripOfNode parent, Index index) {
        if(parent == null || index == null) {
            return null;
        }

        if(index.getNodeIndex() == null) {
            return parent;
        }

        IStripOfNode result = parent;
        int[] nodeIndex = index.getNodeIndex();
        for(int tmpIndex : nodeIndex) {
            if(result instanceof StripOfTree) {
                result = ((StripOfTree) result).getNode(tmpIndex);
                if(result == null) {
                    return null;
                }
            }
        }

        return result;
    }

    private int getNodeIndex(IStripOfNode node, int position) {
        if(node == null || position < 0) {
            return -1;
        }

        int leafCount = 0;
        if(node.hasViewType()) {
            if(position == 0) {
                return 0;
            }
            leafCount++;
        }

        if(node instanceof StripOfTree) {
            StripOfTree tree = (StripOfTree) node;
            for(int i = 0; i < tree.getNodeCount(); i++) {
                IStripOfNode child = tree.getNode(i);
                if(child != null) {
                    if (position < (leafCount + child.getCount())) {
                        int childNodeIndex = getNodeIndex(child, position - leafCount);
                        if(childNodeIndex < 0) {
                            return -1;
                        } else {
                            return i + getNodeIndex(child, position - leafCount);
                        }
                    }
                    leafCount += child.getCount();
                }
            }
        } else if(node instanceof StripOfNode) {
            if(position < node.getCount()) {
                return 0;
            }
        }

        return -1;
    }

    private int getNodePosition(IStripOfNode parent, IStripOfNode node) {
        if(parent == null || node == null) {
            return -1;
        }

        if(parent.equals(node)) {
            return 0;
        }

        int position = 0;
        if(parent.hasViewType()) {
            position++;
        }

        if(parent instanceof StripOfTree) {
            StripOfTree tree = (StripOfTree) parent;
            for(int i = 0; i < tree.getNodeCount(); i++) {
                IStripOfNode child = tree.getNode(i);
                if(child != null) {
                    if (child.equals(node)) {
                        return position;
                    }

                    if(child instanceof StripOfTree) {
                        int childPosition = getNodePosition(child, node);
                        if(childPosition >= 0) {
                            return position + childPosition;
                        }
                    }
                    position += child.getCount();
                }
            }
        }

        return -1;
    }

    private Index getIndex(IStripOfNode node, int position) {
        if(node == null || position < 0) {
            return null;
        }

        int leafCount = 0;
        if(node.hasViewType()) {
            if(position == 0) {
                return new Index(null, -1);
            }
            leafCount++;
        }

        if(node instanceof StripOfTree) {
            StripOfTree tree = (StripOfTree) node;
            for(int i = 0; i < tree.getNodeCount(); i++) {
                IStripOfNode child = tree.getNode(i);
                if(child != null) {
                    if (position < (leafCount + child.getCount())) {
                        return composeIndex(i, getIndex(child, position - leafCount));
                    }
                    leafCount += child.getCount();
                }
            }
        } else if(node instanceof StripOfNode) {
            if(position < node.getCount()) {
                return new Index(null, position - leafCount);
            }
        }

        return null;
    }

    private Index composeIndex(int nodeIndex, Index index) {
        if(index != null) {
            int[] pNodeIndex = index.getNodeIndex();
            if(pNodeIndex != null) {
                int[] newNodeIndex = new int[pNodeIndex.length + 1];
                newNodeIndex[0] = nodeIndex;
                System.arraycopy(pNodeIndex, 0, newNodeIndex, 1, pNodeIndex.length);

                return new Index(newNodeIndex, index.getLeafIndex());
            } else {
                return new Index(new int[] {nodeIndex} , index.getLeafIndex());
            }
        }
        return new Index(new int[] {nodeIndex} , -1);
    }

    public class Index {
        private final int[] mNodeIndex;
        private final int mLeafIndex;

        Index(int[] nodeIndex, int leafIndex) {
            mNodeIndex = nodeIndex;
            mLeafIndex = leafIndex;
        }

        public int[] getNodeIndex() {
            return mNodeIndex;
        }

        public int getLeafIndex() {
            return mLeafIndex;
        }

        public int getLeafNodeIndex() {
            if(mNodeIndex != null && mNodeIndex.length > 0) {
                return mNodeIndex[mNodeIndex.length - 1];
            }
            return -1;
        }
    }
}
