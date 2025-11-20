package com.synapse.joyers.common_widgets

import android.widget.LinearLayout
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CountryCodePicker(
    defaultCountry: String = "US",
    onCountrySelected: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        AndroidView(
            modifier = Modifier.wrapContentHeight(),
            factory = { context ->
                com.hbb20.CountryCodePicker(context).apply {
                    setDefaultCountryUsingNameCode(defaultCountry)
                    setCountryForNameCode(defaultCountry)
                    setAutoDetectedCountry(false)
                    showFullName(false)
                    showNameCode(false)
                    setShowPhoneCode(true)
                    showFlag(true)
                    setOnCountryChangeListener {
                        onCountrySelected(selectedCountryCodeWithPlus)
                    }
                }
            },
            update = { ccp ->
                ccp.setAutoDetectedCountry(false)
                ccp.setCountryForNameCode(defaultCountry)
                //val clickConsumer = ccp.findViewById<View>(com.hbb20.R.id.rlClickConsumer)
                //clickConsumer?.setPadding(ccp.paddingLeft, ccp.paddingTop, 0, ccp.paddingBottom)
                val flagView = ccp.imageViewFlag
                val sizePx = (20 * ccp.resources.displayMetrics.density).toInt()
                flagView.layoutParams = LinearLayout.LayoutParams(sizePx, sizePx)
                flagView.requestLayout()
            }
        )
        VerticalDivider(
            modifier = Modifier
                .height(24.dp)
                .padding(start = 8.dp, end = 8.dp),
            color = Color(0xFFBDBDBD),
            thickness = 1.dp
        )
    }
}