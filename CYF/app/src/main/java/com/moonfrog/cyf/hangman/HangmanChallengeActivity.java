package com.moonfrog.cyf.hangman;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;
import com.moonfrog.cyf.Globals;
import com.moonfrog.cyf.R;
import com.moonfrog.cyf.view.CategoryWordChallengeActivity;
import com.moonfrog.cyf.view.LetterSpacingTextView;
import com.moonfrog.cyf.view.ViewUpdateCall;

import org.json.JSONObject;

/**
 * Created by srinath on 30/03/15.
 */
public class HangmanChallengeActivity extends CategoryWordChallengeActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        challenge_layouts = new int[] {
                R.layout.challenge_hangman_gif_1,
                R.layout.challenge_hangman_gif_2,
                R.layout.challenge_hangman_gif_3
        };
        challenge_fragment_class = HangmanChallengeFragment.class;
        super.onCreate(savedInstanceState);
        shareChallenge = new Runnable() {
            @Override
            public void run() {
                String metadata = "";
                try {
                    JSONObject metadataJson = new JSONObject();
                    metadataJson.put("challenge", Globals.encrypt(selectedWord));
                    metadataJson.put("name", Globals.name);
                    metadataJson.put("type", "hangman");
                    metadata = metadataJson.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Uri contentUri = ((new Uri.Builder()).scheme("file").appendPath(Globals.gifPath)).build();
                ShareToMessengerParams shareToMessengerParams = ShareToMessengerParams.newBuilder(contentUri, "image/gif").setMetaData(metadata).build();

                MessengerUtils.shareToMessenger(static_instance, 10, shareToMessengerParams);
            }
        };
    }

    @Override
    protected ViewUpdateCall[] getViewChanges() {
        ViewUpdateCall[] viewChanges = {
                new ViewUpdateCall() {
                    @Override
                    public void updateView(View v) {
                        TextView vTemp = (TextView) v.findViewById(R.id.name);
                        vTemp.setText(Globals.name);
                    }
                },
                new ViewUpdateCall() {
                    @Override
                    public void updateView(View v) {

                    }
                },
                new ViewUpdateCall() {
                    @Override
                    public void updateView(View v) {
                        LetterSpacingTextView tv = (LetterSpacingTextView) v.findViewById(R.id.challengeWord);
                        tv.setLetterSpacing_(12); //Or any float. To reset to normal, use 0 or LetterSpacingTextView.Spacing.NORMAL
                        String current_status = selectedWord.replaceAll("[A-Z]", "_");
                        tv.setText(current_status);

                        TextView tv2 = (TextView) v.findViewById(R.id.questionText);
                        String text = "Guess the\n" + selectedTopic + "!";
                        tv2.setText(text);
                    }
                }
        };
        return viewChanges;
    }
}
