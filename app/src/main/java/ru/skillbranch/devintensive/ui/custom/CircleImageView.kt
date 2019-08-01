package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.graphics.drawable.BitmapDrawable
import ru.skillbranch.devintensive.extensions.dpToPx
import android.graphics.*
import android.view.View
import android.graphics.Shader
import android.graphics.BitmapShader
import android.graphics.Bitmap
import androidx.core.graphics.drawable.DrawableCompat
import android.os.Build
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.RectF
import ru.skillbranch.devintensive.R
import android.graphics.PixelFormat
import android.graphics.ColorFilter
import android.graphics.drawable.GradientDrawable.RECTANGLE
import android.R.attr.shape
import android.text.TextPaint
import android.text.TextUtils
import ru.skillbranch.devintensive.utils.Utils


/*
xml атрибуты
cv_borderColor (цвет границы (format="color") по умолчанию white)
cv_borderWidth (ширина границы (format="dimension") по умолчанию 2dp).

CircleImageView должна превращать установленное изображение в круглое изображение с цветной рамкой,
у CircleImageView должны быть реализованы методы

getBorderWidth():Int,
setBorderWidth(dp:Int),
getBorderColor():Int,
setBorderColor(hex:String),
setBorderColor(@ColorRes colorId: Int).

Используй CircleImageView как ImageView для аватара пользователя (@id/iv_avatar)

 */

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_BORDER_WIDTH_DP = 2f
        private const val DEFAULT_BORDER_COLOR = "#FFFFFF"
    }

    private var borderWidth = DEFAULT_BORDER_WIDTH_DP
    private var borderColor = Color.parseColor(DEFAULT_BORDER_COLOR)

    private var viewWidth: Int = 0
    private var viewHeight: Int = 0

    private var bgBitmap: Bitmap? = null
    private lateinit var paintBgBitmap: Paint
    private val drawableRect = RectF()

    private var srcBitmap: Bitmap? = null
    private lateinit var paintSrcBitmap: Paint

    private lateinit var paintBorder: Paint
    private val borderRect = RectF()

    private var drawableRadius: Float = 0.toFloat()
    private var borderRadius: Float = 0.toFloat()

    private var shaderBg: BitmapShader? = null
    private var shaderSrc: BitmapShader? = null

    /*
    * Contains initials of the member
    * */
    var imageText: String? = null

    /*
    * Used to set size and color of the member initials
    * */
    var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var textPaint: TextPaint? = TextPaint(Paint.ANTI_ALIAS_FLAG)

     var cornerRadius: Int

    /*
    * Bounds of the canvas in float
    * Used to set bounds of member initial and background
    * */
    var rectF: RectF? = null

    private var showText = false;

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderWidth = a.getDimension(
                R.styleable.CircleImageView_cv_borderWidth,
                DEFAULT_BORDER_WIDTH_DP
            )
            borderColor = a.getColor(
                R.styleable.CircleImageView_cv_borderColor,
                Color.parseColor(DEFAULT_BORDER_COLOR))
            a.recycle()

        }

//        drawable?.let { bgBitmap = getBitmap(drawable) }

        paintBgBitmap = Paint()
        paintBgBitmap.setAntiAlias(true)

//        foreground?.let { srcBitmap = getBitmap(foreground) }

        paintSrcBitmap = Paint()
        paintSrcBitmap.setAntiAlias(true)

        paintBorder = Paint()
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setAntiAlias(true);
        paintBorder.setColor(borderColor)
        paintBorder.setAntiAlias(true)
        paintBorder.setStrokeWidth(context.dpToPx(borderWidth));


        cornerRadius = context.dpToPx(2f).toInt();

        paint.color = Color.MAGENTA
        textPaint?.setColor(Color.WHITE);

        textPaint?.setTextSize(16f * getResources().getDisplayMetrics().scaledDensity);
        textPaint?.setColor(Color.WHITE);

        initRect()
        invalidate()
    }

    private fun getBitmap(_drawable: Drawable, width: Int, height: Int): Bitmap? {
        if (_drawable is BitmapDrawable) {
            return _drawable.bitmap
        }
        try {
            var vdrawable = _drawable
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                vdrawable = DrawableCompat.wrap(_drawable!!).mutate()
            }
            var bitmap: Bitmap? = null
            if (_drawable is ColorDrawable)
                bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
            else
                bitmap = Bitmap.createBitmap(
                    width,
                    height, Bitmap.Config.ARGB_8888
                )

            val canvas = Canvas(bitmap)
            vdrawable.setBounds(0, 0, canvas.width, canvas.height)
            vdrawable.draw(canvas)

            return bitmap

        } catch (e: Exception) {
            e.printStackTrace();
        }
        return null
    }

    fun setText(text: String?) {
        imageText = text
        if (!TextUtils.isEmpty(imageText))
            showText = true
        else
            showText = false
        this.invalidate()
    }

    fun getBorderWidth(): Int {
        return borderWidth.toInt()
    }

    fun setBorderWidth(dp: Int) {
        this.borderWidth = dp*1f
        this.invalidate()
    }

    fun getBorderColor():Int {
        return borderColor
    }

    fun setBorderColor(hex: String) {
        setBorderColorInt(Color.parseColor(hex))
    }

    fun setBorderColor(/*@ColorRes*/ borderColorRes: Int) {
        setBorderColorInt(context.resources.getColor(borderColorRes,context.theme))
    }

    fun setBorderColorInt(borderColor: Int) {
        this.borderColor = borderColor
        if (paintBorder != null)
            paintBorder.setColor(borderColor)

        this.invalidate()
    }

    override fun onDraw(canvas: Canvas) {

        val circleCenter = viewWidth / 2
        // init shaderBg

        initRect()

        if (showText) {
            val centerX = Math.round(canvas.width * 0.5f)*1f
            val centerY = Math.round(canvas.height * 0.5f)*1f
            if (imageText != null) {
                val textWidth = textPaint?.measureText(imageText)!! * 0.5f
                val textBaseLineHeight = textPaint?.getFontMetrics()?.ascent!! * -0.4f


            canvas.drawCircle(drawableRect.centerX(), drawableRect.centerY(),
                    drawableRadius,
                    paint)

            canvas.drawText(imageText, centerX - textWidth, centerY + textBaseLineHeight, textPaint)
            }
        }else {

        if (bgBitmap == null && drawable != null) {
            bgBitmap = getBitmap(drawable, canvas.width, canvas.height)
        }

        if (bgBitmap != null) {
            if (shaderBg == null) {
                shaderBg = BitmapShader(
                    Bitmap.createScaledBitmap(bgBitmap, canvas.width, canvas.height, false),
                    Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP
                )
            }
            paintBgBitmap.setShader(shaderBg)


            // circleCenter is the x or y of the view's center
            // radius is the radius in pixels of the cirle to be drawn
            // paint contains the shaderBg that will texture the shape

//            canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter.toFloat(), paintBgBitmap)

            canvas.drawCircle(drawableRect.centerX(), drawableRect.centerY(), drawableRadius, paintBgBitmap);
        }

        if (srcBitmap == null && foreground != null) {
            srcBitmap = getBitmap(foreground, canvas.width, canvas.height)
        }

        if (srcBitmap != null) {
            if (shaderSrc == null) {
                shaderSrc = BitmapShader(
                    Bitmap.createScaledBitmap(srcBitmap, canvas.width, canvas.height, false),
                    Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP
                )
            }
            paintSrcBitmap.setShader(shaderSrc)


            // circleCenter is the x or y of the view's center
            // radius is the radius in pixels of the cirle to be drawn
            // paint contains the shaderBg that will texture the shape

            canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter.toFloat(), paintSrcBitmap)
        }
        }

        if (borderWidth > 0) {
            canvas.drawCircle(borderRect.centerX(), borderRect.centerY(), borderRadius, paintBorder);

//            canvas.drawCircle(
//                circleCenter + borderWidth,
//                circleCenter + borderWidth,
//                circleCenter + borderWidth,
//                paintBorder
//            )
        }


    }

    private fun initRect() {
        borderRect.set(calculateBounds());
        borderRadius = Math.min((borderRect.height() - borderWidth) / 2.0f, (borderRect.width() - borderWidth) / 2.0f);

        drawableRect.set(borderRect);
//        if (!mBorderOverlay && borderWidth > 0) {
//            drawableRect.inset(borderWidth - 1.0f, borderWidth - 1.0f);
//        }
        drawableRadius = Math.min(drawableRect.height() / 2.0f, drawableRect.width() / 2.0f);
    }

    private fun calculateBounds(): RectF {
        val availableWidth = width - paddingLeft - paddingRight
        val availableHeight = height - paddingTop - paddingBottom

        val sideLength = Math.min(availableWidth, availableHeight)

        val left = paddingLeft + (availableWidth - sideLength) / 2f
        val top = paddingTop + (availableHeight - sideLength) / 2f

        return RectF(left, top, left + sideLength, top + sideLength)
    }


//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        val width = measureWidth(widthMeasureSpec)
//        val height = measureHeight(heightMeasureSpec, widthMeasureSpec)
//
//        viewWidth = width - borderWidth.toInt() * 2
//        viewHeight = height - borderWidth.toInt() * 2
//
//        setMeasuredDimension(width, height)
//    }
//
//    private fun measureWidth(measureSpec: Int): Int {
//        var result = 0
//        val specMode = View.MeasureSpec.getMode(measureSpec)
//        val specSize = View.MeasureSpec.getSize(measureSpec)
//
//        if (specMode == View.MeasureSpec.EXACTLY) {
//            // We were told how big to be
//            result = specSize
//        } else {
//            // Measure the text
//            result = viewWidth
//
//        }
//
//        return result
//    }
//
//    private fun measureHeight(measureSpecHeight: Int, measureSpecWidth: Int): Int {
//        var result = 0
//        val specMode = View.MeasureSpec.getMode(measureSpecHeight)
//        val specSize = View.MeasureSpec.getSize(measureSpecHeight)
//
//        if (specMode == View.MeasureSpec.EXACTLY) {
//            // We were told how big to be
//            result = specSize
//        } else {
//            // Measure the text (beware: ascent is a negative number)
//            result = viewHeight
//        }
//        return result
//    }
}