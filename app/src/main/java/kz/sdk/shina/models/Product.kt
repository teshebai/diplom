package kz.sdk.shina.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    var id:String? = null,
    var title:String? = null,
    var img:String? = null,
    var price:Int?= null,
    var year:Int? = null,
    var type:String? = null,
    var transmission:String? =null,
    var volume:Int? = null,
    var millage:Int? =null,
    var color:String? =null,
    var isCredit:Boolean = false,
): Parcelable

