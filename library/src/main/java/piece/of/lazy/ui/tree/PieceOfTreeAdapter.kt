package piece.of.lazy.ui.tree

import android.content.Context
import piece.of.lazy.ui.PieceOfAdapter

/**
 * Created by zpdl
 */

abstract class PieceOfTreeAdapter(context: Context) : PieceOfAdapter(context), PieceOfNotify {

    private val root by lazy {
        PieceOfRoot(this)
    }

    override fun getBindItem(position: Int): Any? {
        return root.getPieceOfPosition(position)?.let {
            root.getNode(it)?.model
        }
    }

    override fun getItemCount(): Int = root.getViewCount()

    override fun getItemId(position: Int): Long {
        val itemId = root.getPieceOfPosition(position)?.let {
            root.getNode(it)?.getNodeId()
        }
        return itemId ?: super.getItemId(position)
    }

    override fun notifyNodeSetChanged() {
        notifyDataSetChanged()
    }

    override fun notifyNodeChanged(position: Int, count: Int) {
        notifyItemRangeChanged(position, count)
    }

    override fun notifyNodeInserted(position: Int, count: Int) {
        notifyItemRangeInserted(position, count)
    }

    override fun notifyNodeRemoved(position: Int, count: Int) {
        notifyItemRangeRemoved(position, count)
    }
}