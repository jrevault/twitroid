package fr.quidquid.twitroid.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fr.quidquid.twitroid.MainActivity;
import fr.quidquid.twitroid.R;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import static android.net.Uri.encode;

public class TwitdroidSearchFragment extends Fragment {

    MainActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (MainActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        final EditText search_hashtag = view.findViewById(R.id.search_hashtag);
        final Button search_btn = view.findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Let's call Twitter API !", Toast.LENGTH_SHORT).show();

                // Checking internet connection
                ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {

                    // Key words
                    String hashtag = search_hashtag.getText().toString();
                    // number of result we want
                    String count = "10";
                    // Language of the results
                    String lang = getResources().getConfiguration().getLocales().get(0).getLanguage();

                    new RequestTask().execute(hashtag, count, lang);
                } else {
                    Toast.makeText(getContext(), "No internet connection!\nPlease, retry later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private class RequestTask extends AsyncTask<String, String, QueryResult> {
        @Override
        protected QueryResult doInBackground(String... params) {
            try {
                final String CONSUMER_API_KEY = getString(R.string.consumer_api_key);
                final String CONSUMER_API_KEY_SECRET = getString(R.string.consumer_api_key_secret);
                final String ACCESS_TOKEN = getString(R.string.access_token);
                final String ACCESS_TOKEN_SECRET = getString(R.string.access_token_secret);

                Twitter twitter = new TwitterFactory().getInstance();
                // Twitter Consumer key & Consumer Secret
                twitter.setOAuthConsumer(CONSUMER_API_KEY, CONSUMER_API_KEY_SECRET);

                // Twitter Access token & Access token Secret
                AccessToken accessToken = new AccessToken(ACCESS_TOKEN, ACCESS_TOKEN_SECRET);
                twitter.setOAuthAccessToken(accessToken);

                Query query = new Query( params[0] );
                query.count(Integer.parseInt(params[1]));
                query.lang(params[2]);

                return twitter.search(query);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(QueryResult result) {
            super.onPostExecute(result);
            if (result != null) {
                // and send the token to the choice fragment
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", result);
                // And then load the user list screen
                activity.load_fragment(TwitdroidListFragment.class, bundle);
            } else {
                Toast.makeText(getContext(), "I'm terribly sorry, but the passphrase is incorrect...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
