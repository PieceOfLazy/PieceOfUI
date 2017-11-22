package piece.of.lazy.ui

import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by zpdl
 */

open abstract class PieceOfView<I> {
    protected var mView: View? = null
    protected var mItem: I? = null

    open fun doCreateView(context: Context?, parent: ViewGroup?): View? {
        val inflater: LayoutInflater? = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = onCreatePieceOfView(inflater, parent) ?: return null

        parent?.addView(view)
        doBindView(view)

        return view
    }

    open fun doBindView(parent: ViewGroup, @IdRes id: Int) {
        val view: View = parent.findViewById(id) ?: return
        doBindView(view)
    }

    open fun doBindView(view: View) {
        mView = view

        view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(p0: View?) {
                onDetached()
            }

            override fun onViewAttachedToWindow(p0: View?) {
                onAttached()
            }
        })

        onBindView(view)
    }

    open fun doBindItem(c: Context, item: I?) {
        mItem = item
        mView?.let {
            onBindItem(c, item)
        }
    }

    protected fun onCreatePieceOfView(inflater: LayoutInflater?, parent: ViewGroup?): View? =
            inflater?.inflate(onLayout(), parent)

    @LayoutRes
    protected abstract fun onLayout(): Int

    open fun onAttached() {
    }
    open fun onDetached() {
    }

    protected abstract fun onBindView(v: View)
    protected abstract fun onBindItem(c: Context, item: I?)
}
