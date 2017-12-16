package piece.of.lazy.ui.tree

/**
 * Created by zpdl
 */
interface PieceOfNotify {
    fun notifyNodeSetChanged()
    fun notifyNodeChanged(position: Int, count: Int)
    fun notifyNodeInserted(position: Int, count: Int)
    fun notifyNodeRemoved(position: Int, count: Int)
}