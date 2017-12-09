package edu.illinois.finalproject.clarifai;

import android.graphics.Bitmap;
import android.os.AsyncTask;
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

public class ClarifaiAsync extends AsyncTask<Bitmap, Integer, List<String>> {

    private static ClarifaiClient client;
    private TagsAdapter adapter;

    public ClarifaiAsync(TagsAdapter adapter) {
        this.adapter = adapter;
        client = new ClarifaiBuilder(ClarifaiApiKey.KEY).buildSync();
    }

    @Override
    protected List<String> doInBackground(Bitmap... bitmaps) {
        if (bitmaps == null) {
            return null;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmaps[0].compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageData = stream.toByteArray();

        try {
            List<ClarifaiOutput<Concept>> predictionResults = client.getDefaultModels()
                    .generalModel().predict().withInputs(ClarifaiInput.forImage(imageData))
                    .executeSync()
                    .get();

            List<String> results = new ArrayList<>();
            for (Concept concept : predictionResults.get(0).data()) {
                results.add(concept.name());
            }

            return results;

        } catch (java.util.NoSuchElementException e) {
            // throws this error if user quits the activity too early
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<String> results) {
        if (results != null) {
            adapter.setTagsList(results);
            adapter.notifyDataSetChanged();
        }
    }
}
