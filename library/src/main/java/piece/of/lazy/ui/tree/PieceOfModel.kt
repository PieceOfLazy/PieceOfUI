package piece.of.lazy.ui.tree

/**
 * Created by zpdl
 */

open interface PieceOfModel {

    interface OnModelInterface {
        fun onGetNodeInstance(): PieceOfNode<*>
        fun onChangedView(model: PieceOfModel)
        fun onChangedExpand(model: PieceOfModel)
    }

    var isView: Boolean
    var isExpand: Boolean

    fun setOnOnModelInterface(l: OnModelInterface)

    fun getNode(): PieceOfNode<*>?
}