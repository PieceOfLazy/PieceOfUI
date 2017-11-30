package piece.of.lazy.demo.piece

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.piece_group.view.*
import piece.of.lazy.demo.R
import piece.of.lazy.ui.PieceOfHolder

/**
 * Created by zpdl
 */
open class GroupPiece : PieceOfHolder<GroupPiece.Holder, GroupPiece.Item>(Holder::class, Item::class) {
    interface OnPieceListener {
        fun onClick(holder: Holder, item: Item)
        fun onExpand(holder: Holder, item: Item)
    }
    private var listener: OnPieceListener? = null

    fun setOnListener(l: OnPieceListener?) {
        listener = l
    }

    override fun onLayout(): Int = R.layout.piece_group

    override fun onCreateViewHolderPiece(view: View): Holder = Holder(view)

    override fun onBindViewHolderPiece(context: Context, holder: Holder, item: Item, position: Int) {
        val countStr = "${position + 1} Count"

        holder.subject.text = item.subject
        holder.count.text = countStr
        if(item.expand) {
            holder.dropdown.text = "-"
        } else {
            holder.dropdown.text = "+"
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var subject: TextView
        lateinit var count: TextView
        lateinit var dropdown: TextView

        init {
            with(itemView) {
                subject = piece_group_subject
                count = piece_group_count
                dropdown = piece_group_dropdown
            }

            itemView.setOnClickListener {
                getBindItem(this@Holder)?.let {
                    listener?.onClick(this@Holder, it)
                }
            }

            dropdown.setOnClickListener {
                getBindItem(this@Holder)?.let {
                    if(it.expand) {
                        it.expand = false
                        holder?.dropdown?.text = "+"
                        listener?.onExpand(this@Holder, it)
                    } else {
                        it.expand = true
                        holder?.dropdown?.text = "-"
                        listener?.onExpand(this@Holder, it)

                    }
                }
            }
        }
    }

    open class Item(val subject: String, var expand: Boolean)
}