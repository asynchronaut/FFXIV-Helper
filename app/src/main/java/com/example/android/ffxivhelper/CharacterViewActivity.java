package com.example.android.ffxivhelper;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.ffxivhelper.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class CharacterViewActivity extends AppCompatActivity {

    private TextView mUrlView;
    private RecyclerView mMountsRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CollectibleAdapter mMountAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_view);

        mMountsRecyclerView = findViewById(R.id.rv_mounts);
        mUrlView = findViewById(R.id.tv_character_url_display);

        layoutManager = new LinearLayoutManager(this);
        mMountsRecyclerView.setLayoutManager(layoutManager);
        mMountAdapter = new CollectibleAdapter();
        mMountsRecyclerView.setAdapter(mMountAdapter);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                String characterId = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                makeXivapiCharacterQuery(characterId);
            }
        }
    }

    private void makeXivapiCharacterQuery(String characterId) {
        URL xivapiSearchUrl = NetworkUtils.buildCharacterUrl(characterId);
        mUrlView.setText(xivapiSearchUrl.toString());
        new CharacterViewActivity.xivapiQueryTask().execute(xivapiSearchUrl);
    }

    public class xivapiQueryTask extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(URL... params) {
            URL searchUrl = params[0];
            String xivapiSearchResults = null;
            JSONObject jsonResults = null;
            try {
                xivapiSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                jsonResults = new JSONObject(xivapiSearchResults);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (final JSONException e) {
                Log.e("FAILED", "Json parsing error: " + e.getMessage());
            }
            return jsonResults;
        }

        @Override
        protected void onPostExecute(JSONObject jsonResults) {
            //mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (jsonResults != null && !jsonResults.equals("")) {
                //showJsonDataView();
                CollectibleObject[] results = null;
                try {
                    JSONObject characterData = jsonResults.getJSONObject("Character");
                    JSONArray mountData = characterData.getJSONArray("Mounts");
                    Log.d("MOUNTS", String.valueOf(mountData.length()) + " mounts found.");
                    results = new CollectibleObject[mountData.length()];
                    for (int i = 0; i < mountData.length(); i++) {
                        String mountId = mountData.getString(i);
                        Log.d("MOUNTS", "Mount " + mountId + " located.");
                        results[i] = new CollectibleObject(mountId,0); //TODO: Pass correct type
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (results == null) {
                    Log.d("MOUNTS", "No mounts found.");
                }
                else {
                    mMountAdapter.setResultsData(results);
                    mMountsRecyclerView.setVisibility(View.VISIBLE);
                }
            } else {
                //showErrorMessage();
            }
        }
    }


}
