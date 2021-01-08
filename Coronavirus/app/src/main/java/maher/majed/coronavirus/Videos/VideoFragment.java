package maher.majed.coronavirus.Videos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import maher.majed.coronavirus.MainActivity;
import maher.majed.coronavirus.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends Fragment {

    PlayerView playerView;
    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    List<String> videoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View view = inflater.inflate(R.layout.fragment_video, container, false);

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                100);

        playerView =view.findViewById(R.id.player_view_0);

        //getSupportActionBar().setTitle("Videos about Coronavirus");

        return view;

    }


    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(getContext());
        playerView.setPlayer(player);

        videoList = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("videos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    videoList.clear();
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        videoList.add(documentSnapshot.getString("uriVideo").toString());
                    }
                    Uri uri = Uri.parse(videoList.get(0));
                    Uri uri1 = Uri.parse(videoList.get(1));
                    Uri uri2 = Uri.parse(videoList.get(2));
                    Uri uri3 = Uri.parse(videoList.get(3));

                    MediaSource mediaSource = buildMediaSource(uri, uri1, uri2, uri3);
                    player.setPlayWhenReady(playWhenReady);
                    player.seekTo(currentWindow, playbackPosition);
                    player.prepare(mediaSource, false, false);
                }
            }
        });


    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri, Uri uri1, Uri uri2, Uri uri3) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(getContext(), "exoplayer-codelab");
        ProgressiveMediaSource.Factory mediaSourceFactory =
                new ProgressiveMediaSource.Factory(dataSourceFactory);

        // Create a media source using the supplied URI
        MediaSource mediaSource1 = mediaSourceFactory.createMediaSource(uri);
        MediaSource mediaSource2 = mediaSourceFactory.createMediaSource(uri1);
        MediaSource mediaSource3 = mediaSourceFactory.createMediaSource(uri2);
        MediaSource mediaSource4 = mediaSourceFactory.createMediaSource(uri3);

        return new ConcatenatingMediaSource(mediaSource1, mediaSource2, mediaSource3, mediaSource4);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }
}