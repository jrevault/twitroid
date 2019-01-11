package fr.quidquid.twitroid.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import fr.quidquid.twitroid.MainActivity;
import fr.quidquid.twitroid.R;
import twitter4j.QueryResult;
import twitter4j.Status;

public class TwitdroidListFragment extends Fragment {

    MainActivity activity;

    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof Activity) {
            activity = ( MainActivity ) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        setHasOptionsMenu( true );
        View view = inflater.inflate( R.layout.fragment_list, container, false );
        ListView list_tweets = view.findViewById( R.id.list_view);

        Bundle bundle = this.getArguments();
        final QueryResult results = (QueryResult) bundle.getSerializable("data" );


        HashMap<String, String> user_tweet_map = new HashMap<>();
        for (Status status : results.getTweets()) {
            user_tweet_map.put("@"  + status.getUser().getScreenName(), status.getText());
        }
        Log.i("INFO", "There are " + user_tweet_map.size());

        List<HashMap<String, String>> list_items = new ArrayList<>();
        if (user_tweet_map.size() > 0) {
            SimpleAdapter adapter = new SimpleAdapter(
                    getContext(),
                    list_items,
                    R.layout.list_result_item,
                    new String[]{"User", "Tweet"},
                    new int[]{R.id.list_result_item_user, R.id.list_result_item_tweet}
            );
            Iterator ite = user_tweet_map.entrySet().iterator();
            while (ite.hasNext()) {
                HashMap<String, String> result_map = new HashMap<>();
                Map.Entry pair = (Map.Entry) ite.next();
                result_map.put("User", pair.getKey().toString());
                result_map.put("Tweet", pair.getValue().toString());
                list_items.add(result_map);
            }

            list_tweets.setAdapter(adapter);
        }
        return view;
    }

}
