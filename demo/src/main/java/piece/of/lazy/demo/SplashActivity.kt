package piece.of.lazy.demo

import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import piece.of.lazy.ui.PieceOfView

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        val animationSplashView: View = findViewById(R.id.splash_activity_piece_animation)
        val animationSplash = AnimationSplash()
        animationSplash.doBindView(animationSplashView)
        animationSplash.doBindItem(this, Unit)
    }

    private class AnimationSplash : PieceOfView<Unit>() {
        private lateinit var image: ImageView

        override fun onLayout(): Int = R.layout.splash_piece_animation

        override fun onBindView(v: View) {
            image = v.findViewById(R.id.splash_piece_animation_iv)
        }

        override fun onBindItem(c: Context, item: Unit?) {
        }
    }
}