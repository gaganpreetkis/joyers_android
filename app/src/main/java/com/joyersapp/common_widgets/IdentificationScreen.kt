package com.joyersapp.common_widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joyersapp.R

@Composable
fun IdentificationScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF6F6F6))
            .padding(16.dp)
    ) {

        SectionTitle("Identification")

        Spacer(Modifier.height(12.dp))

        ProfileFieldCard {
            NameField()
        }

        ProfileFieldCard {
            BirthdayField()
        }

        ProfileFieldCard {
            GenderField()
        }

        ProfileFieldCard {
            NationalityField()
        }

        ProfileFieldCard {
            EthnicityField()
        }

        ProfileFieldCard {
            FaithField()
        }

        ProfileFieldCard {
            LanguageField()
        }

        ProfileFieldCard {
            EducationField()
        }

        ProfileFieldCard {
            RelationshipField()
        }

        Spacer(Modifier.height(50.dp))
    }
}


@Composable
fun ProfileFieldCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            content()
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth(),
        color = Color.Black
    )
}
@Composable
fun NameField() {
    Text("Name", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)

    Spacer(Modifier.height(6.dp))

    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = { Text("Sara Spegel James…") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}
@Composable
fun BirthdayField() {
    Text("Birthday", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)

    Spacer(Modifier.height(6.dp))

    OutlinedTextField(
        value = "30 Jan 2012",
        onValueChange = {},
        trailingIcon = {
//            Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color.Gray)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}
@Composable
fun GenderField() {
    Text("Gender", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
    Spacer(Modifier.height(6.dp))

    var selected by remember { mutableStateOf("Male") }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {

        listOf("Male", "Female", "Other Gender").forEach { gender ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selected == gender,
                    onClick = { selected = gender }
                )
                Text(gender, fontSize = 15.sp)
            }
        }
    }
}
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NationalityField() {

    Text("Nationality", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)

    Spacer(Modifier.height(8.dp))

    var expanded by remember { mutableStateOf(false) }

    val allCountries = listOf(
        "United States", "Kuwait", "India", "England", "Spain",
        "Kuwait", "India", "England", "United States"
    )

    val visibleItems = if (expanded) allCountries else allCountries.take(5)

    FlowRow(

    ) {
        visibleItems.forEach { item ->
            Box(
                modifier = Modifier
                    .background(Color(0xFFF1F1F1), RoundedCornerShape(50))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(item, fontSize = 14.sp)
            }
        }
    }

    if (!expanded) {
        Text(
            text = "See All",
            color = Color(0xFFD29E29),
            fontSize = 14.sp,
            modifier = Modifier
                .padding(top = 6.dp)
                .clickable { expanded = true }
        )
    }
}
@Composable
fun EthnicityField() {
    Text("Ethnicity", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
    Spacer(Modifier.height(6.dp))

    OutlinedTextField(
        value = "Spanish",
        onValueChange = {},
        readOnly = true,
        trailingIcon = {
//            Icon(Icons.Default.ArrowDropDown, null)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    )
}
@Composable
fun FaithField() {
    Text("Faith", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
    Spacer(Modifier.height(6.dp))

    OutlinedTextField(
        value = "Christianity (Catholicism)",
        onValueChange = {},
        readOnly = true,
//        trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    )
}
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LanguageField() {

    Text("Language", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)

    Spacer(Modifier.height(8.dp))

    var expanded by remember { mutableStateOf(false) }

    val languages = listOf(
        "Hindi (Excellent)",
        "Japanese (Good)",
        "English (Very Good)",
        "Arabic (Basic)",
        "Spanish (Excellent)",
        "Italian (Good)"
    )

    val visible = if (expanded) languages else languages.take(4)

    FlowRow(

    ) {
        visible.forEach {
            Text(it, fontSize = 15.sp)
            Text("•", fontSize = 20.sp, color = Color.Gray)
        }
    }

    if (!expanded) {
        Text(
            "See All",
            color = Color(0xFFD29E29),
            fontSize = 14.sp,
            modifier = Modifier
                .padding(top = 6.dp)
                .clickable { expanded = true }
        )
    }
}
@Composable
fun EducationField() {
    Text("Education", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
    Spacer(Modifier.height(6.dp))

    OutlinedTextField(
        value = "Master’s",
        onValueChange = {},
        readOnly = true,
//        trailingIcon = { Icon(R.drawable.ic_mail_golden) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    )
}
@Composable
fun RelationshipField() {
    Text("Relationship", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
    Spacer(Modifier.height(6.dp))

    OutlinedTextField(
        value = "Married",
        onValueChange = {},
        readOnly = true,
//        trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    )
}
