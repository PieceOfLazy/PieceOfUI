package piece.of.lazy.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.pov_activity.*
import piece.of.lazy.demo.piece.AddPiece
import piece.of.lazy.demo.piece.DeletablePiece
import piece.of.lazy.demo.piece.GroupPiece
import piece.of.lazy.demo.piece.NormalPiece
import piece.of.lazy.ui.PieceOfHolder
import piece.of.lazy.ui.util.Log

class POVActivity : AppCompatActivity() {
    companion object {
        var deletableCount = 0
    }

    private val log = Log("POV")

    private lateinit var group1: MutableList<PieceOfHolder<*, *>>
    private lateinit var group2: MutableList<PieceOfHolder<*, *>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pov_activity)

        val normal1 = NormalPiece().apply {
            doBindView(pov_activity_piece_normal_1)
            doBindItem(this@POVActivity, NormalPiece.Item("NORMAL ONE"))
            setOnListener(object : NormalPiece.OnPieceListener {
                override fun onClick(holder: NormalPiece.Holder, item: NormalPiece.Item) {
                    Toast.makeText(this@POVActivity, item.subject, Toast.LENGTH_SHORT).show()
                }
            })
        }

        val normal2 = NormalPiece().apply {
            doBindView(pov_activity_piece_normal_2)
            doBindItem(this@POVActivity, NormalPiece.Item("NORMAL TWO"))
            setOnListener(object : NormalPiece.OnPieceListener {
                override fun onClick(holder: NormalPiece.Holder, item: NormalPiece.Item) {
                    Toast.makeText(this@POVActivity, item.subject, Toast.LENGTH_SHORT).show()
                }
            })
        }

        val normal3 = NormalPiece().apply {
            doBindView(pov_activity_piece_normal_3)
            doBindItem(this@POVActivity, NormalPiece.Item("NORMAL THREE"))
            setOnListener(object : NormalPiece.OnPieceListener {
                override fun onClick(holder: NormalPiece.Holder, item: NormalPiece.Item) {
                    Toast.makeText(this@POVActivity, item.subject, Toast.LENGTH_SHORT).show()
                }
            })
        }

        val normal4 = NormalPiece().apply {
            doBindView(pov_activity_piece_normal_4)
            doBindItem(this@POVActivity, NormalPiece.Item("NORMAL FORE"))
            setOnListener(object : NormalPiece.OnPieceListener {
                override fun onClick(holder: NormalPiece.Holder, item: NormalPiece.Item) {
                    Toast.makeText(this@POVActivity, item.subject, Toast.LENGTH_SHORT).show()
                }
            })
        }

        val normal5 = NormalPiece().apply {
            doBindView(pov_activity_piece_normal_5)
            doBindItem(this@POVActivity, NormalPiece.Item("NORMAL FIVE"))
            setOnListener(object : NormalPiece.OnPieceListener {
                override fun onClick(holder: NormalPiece.Holder, item: NormalPiece.Item) {
                    Toast.makeText(this@POVActivity, item.subject, Toast.LENGTH_SHORT).show()
                }
            })
        }

        val normal6 = NormalPiece().apply {
            doBindView(pov_activity_piece_normal_6)
            doBindItem(this@POVActivity, NormalPiece.Item("NORMAL SIX"))
            setOnListener(object : NormalPiece.OnPieceListener {
                override fun onClick(holder: NormalPiece.Holder, item: NormalPiece.Item) {
                    Toast.makeText(this@POVActivity, item.subject, Toast.LENGTH_SHORT).show()
                }
            })
        }

        val normal7 = NormalPiece().apply {
            doBindView(pov_activity_piece_normal_7)
            doBindItem(this@POVActivity, NormalPiece.Item("NORMAL SEVEN"))
            setOnListener(object : NormalPiece.OnPieceListener {
                override fun onClick(holder: NormalPiece.Holder, item: NormalPiece.Item) {
                    Toast.makeText(this@POVActivity, item.subject, Toast.LENGTH_SHORT).show()
                }
            })
        }

        val normal8 = NormalPiece().apply {
            doBindView(pov_activity_piece_normal_8)
            doBindItem(this@POVActivity, NormalPiece.Item("NORMAL EIGHT"))
            setOnListener(object : NormalPiece.OnPieceListener {
                override fun onClick(holder: NormalPiece.Holder, item: NormalPiece.Item) {
                    Toast.makeText(this@POVActivity, item.subject, Toast.LENGTH_SHORT).show()
                }
            })
        }

        GroupPiece().apply {
            doBindView(pov_activity_piece_group_1)
            doBindItem(this@POVActivity, GroupPiece.Item("GROUP ONE", true))
            setOnListener(object : GroupPiece.OnPieceListener {
                override fun onExpand(holder: GroupPiece.Holder, item: GroupPiece.Item) {
                    if(item.expand) {
                        for(piece: PieceOfHolder<*, *> in group1) {
                            piece.mView?.visibility = View.VISIBLE
                        }
                    } else {
                        for(piece: PieceOfHolder<*, *> in group1) {
                            piece.mView?.visibility = View.GONE
                        }
                    }
                }

                override fun onClick(holder: GroupPiece.Holder, item: GroupPiece.Item) {
                    Toast.makeText(this@POVActivity, item.subject, Toast.LENGTH_SHORT).show()
                }
            })
        }

        GroupPiece().apply {
            doBindView(pov_activity_piece_group_2)
            doBindItem(this@POVActivity, GroupPiece.Item("GROUP TWO", true))
            setOnListener(object : GroupPiece.OnPieceListener {
                override fun onExpand(holder: GroupPiece.Holder, item: GroupPiece.Item) {
                    if(item.expand) {
                        for(piece: PieceOfHolder<*, *> in group2) {
                            piece.mView?.visibility = View.VISIBLE
                        }
                    } else {
                        for(piece: PieceOfHolder<*, *> in group2) {
                            piece.mView?.visibility = View.GONE
                        }
                    }
                }

                override fun onClick(holder: GroupPiece.Holder, item: GroupPiece.Item) {
                    Toast.makeText(this@POVActivity, item.subject, Toast.LENGTH_SHORT).show()
                }
            })
        }

        group1 = mutableListOf(normal4, normal5, normal6)
        group2 = mutableListOf(normal7, normal8)

        AddPiece().apply {
            doBindView(pov_activity_add)
            doBindItem(this@POVActivity, AddPiece.Item("ADD"))
            setOnListener(object : AddPiece.OnPieceListener {
                override fun onClick(holder: AddPiece.Holder, item: AddPiece.Item) {
                    Toast.makeText(this@POVActivity, item.subject, Toast.LENGTH_SHORT).show()

                    val index = indexOfChild(holder.itemView)
                    DeletablePiece().apply {
                        POVActivity.deletableCount++
                        doCreateView(this@POVActivity, null)
                        pov_activity_container.addView(mView, index)
                        doBindItem(this@POVActivity, DeletablePiece.Item("DELETABLE COUNT ${POVActivity.deletableCount}"))
                        setOnListener(object : DeletablePiece.OnPieceListener{
                            override fun onDelete(holder: DeletablePiece.Holder, item: DeletablePiece.Item) {
                                Toast.makeText(this@POVActivity, item.subject, Toast.LENGTH_SHORT).show()
                                removeChild(holder.itemView)
                            }

                            override fun onClick(holder: DeletablePiece.Holder, item: DeletablePiece.Item) {
                                Toast.makeText(this@POVActivity, item.subject, Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            })
        }
    }

    private fun indexOfChild(view: View): Int {
        return pov_activity_container.indexOfChild(view)
    }

    private fun removeChild(view: View) {
        pov_activity_container.removeView(view)
    }
}
