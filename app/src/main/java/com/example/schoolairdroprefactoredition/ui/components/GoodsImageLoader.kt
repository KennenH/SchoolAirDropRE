package com.example.schoolairdroprefactoredition.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.databinding.ComponentGoodsImageLoaderBinding
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.ImageUtil
import com.example.schoolairdroprefactoredition.utils.MyUtil
import com.lxj.xpopup.XPopup
import kotlinx.android.synthetic.main.fragment_my.view.*


/**
 * @author kennen
 * @date 2021/4/9
 */
class GoodsImageLoader : FrameLayout {

    private val binding by lazy {
        ComponentGoodsImageLoaderBinding.bind(
                LayoutInflater.from(context).inflate(R.layout.component_goods_image_loader, this, true))
    }

    private val margin by lazy {
        SizeUtils.dp2px(5f)
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * 装载图片
     *
     * 宽度最大为屏幕宽度，长度以宽度变化同比例缩放
     */
    fun setData(images: List<String>) {
        val size = images.size
        val imageViewList = ArrayList<ImageView>(size)
        val imageSrcWithPrefixList = ArrayList<String>(size)
        for ((index, image) in images.withIndex()) {
            val readPath = ConstantUtil.QINIU_BASE_URL + image
            imageSrcWithPrefixList.add(readPath)
            val imageView = ImageView(context).also {
                it.adjustViewBounds = true
                it.setOnClickListener {
                    XPopup.Builder(context)
                            .asImageViewer(imageView, index, imageSrcWithPrefixList as List<String>,
                                    false, false, -1, -1, -1, true,
                                    context.resources.getColor(R.color.blackAlways, context.theme), { _, _ ->
                            }, MyUtil.ImageLoader()).show()
                }
            }

            imageViewList.add(imageView)

            // Glide加载图片之前一定要先添加imageView，否则Glide怎么知道往哪个view里加载
            binding.componentGoodsImageLoader.addView(imageView)
            Glide.with(context)
                    .asBitmap()
                    .load(readPath)
                    .encodeQuality(ConstantUtil.DEFAULT_COMPRESS)
                    .apply(RequestOptions().placeholder(R.drawable.logo_placeholder))
                    .listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Bitmap, model: Any, target: Target<Bitmap>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                            // 按照比例计算图片高度
                            imageView.layoutParams = LinearLayout.LayoutParams(
                                    binding.componentGoodsImageLoader.measuredWidth,
                                    ImageUtil.getResizedHeight(resource.width, resource.height, SizeUtils.dp2px(600f))).also {
                                // 不是最后一张图片则在图片尾巴处添加一个margin值以区分上下两张图片
                                if (index < size - 1) {
                                    it.bottomMargin = margin
                                }
                            }
                            // 设置完layoutParams之后重新布局imageView
                            imageView.requestLayout()

                            // *********************************************//
                            // ！！这里的return false是对的，不要手贱改为true！！
                            return false
                            // ！！这里的return false是对的，不要手贱改为true！！
                            // *********************************************//
                        }
                    }).into(imageView)
        }
    }
}