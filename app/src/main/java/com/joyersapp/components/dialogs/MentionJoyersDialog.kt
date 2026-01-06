package com.joyersapp.components.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joyersapp.R
import com.joyersapp.common_widgets.AppBasicTextField
import com.joyersapp.theme.Golden
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.Gray40
import com.joyersapp.theme.GrayLightBorder
import com.joyersapp.theme.LightBlack
import com.joyersapp.utils.fontFamilyLato

@Preview
@Composable
fun composePreview() {
    MentionJoyersDialog(onDismiss = {}) { }
}

@Composable
fun MentionJoyersDialog(
    onDismiss: () -> Unit,
    onApply: () -> Unit
) {
    BaseDialog(
        onDismiss = { onDismiss() },

        titles = arrayListOf("Profile Header")

    ) { dialogModifier, dialogFocusManager, maxHeight ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 35.dp)
                .background(Color.White)
                .verticalScroll(rememberScrollState()),
        ) {
            // ---------- HEADER SECTION ----------

            Text(
                text = "Mention Joyers",
                fontSize = 16.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamilyLato,
                color = LightBlack,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            EditableProfilePictureCard1(
                dialogModifier = dialogModifier,
            )
        }
    }
}

@Composable
fun EditableProfilePictureCard1(
    dialogModifier: Modifier,
) {
    // Card with profile and header images
    Card(
        modifier = Modifier
            .width(384.dp)
            .height(317.dp)
            .border(
                width = 1.dp, color = GrayLightBorder, shape = RoundedCornerShape(5.dp)
            ),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(containerColor = Gray20)
    ) {

        Spacer(modifier = dialogModifier.height(10.dp))
        SearchBarRowForEditMaganetic(
            searchQuery = "Search Joyer",
            showApplyButton = true,
            onApply = { },
            onSearchQueryChanged = { })

        Spacer(modifier = dialogModifier.height(10.dp))

        MentionJoyersScreen()

    }
}


/*
@Composable
fun SearchBarRow1(
    dialogModifier: Modifier = Modifier,
    searchQuery: String,
    showApplyButton: Boolean,
    onApply: () -> Unit,
    onSearchQueryChanged: (String) -> Unit
) {
    val goldenColor = Golden
    val lightBlackColor = LightBlack
    val hintColor = Gray40
    val whiteColor = Color.White
    // Search bar and buttons
    Row(
        modifier = dialogModifier
            .fillMaxWidth()
            .height(35.dp)
            .padding(horizontal = 15.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Search field with icons
        Box(
            modifier = dialogModifier
                .fillMaxSize()
                .height(35.dp)
                .clip(shape = RoundedCornerShape(35.dp))
                .background(
                    color = whiteColor, shape = RoundedCornerShape(35.dp)
                )
                .border(
                    1.dp, color = GrayLightBorder, shape = RoundedCornerShape(35.dp)
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // AppBasicTextField - it has internal padding (15.dp start, 2.dp end)
                // We account for this in our layout
                AppBasicTextField(
                    value = searchQuery,
                    onValueChange = { query ->
                        onSearchQueryChanged(query)
                    },
                    placeholder = "Search Joyer",
                    modifier = dialogModifier
                        .fillMaxHeight()
                        .padding(bottom = 1.dp),
                    textStyle = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        ),
                        fontFamily = fontFamilyLato,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    ),
                    containerColor = Color.White,
                    contentColor = lightBlackColor,
                    placeholderColor = hintColor,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    maxLength = 100
                )
            }
            Spacer(modifier = Modifier.width(50.dp))
            Row(
                modifier = Modifier
                    .width(70.dp)
                    .height(35.dp)
                    .clip(RoundedCornerShape(35.dp))
                    .clickable {
                        onApply()
                    }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_nav_joyers_add),
                    contentDescription = null,
                    tint = Color(0xFFD8A23A),
                    modifier = Modifier
                        .padding(end = 14.dp)
                        .size(20.dp)
                )
            }
        }
    }
}
*/

@Composable
fun SearchBarRowForEditMaganetic(
    dialogModifier: Modifier = Modifier,
    searchQuery: String,
    showApplyButton: Boolean,
    onApply: () -> Unit,
    onSearchQueryChanged: (String) -> Unit
) {
    val lightBlackColor = LightBlack
    val hintColor = Gray40
    val whiteColor = Color.White

    Row(
        modifier = dialogModifier
            .fillMaxWidth()
            .height(35.dp)
            .padding(start = 15.dp, end = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        /* ---------------- SEARCH BAR ---------------- */
        Box(
            modifier = dialogModifier
                .width(314.dp) // 🔥 KEY FIX
                .height(30.dp)
                .clip(RoundedCornerShape(35.dp))
                .background(whiteColor)
                .border(
                    width = 1.dp,
                    color = GrayLightBorder,
                    shape = RoundedCornerShape(35.dp)
                )
        ) {
            AppBasicTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                placeholder = "Search Joyer",
                modifier = dialogModifier
                    .fillMaxHeight()
                    .padding(horizontal = 15.dp),
                textStyle = TextStyle(
                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                    fontFamily = fontFamilyLato,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                ),
                containerColor = Color.White,
                contentColor = lightBlackColor,
                placeholderColor = hintColor,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                maxLength = 100
            )
        }

        Spacer(modifier = dialogModifier.width(10.dp))
        /* ---------------- PLUS BUTTON ---------------- */
        Box(
            modifier = dialogModifier
                .size(30.dp) // exact outer size
                .background(
                    color = Color(0xFFF6F6F6), // light grey fill
                    shape = CircleShape
                )
                .clickable {  },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_edit_magantic_add),
                contentDescription = "Add Joyer",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}


@Composable
fun MentionJoyersScreen() {
    Column(
        modifier = Modifier
            .width(354.dp)
            .padding(top = 15.dp, bottom = 15.dp, start = 15.dp, end = 15.dp)
            .background(
                color = Color.White, shape = RoundedCornerShape(1.dp)
            )
    ) {
        JoyersList(getPreviewJoyerList())
    }
}

@Composable
fun JoyersList(getPreviewJoyerList: List<JoyerUiModel>) {
    LazyColumn {
        items(getPreviewJoyerList) {
            MentionJoyerRow(it)
        }
    }
}

@Composable
fun MentionJoyerRow(joyer: JoyerUiModel) {
    Row(
        modifier = Modifier
            .width(354.dp)
            .height(67.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Radio
        RadioButton(
            selected = false,
            onClick = {  }
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Avatar
        Image(
            painter = joyer.avatar,
            contentDescription = null,
            modifier = Modifier
                .size(37.dp)
                .clip(CircleShape)
                .border(2.dp, Color.White, CircleShape)
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Texts
        Column(
            modifier = Modifier.weight(1f)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = joyer.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                if (joyer.starCount > 0) {
                    Spacer(modifier = Modifier.width(5.dp))
                    repeat(joyer.starCount) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "${joyer.role} • ${joyer.followStatus}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

data class JoyerUiModel(
    val name: String,
    val role: String,
    val followStatus: String,
    val starCount: Int,
    val avatar: Painter
)

@Composable
fun getPreviewJoyerList(): List<JoyerUiModel> {
    val placeholderAvatar = painterResource(id = R.drawable.avatar)

    return listOf(
        JoyerUiModel("Aarav Sharma", "Android Developer", "Following", 120, placeholderAvatar),
        JoyerUiModel("Priya Verma", "UI/UX Designer", "Follow", 85, placeholderAvatar),
        JoyerUiModel("Rohit Singh", "Backend Engineer", "Following", 200, placeholderAvatar),
        JoyerUiModel("Neha Gupta", "Product Manager", "Follow", 64, placeholderAvatar)
    )
}

