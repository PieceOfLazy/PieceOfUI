package piece.of.lazy.ui.tree

/**
 * Created by zpdl
 */

open class DefaultPieceOfModel(isView: Boolean = true, isExpand: Boolean = true) : PieceOfModel {
    override var isView: Boolean = isView
        set(value) {
            if (field != value) {
                field = value
                listener?.onChangedView(this@DefaultPieceOfModel)
            }
        }

    override var isExpand: Boolean = isExpand
        set(value) {
            if (field != value) {
                field = value
                listener?.onChangedExpand(this@DefaultPieceOfModel)
            }
        }

    private var listener: PieceOfModel.OnModelInterface? = null

    override fun setOnOnModelInterface(l: PieceOfModel.OnModelInterface) {
        listener = l
    }

    override fun getNode(): PieceOfNode<*>? = listener?.onGetNodeInstance()
}