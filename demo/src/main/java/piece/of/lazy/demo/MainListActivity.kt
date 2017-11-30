package piece.of.lazy.demo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.main_list_activity.*
import kotlinx.android.synthetic.main.main_list_piece_menu.view.*
import piece.of.lazy.ui.PieceOfAdapter
import piece.of.lazy.ui.PieceOfHolder
import piece.of.lazy.ui.util.Log

class MainListActivity : AppCompatActivity() {
    private val log = Log("MainList")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_list_activity)

        main_list_activity_recycler.apply {
            val lm = LinearLayoutManager(this@MainListActivity).apply {
                orientation = LinearLayoutManager.VERTICAL
            }

            setHasFixedSize(true)
            layoutManager = lm
            if(itemAnimator is SimpleItemAnimator) {
                (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            }

            adapter = Adapter(this@MainListActivity)
        }
    }

    inner class Adapter(context: Context) : PieceOfAdapter(context) {
        private val items: MutableList<MenuItem> = mutableListOf()

        init {
            items.add(MenuItem(1, "TEST : Piece Of View"))
            items.add(MenuItem(2, "TEST : Piece Of Adapter"))

        }

        override fun onBindAdapterInterface(list: MutableList<PieceOfHolder<*, *>>) {
            list.add(MenuHolder())
        }

        override fun getBindItem(position: Int): Any? = items[position]

        override fun getItemCount(): Int = items.size
    }

    inner private class MenuHolder : PieceOfHolder<MenuHolder.Holder, MenuItem>(Holder::class, MenuItem::class) {
        override fun onLayout(): Int = R.layout.main_list_piece_menu

        override fun onCreateViewHolderPiece(view: View): Holder = Holder(view)

        override fun onBindViewHolderPiece(context: Context, holder: Holder, item: MenuItem, position: Int) {
            val countStr = "${position+1} MENU ITEM"

            holder.subject.text = item.subject
            holder.count.text = countStr
        }

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            lateinit var subject: TextView
            lateinit var count: TextView
            lateinit var delete: TextView

            init {
                with(itemView) {
                    subject = main_list_piece_menu_main_tv
                    count = main_list_piece_menu_sub_tv
                }

                itemView.setOnClickListener {
                    val item = getBindItem(this@Holder)
                    item?.let {
                        when(it.mode) {
                            1 -> {
                                val intent = Intent(this@MainListActivity, POVActivity::class.java)
                                startActivity(intent)
                            }
                            2 -> log.i("Mode 2")
                        }
                    }
                }
            }
        }
    }

    inner private class MenuItem(val mode: Int, val subject: String)

}
