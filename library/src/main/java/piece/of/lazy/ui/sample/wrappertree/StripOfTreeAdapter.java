package piece.of.lazy.ui.sample.wrappertree;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewParent;

import com.seilcre.lib.stripUI.IStripOfAdapter;
import com.seilcre.lib.stripUI.IStripOfHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zpdl
 */

public abstract class StripOfTreeAdapter extends IStripOfAdapter implements IStripOfNotify {

    @NonNull
    private StripOfRoot mRoot = new StripOfRoot();

    public StripOfTreeAdapter(@NonNull Context context) {
        super(context);
    }

    public abstract void onBindHolderInterface(List<IStripOfHolder> list);

    public IStripOfNode getNode(RecyclerView.ViewHolder holder) {
        if(holder == null) {
            return null;
        }

        NodePosition nodePosition = getNodePosition(holder.getAdapterPosition());
        return getNode(nodePosition);
    }

    @NonNull
    public IStripOfNode root() {
        return mRoot;
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
            NodePosition nodePosition = getNodePosition(position);
            IStripOfNode node = getNode(nodePosition);
            recyclerHolderInterface.onBindViewHolder(mContext, holder, node != null ? node.getModel() : null, nodePosition);
        }
    }

    @Override
    public Object getBindItem(int position) {
        NodePosition nodePosition = getNodePosition(position);
        IStripOfNode node = getNode(nodePosition);
        if(node != null) {
            return node.getModel();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mRoot.getViewCount();
    }

    @Override
    public int getItemViewType(int position) {
        NodePosition nodePosition = getNodePosition(position);
        IStripOfNode node = getNode(nodePosition);
        if(node != null && node.getModel() != null) {
            for (int i = 0; i < mRecyclerHolderInterfaceList.size(); i++) {
                IStripOfHolder holderInterface = mRecyclerHolderInterfaceList.valueAt(i);
                if (holderInterface != null && holderInterface.isBindItem(node.getModel())) {
                    return holderInterface.getViewType();
                }
            }
        }

        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        NodePosition nodePosition = getNodePosition(position);
        IStripOfNode node = getNode(nodePosition);
        if(node != null) {
            return node.getNodeId();
        }
        return super.getItemId(position);
    }

    @Override
    public int getViewPosition(IStripOfNode node) {
        IStripOfNode parent = node.getParent();
        int vPos = node.getViewPosition();
        while(parent != null) {
            vPos += parent.getViewPosition();
            parent = parent.getParent();
        }
        return vPos;
    }

    @Override
    public void beginTransition() {
        mRoot.beginTransition(this);
    }

    @Override
    public void applyTo() {
        mRoot.applyTo();
    }

    @Override
    public void notifyNodeSetChanged() {
        mRoot.traversals();
        notifyDataSetChanged();
    }

    @Override
    public void notifyNodeChanged(int vPos, int cnt) {
        notifyItemRangeChanged(vPos, cnt);
    }

    @Override
    public void notifyNodeInserted(int vPos, int cnt) {
        notifyItemRangeInserted(vPos, cnt);
    }

    @Override
    public void notifyNodeRemoved(int vPos, int cnt) {
        notifyItemRangeRemoved(vPos, cnt);
    }

    private IStripOfNode getNode(NodePosition nodePosition) {
        if(nodePosition == null) {
            return null;
        }

        int[] nPos = nodePosition.getNodePosition();
        if(nPos == null) {
            return null;
        }

        IStripOfNode result = mRoot;
        for (int tPos : nPos) {
            if(result == null) {
                return null;
            }
            result = result.getChildNode(tPos);
        }

        return result;
    }

    public void scrollBottomLastNodeFromParent(final RecyclerView.ViewHolder holder, final IStripOfNode parent) {
        if(holder == null || parent == null) {
            return;
        }
        boolean isLastNode = isLastNodeFromParent(holder, parent);
        if(isLastNode) {
            Handler uiHandler = new Handler(Looper.getMainLooper());
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    ViewParent viewParent = holder.itemView.getParent();
                    if (viewParent != null && viewParent instanceof RecyclerView) {
                        ((RecyclerView) viewParent).scrollToPosition(getViewPosition(parent) + parent.getViewCount());
                    }
                }
            };
            uiHandler.post(runnable);
        }
    }

    public boolean isLastNodeFromParent(RecyclerView.ViewHolder holder, IStripOfNode parent) {
        if(holder == null || parent == null) {
            return false;
        }

        NodePosition nodePosition = getNodePosition(holder);
        if(nodePosition == null) {
            return false;
        }

        if(parent.isView()) {
            if(nodePosition.getLeafPosition() == parent.getViewCount() - 2) {
                return true;
            }
        } else {
            if(nodePosition.getLeafPosition() == parent.getViewCount() - 1) {
                return true;
            }
        }
        return false;
    }

    public NodePosition getNodePosition(@NonNull RecyclerView.ViewHolder holder) {
        return getNodePosition(holder.getAdapterPosition());
    }

    public NodePosition getNodePosition(int vPos) {
        int low = 0;
        int high = mRoot.getChildNodeCount() - 1;
        int mid = (low + high) / 2;
        while (low <= high) {
            IStripOfNode childNode = mRoot.getChildNode(mid);
            if(childNode == null) {
                high = (low + high) / 2;
                mid = (low + high) / 2;
                continue;
            }

            int cPos = childNode.getViewPosition();
            int cCnt = childNode.getViewCount();
            if(cCnt <= 0) {
                mid++;
                continue;
            }

            if (cPos > vPos) {
                high = mid - 1;
            } else if (cPos + cCnt - 1 < vPos) {
                low = mid + 1;
            } else {
                break;
            }
            mid = (low + high) / 2;
        }

        IStripOfNode node = mRoot.getChildNode(mid);
        if(node != null) {
            return composeNodePosition(mid, getNodePosition(node, vPos - node.getViewPosition()));
        } else {
            return null;
        }
    }

    private NodePosition getNodePosition(IStripOfNode node, int vPos) {
        if(node == null || vPos < 0) {
            return null;
        }

        if(node.isView()) {
            if(vPos == 0) {
                return null;
            }
            vPos--;
        }

        int low = 0;
        int high = node.getChildNodeCount() - 1;
        int mid = (low + high) / 2;
        while(low <= high) {
            IStripOfNode childNode = node.getChildNode(mid);
            if(childNode == null) {
                high = (low + high) / 2;
                mid = (low + high) / 2;
                continue;
            }

            int cPos = childNode.getViewPosition();
            int cCnt = childNode.getViewCount();
            if(cCnt <= 0) {
                mid++;
                continue;
            }
            if (cPos > vPos) {
                high = (mid < high) ? mid - 1 : high - 1;
            } else if (cPos + cCnt - 1 < vPos) {
                low = mid + 1;
            } else {
                break;
            }
            mid = (low + high) / 2;
        }
        IStripOfNode childNode = node.getChildNode(mid);
        if(childNode != null) {
            return composeNodePosition(mid, getNodePosition(childNode, vPos - childNode.getViewPosition()));
        } else {
            return null;
        }
    }

    private NodePosition composeNodePosition(int position, NodePosition NodePosition) {
        if(NodePosition != null) {
            int[] pNodePosition = NodePosition.getNodePosition();
            if(pNodePosition != null) {
                int[] newNodePosition = new int[pNodePosition.length + 1];
                newNodePosition[0] = position;
                System.arraycopy(pNodePosition, 0, newNodePosition, 1, pNodePosition.length);

                return new NodePosition(newNodePosition);
            }
        }
        return new NodePosition(new int[]{position});
    }

    public class NodePosition {
        private final int[] mNodePosition;

        NodePosition(int[] nodePosition) {
            mNodePosition = nodePosition;
        }

        public int[] getNodePosition() {
            return mNodePosition;
        }

        public int getLeafPosition() {
            if(mNodePosition.length > 0) {
                return mNodePosition[mNodePosition.length - 1];
            }
            return -1;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if(mNodePosition != null) {
                for(int position : mNodePosition) {
                    sb.append(String.format("[%d]",position));
                }
            }
            return sb.toString();
        }
    }
}