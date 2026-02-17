package com.joyersapp.feature.post.presentation.create_post

import android.content.Context
import android.media.browse.MediaBrowser
import android.net.Uri
import android.widget.VideoView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import com.joyersapp.R
import com.joyersapp.components.dialogs.MentionJoyersDialog
import com.joyersapp.components.layouts.HardBlockingLoader
import com.joyersapp.feature.post.domain.model.Joyer
import com.joyersapp.feature.post.presentation.common.CameraFab
import com.joyersapp.feature.post.presentation.common.CreateJoyHeader
import com.joyersapp.feature.post.presentation.common.JoyTextField
import com.joyersapp.feature.post.presentation.common.MediaPickerDialog
import com.joyersapp.feature.post.presentation.create_joy.CreateJoyUiState
import com.joyersapp.feature.post.presentation.create_joy.CreateJoyViewModel
import com.joyersapp.feature.profile.data.remote.dto.EditMagneticsUserListData
import com.joyersapp.feature.profile.presentation.UserProfileEvent
import com.joyersapp.theme.AvatarBorder
import com.joyersapp.theme.Golden
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.GrayOuterBorder
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack55
import com.joyersapp.theme.LightBlack60
import com.joyersapp.theme.Red
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.graphemeCount
import com.joyersapp.utils.noRippleClickable
import kotlin.collections.isNotEmpty

@Preview
@Composable
private fun ScreenPreview() {
    CreatePostScafold(
        context = LocalContext.current,
        CreateJoyUiState(),
        CreatePostUiState(),
        toggleMediaPickerDialog = {},
        dismissMentionJoyersDialog = {},
        showMentionedJoyersDialog = {},
        applyMentionedJoyers = {},
        onBack = {},
        addMedia = {a,s -> },
        removeMedia = {a -> },
        onPreviewMedia = { },
    )
}

@Composable
fun CreatePostScreen(
    sharedViewmodel: CreateJoyViewModel,
    viewmodel: CreatePostViewModel,
    onBack: () -> Unit,
    onPreviewMedia: (Int) -> Unit,
) {

    val sharedState by sharedViewmodel.uiState.collectAsStateWithLifecycle()
    val state by viewmodel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current



    HardBlockingLoader(state.isLoading)

    CreatePostScafold(
        context = context,
        sharedState = sharedState,
        state = state,
        onBack = onBack,
        onPreviewMedia = onPreviewMedia,
        toggleMediaPickerDialog = {viewmodel.onEvent(CreatePostEvent.ToggleMediaPickerDialog(it))},
        dismissMentionJoyersDialog = {viewmodel.onEvent(CreatePostEvent.ToggleMentionJoyersDialog(false))},
        showMentionedJoyersDialog = {viewmodel.onEvent(CreatePostEvent.ToggleMentionJoyersDialog(true))},
        applyMentionedJoyers = {viewmodel.onEvent(CreatePostEvent.ApplyMentionedJoyers(it))},
        addMedia = {media, context -> viewmodel.addMedia(media, context)},
        removeMedia = {mediaItem -> viewmodel.removeMedia(mediaItem)},
    )

//    }
}

@Composable
private fun CreatePostScafold(
    context: Context,
    sharedState: CreateJoyUiState,
    state: CreatePostUiState,
    onBack: () -> Unit,
    toggleMediaPickerDialog: (Boolean) -> Unit,
    dismissMentionJoyersDialog: () -> Unit,
    showMentionedJoyersDialog: () -> Unit,
    addMedia: (List<Uri>, Context) -> Unit,
    removeMedia: (MediaItem) -> Unit,
    applyMentionedJoyers: (List<EditMagneticsUserListData>) -> Unit,
    onPreviewMedia: (Int) -> Unit,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CreateJoyHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(63.dp)
                    .background(White),
                title = "Create Post",
                onBack = onBack,
                onMenu = {}
            )

            HorizontalDivider(
                thickness = 0.5.dp,
                color = GrayOuterBorder
            )

            Row() {
                JoyerRow(
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .weight(1f)
                        .height(37.dp)
                        .padding(start = 15.dp),
                    joyer = state.joyer
                )

                Text(
                    modifier = Modifier
                        .padding(top = 26.dp, start = 15.dp, end = 18.dp),
                    text = state.remainingCharacters.toString(),
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    color = if (state.remainingCharacters < 0) Red else Golden,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamilyLato,
                    maxLines = 1
                )
            }

            JoyTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 11.dp),
                textState = state.textState,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = fontFamilyLato,
                    fontWeight = FontWeight.Normal,
                    color = LightBlack
                ),
                placeholder = "Let’s Joy… Sara",
                placeHolderTextStyle = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = fontFamilyLato,
                    fontWeight = FontWeight.Normal,
                    color = LightBlack60
                ),
                highlightWords = true,
                singleLine = false,
                maxLength = 425,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Sentences,
                    showKeyboardOnFocus = false
                ),
                keyEvent = {
                    if (it == "@") {
                        showMentionedJoyersDialog()
                    }

                }
            )

            MediaGridScreen(
                navMediaPreview = onPreviewMedia,
                mediaList = state.mediaList,
                removeMedia = removeMedia
            )

        }

        CameraFab(
            Modifier
                .padding(bottom = 45.dp, end = 15.dp)
                .align(Alignment.BottomEnd)
                .imePadding()
                .clip(CircleShape)
                .clickable{
                    toggleMediaPickerDialog(true)
                }
        )

        MediaPickerDialog(
            show = state.showMediaPickerDialog,
            onDismiss = { toggleMediaPickerDialog(false) },
            onMediaPicked = { media -> addMedia(media, context) }
        )

        if (state.showMentionJoyersDialog) {
            MentionJoyersDialog(
                initList = sharedState.editMagneticsUserList,
                onDismiss = { dismissMentionJoyersDialog() },
                onApply = { applyMentionedJoyers(it) },
            )
        }

    }
}

@Composable
fun JoyerRow(
    modifier: Modifier,
    joyer: Joyer
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Avatar
        Box(
            modifier = Modifier
                .size(37.dp)
                .border(width = 1.5.dp, color = AvatarBorder, shape = CircleShape)
                .padding(1.5.dp)
                .border(width = 2.5.dp, color = White, shape = CircleShape)
                .clip(CircleShape)
                .background(Gray20),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.avatar),
                contentDescription = "avatar", modifier = Modifier.size(33.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Texts
        Column(
            modifier = Modifier.weight(1f)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = joyer.name,
                    fontSize = 16.sp,
                    lineHeight = 19.sp,
                    color = LightBlack,
                    fontFamily = fontFamilyLato,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(Modifier.height(3.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = joyer.tag,
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    color = Golden,
                    fontFamily = fontFamilyLato,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}


@Composable
fun MediaGridScreen(
    navMediaPreview: (Int) -> Unit,
    mediaList: List<MediaItem>,
    removeMedia: (MediaItem) -> Unit,
) {

    Column {
        if (mediaList.isNotEmpty()) {
            MediaDynamicGrid(
                mediaList = mediaList,
                onPreview = navMediaPreview,
                removeMedia = removeMedia
            )
        }

    }
}

@Composable
fun MediaDynamicGrid(
    mediaList: List<MediaItem>,
    onPreview: (Int) -> Unit,
    removeMedia: (MediaItem) -> Unit,
) {
    val count = mediaList.size

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .background(White)
    ) {

        when (count) {

            1 -> {
                MediaItemView(
                    Modifier.fillMaxSize(),
                    mediaList[0],
                    onPreview = { item ->
                        onPreview(mediaList.indexOf(item))
                                },
                    removeMedia
                )
            }

            2 -> {
                Row(
                    Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(1.dp)

                ) {
                    mediaList.forEach {
                        MediaItemView(
                            Modifier.weight(1f).fillMaxHeight(),
                            it,
                            onPreview = { item ->
                                onPreview(mediaList.indexOf(item))
                            },
                            removeMedia
                        )

                    }
                }
            }

            3 -> {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {

                    Row(
                        Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        mediaList.take(2).forEach {
                            MediaItemView(
                                Modifier.weight(1f).fillMaxHeight(),
                                it,
                                onPreview = { item ->
                                    onPreview(mediaList.indexOf(item))
                                },
                                removeMedia
                            )
                        }
                    }

                    MediaItemView(
                        Modifier.weight(1f).fillMaxWidth(),
                        mediaList[2],
                        onPreview = { item ->
                            onPreview(mediaList.indexOf(item))
                        },
                        removeMedia
                    )
                }
            }

            4 -> {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    mediaList.chunked(2).forEach { rowItems ->
                        Row(
                            Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(1.dp)
                        ) {
                            rowItems.forEach {
                                MediaItemView(
                                    Modifier.weight(1f).fillMaxHeight(),
                                    it,
                                    onPreview = { item ->
                                        onPreview(mediaList.indexOf(item))
                                    },
                                    removeMedia
                                )
                            }
                        }
                    }
                }
            }

            5 -> {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {

                    Row(
                        Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        mediaList.take(2).forEach {
                            MediaItemView(
                                Modifier.weight(1f).fillMaxHeight(),
                                it,
                                onPreview = { item ->
                                    onPreview(mediaList.indexOf(item))
                                },
                                removeMedia
                            )
                        }
                    }

                    Row(
                        Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        mediaList.takeLast(3).forEach {
                            MediaItemView(
                                Modifier.weight(1f).fillMaxHeight(),
                                it,
                                onPreview = { item ->
                                    onPreview(mediaList.indexOf(item))
                                },
                                removeMedia
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MediaItemView(
    modifier: Modifier,
    item: MediaItem,
    onPreview: (MediaItem) -> Unit,
    removeMedia: (MediaItem) -> Unit,
) {
    Box(
        modifier = modifier
            .clickable { onPreview(item) }
    ) {
        when (item.type) {
            MediaType.IMAGE -> {
                AsyncImage(
                    model = item.uri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            MediaType.VIDEO -> {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.uri) // Works for both image and video Uris
                        .decoderFactory { result, options, _ ->
                            VideoFrameDecoder(result.source, options)
                        }
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Image(
                    painterResource(R.drawable.ic_video_play_golden),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(40.dp)
                )
            }
        }
        Image(
            painter = painterResource(R.drawable.ic_cross_light_black90),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 10.dp)
                .padding(end = 10.dp)
                .size(21.dp)
                .noRippleClickable{
                    removeMedia(item)
                }
        )


    }
}

@Composable
fun MediaPreviewScreen(
    mediaList: List<MediaItem>,
    initialPage: Int = 0,
    onBack: () -> Unit
) {

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { mediaList.size }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->

            val item = mediaList[page]

            when (item.type) {
                MediaType.IMAGE -> {
                    ImagePreview(item)
                }

                MediaType.VIDEO -> {
                    VideoPreview(item)
                }
            }
        }

        // Optional back button
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_back_arrow_golden),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
private fun ImagePreview(item: MediaItem) {
    AsyncImage(
        model = item.uri,
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    )
}

@Composable
private fun VideoPreview(item: MediaItem) {

    val context = LocalContext.current

    val exoPlayer = remember(item.uri) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(androidx.media3.common.MediaItem.fromUri(item.uri))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
                useController = true
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}