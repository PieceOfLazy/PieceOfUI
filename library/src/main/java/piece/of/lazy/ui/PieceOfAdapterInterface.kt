package piece.of.lazy.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlin.reflect.KClass

/**
 * Created by zpdl
 */

abstract class PieceOfAdapterInterface<VH : RecyclerView.ViewHolder, VI : Any>(private val holderType: KClass<VH>, private val itemType: KClass<VI>): PieceOfView<VI>(), PieceOfAdapterItem {

    private var holder: VH? = null

    override fun onBindView(v: View) {
        holder = onCreateViewHolderPiece(v)
    }

    override fun onBindItem(c: Context, item: VI?) {
        if(item != null) {
            holder?.let { onBindViewHolderPiece(c, it, item, 0) }
        }
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup?): RecyclerView.ViewHolder {
        val view: View = onCreateView(inflater, parent)
        return onCreateViewHolderPiece(view)
    }

    override fun onBindViewHolder(context: Context, viewHolder: RecyclerView.ViewHolder, item: Any, position: Int) {
        val castHolder: VH? = castHolder(viewHolder)
        val castItem: VI? = castItem(item)

        if(castHolder != null && castItem != null) {
            onBindViewHolderPiece(context, castHolder, castItem, position)
        }
    }

    override fun isBindItem(item: Any?): Boolean = itemType.isInstance(item)

    override fun getViewType(): Int = onLayout()

    private fun castHolder(viewHolder: RecyclerView.ViewHolder): VH? {
        if(holderType.isInstance(viewHolder)) {
            @Suppress("UNCHECKED_CAST")
            return viewHolder as VH
        }
        return null
    }

    private fun castItem(item: Any): VI? {
        if(itemType.isInstance(item)) {
            @Suppress("UNCHECKED_CAST")
            return item as VI
        }
        return null
    }

    private fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?):
            View = inflater.inflate(onLayout(), parent, false)

    abstract protected fun onCreateViewHolderPiece(view: View): VH

    abstract protected fun onBindViewHolderPiece(context: Context, holder: VH, item: VI, position: Int)

}
