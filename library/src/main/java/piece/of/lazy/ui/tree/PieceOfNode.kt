package piece.of.lazy.ui.tree

/**
 * Created by zpdl
 */

interface PieceOfNode<MODEL> {

    var nodeId: Int

    var isView: Boolean
    var isExpand: Boolean

    var viewCount: Int
    var viewPosition: Int

    val model: MODEL

    fun root(): PieceOfRoot
    fun parent(): PieceOfNode<*>

    fun getChildNodeCount(): Int
    fun getChildNode(nPos: Int): PieceOfNode<*>

    fun changedNode()

    fun addNodes(vararg nodes:PieceOfNode<*>)
    fun addNodes(nPos: Int, vararg nodes:PieceOfNode<*>)

    fun removeNodes(nPos: Int, nCnt: Int)
    fun removeNode(nodes:PieceOfNode<*>)
}