package piece.of.lazy.demo

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.splash_activity.*
import piece.of.lazy.ui.Log
import piece.of.lazy.ui.PieceOfView

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window?.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)

        setContentView(R.layout.splash_activity)

        Log.level = Log.LEVEL.VERBOSE
        Log.i("KKH", "SplashActivity")
        val animationSplash = AnimationSplash()
        animationSplash.doBindView(splash_activity_piece_animation)
        animationSplash.doBindItem(this, Unit)
    }

    private class AnimationSplash : PieceOfView<Unit>() {
        private lateinit var image: ImageView

        override fun onLayout(): Int = R.layout.splash_piece_animation

        override fun onBindView(v: View) {
            Log.i("KKH", "onBindView()")
//            image = v.findViewById(R.id.splash_piece_animation_iv)
        }

        override fun onBindItem(c: Context, item: Unit?) {
            Log.i("KKH", "onBindItem()")
        }

        override fun onAttached() {
            super.onAttached()
            Log.i("KKH", "onAttached()")
        }

        override fun onDetached() {
            super.onDetached()
            Log.i("KKH", "onDetached()")
        }
    }
}