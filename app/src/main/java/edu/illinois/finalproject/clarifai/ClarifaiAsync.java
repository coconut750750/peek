package edu.illinois.finalproject.clarifai;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import edu.illinois.finalproject.upload.TagsAdapter;

/**
 * Created by Brandon on 12/4/17.
 */

public class ClarifaiAsync extends AsyncTask<byte[], Integer, String[]> {

    private static ClarifaiClient client= new ClarifaiBuilder(ClarifaiApiKey.KEY).buildSync();
    private TagsAdapter adapter;

    public ClarifaiAsync(TagsAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected String[] doInBackground(byte[]... bytes) {
        if (bytes == null) {
            return null;
        }

        List<ClarifaiOutput<Concept>> predictionResults = client.getDefaultModels().generalModel()
                .predict().withInputs(ClarifaiInput.forImage(bytes[0]))
                .executeSync()
                .get();


        List<String> results = new ArrayList<>();
        for (ClarifaiOutput<Concept> output : predictionResults) {
            for (Concept concept : output.data()) {
                results.add(concept.name());
            }
        }

        return results.toArray(new String[results.size()]);
    }

    @Override
    protected void onPostExecute(String[] strings) {
        for(String s : strings) {
            Log.d("asdf", s);
        }
        adapter.setTagsArray(strings);
        adapter.notifyDataSetChanged();
    }
}
