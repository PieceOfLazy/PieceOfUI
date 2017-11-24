package piece.of.lazy.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by zpdl
 */

internal interface PieceOfAdapterItem {

    fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup?): RecyclerView.ViewHolder?

    fun onBindViewHolder(context: Context, viewHolder: RecyclerView.ViewHolder, item: Any, position: Int)

    fun isBindItem(item: Any?): Boolean

    fun getViewType(): Int
}
