package taewon.navercorp.integratedsns.video;

import android.util.Log;
import android.view.View;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import taewon.navercorp.integratedsns.R;

/**
 * Created by USER on 2017-11-21.
 */

public class YouTubeFragment extends YouTubePlayerSupportFragment {

    public void youTubeFragmentInitialize(final String videoId, final YouTubeFragment fragment, final View parent) {

        fragment.initialize(getString(R.string.google_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean wasRestored) {

                youTubePlayer.setShowFullscreenButton(false);
                if (!wasRestored) {
                    youTubePlayer.loadVideo(videoId);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                Log.e("app", youTubeInitializationResult.toString());
            }
        });
    }
}
