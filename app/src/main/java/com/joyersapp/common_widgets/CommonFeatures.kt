package com.joyersapp.common_widgets

import android.content.Context
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.hbb20.CountryCodePicker
import com.joyersapp.theme.Gray40
import com.joyersapp.theme.LightBlack35
import com.joyersapp.utils.fontFamilyLato


/*@Composable
fun BasicTextField(
    value: String,
    onValueChange: () -> Unit = {},
    modifier: Modifier) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
//        placeholder = { Text(contactPlaceHolder, color = Gray40) },
//        modifier = Modifier.weight(0.97f),
        textStyle = TextStyle(
            platformStyle = PlatformTextStyle(includeFontPadding = false)
        ),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFF1F1F1),
            focusedContainerColor = Color(0xFFF1F1F1),
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true
    )
}*/

fun showCCPDialog(context: Context, showPhoneCode: Boolean, onCodeSelected: (String, String, ImageView,Int) -> Unit) {
    val ccp = CountryCodePicker(context)

    // Optional: configure default country if needed
    ccp.ccpDialogShowFlag = false
    ccp.ccpDialogShowNameCode = false
    ccp.ccpDialogShowTitle = true
    ccp.ccpDialogShowNameCode = false
    ccp.gravity = Gravity.CENTER
    ccp.setShowPhoneCode(false)
    ccp.setCcpDialogShowPhoneCode(false)
    ccp.setCcpDialogShowPhoneCode(showPhoneCode)

    ccp.showFullName(true)

    // Listener to capture the selected country code
    ccp.setOnCountryChangeListener {
        val code = ccp.selectedCountryCodeWithPlus
        val name = ccp.selectedCountryName
        val flag = ccp.imageViewFlag
        val flagId = ccp.selectedCountryFlagResourceId

        onCodeSelected(code, name, flag, flagId)
    }

    // Trigger the CCP dialog
    ccp.launchCountrySelectionDialog()
}


@Composable
fun CountryCodePicker(
    initialPadding: Float = 2f,
    defaultCountry: String = "US",
    onCountrySelected: (String) -> Unit = {},
    onCountryNameCodeSelected: ((String) -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(initialPadding.dp))
        AndroidView(
            modifier = Modifier.wrapContentHeight().padding(bottom = 1.dp),
            factory = { context ->
                CountryCodePicker(context).apply {
                    setDefaultCountryUsingNameCode(defaultCountry)
                    setCountryForNameCode(defaultCountry)
                    setAutoDetectedCountry(false)
                    showFullName(false)
                    showNameCode(false)
                    setShowPhoneCode(true)
                    showFlag(true)
                    setOnCountryChangeListener {
                        onCountrySelected(selectedCountryCodeWithPlus)
                        onCountryNameCodeSelected?.invoke(selectedCountryNameCode)
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
                .height(24.dp),
            color = LightBlack35,
            thickness = 1.dp
        )
    }
}


