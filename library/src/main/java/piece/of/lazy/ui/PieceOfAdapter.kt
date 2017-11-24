package piece.of.lazy.ui

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import java.util.ArrayList

/**
 * Created by zpdl
 */

abstract class PieceOfAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val holderInterfaces: SparseArray<PieceOfAdapterInterface<*,*>> = SparseArray()

    init {
        this.setHasStableIds(true)
        this.onBindAdapterInterface(holderInterfaces)
    }

    protected abstract fun onBindAdapterInterface(list: List<PieceOfAdapterInterface<*,*>>)

    public abstract fun getBindItem(position: Int): Any?

    private fun onBindAdapterInterface(list: SparseArray<PieceOfAdapterInterface<*,*>>) {
        val bindHolder = ArrayList<PieceOfAdapterInterface<*,*>>()
        onBindAdapterInterface(bindHolder)

        for (holder in bindHolder) {
            list.put(holder.getViewType(), holder)
        }
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val holderInterface: PieceOfAdapterInterface<*,*>? = holderInterfaces.get(viewType)
        return if(holderInterface != null) {
            holderInterface.onCreateViewHolder(inflater, parent)
        } else {
            val tv = TextView(context)
            tv.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            tv.setTextColor(Color.WHITE)
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
            tv.setPadding(20, 20, 20, 20)
            tv.setBackgroundColor(Color.DKGRAY)
            object : RecyclerView.ViewHolder(tv) {
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        holder?.let {
            val item = getBindItem(position)
            val holderInterface: PieceOfAdapterInterface<*,*>? = holderInterfaces.get(it.itemViewType)

            when {
                holderInterface != null -> item?.let {
                    holderInterface.onBindViewHolder(context, holder, item, position)
                }
                holder.itemView is TextView -> {
                    (holder.itemView as TextView).text = String.format("ERROR : position : %d", position)
                }
                else -> {

                }
            }
        }
    }
}