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
 * This AsyncTask is used to make requests to the Clarifai API. Given a bitmap, it will request
 * Clarifai to gives suggestions about what is in the bitmap. The API returns a List of Clarifai
 * Concept objects which have details about each item the API suggests, specifically, the name of
 * what the API believes is in the picture. These Concept objects are ordered by confidence, and
 * this task will choose the first {MAX_SUGGESTIONS} suggestions to add to the TagsAdapter. Once the
 * Clarifai results come in, this AsyncTask can add each suggested tag into the adapter.
 */

public class ClarifaiAsync extends AsyncTask<Bitmap, Integer, List<String>> {

    // maximum number of suggestions this app will display
    private static final int MAX_SUGGESSTIONS = 8;
    private static final int BITMAP_QUALITY = 100;

    private static ClarifaiClient client;
    private TagsAdapter adapter;

    public ClarifaiAsync(TagsAdapter adapter) {
        this.adapter = adapter;
        client = new ClarifaiBuilder(ClarifaiApiKey.KEY).buildSync();
    }

    /**
     * This is the background thread that will make requests to Clarifai. Given a bitmap, it will
     * ask Clarifai to predict the subjects of that bitmap. Then, it will return the results
     * @param bitmaps
     * @return
     */
    @Override
    protected List<String> doInBackground(Bitmap... bitmaps) {
        if (bitmaps[0] == null) {
            return null;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmaps[0].compress(Bitmap.CompressFormat.PNG, BITMAP_QUALITY, stream);
        byte[] imageData = stream.toByteArray();

        try {
            List<ClarifaiOutput<Concept>> predictionResults = client.getDefaultModels()
                    .generalModel().predict().withInputs(ClarifaiInput.forImage(imageData))
                    .executeSync()
                    .get();

            List<Concept> clarifaiData = predictionResults.get(0).data();
            List<String> results = new ArrayList<>();
            int upperBound = Math.min(MAX_SUGGESSTIONS, clarifaiData.size());
            for (int i = 0; i < upperBound; i++) {
                results.add(clarifaiData.get(i).name());
            }

            return results;

        } catch (OutOfMemoryError | java.util.NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Once the requests have been made, add the list of results to the TagsAdapter, which will
     * display to the user the results from Clarifai.
     * @param results
     */
    @Override
    protected void onPostExecute(List<String> results) {
        if (results != null) {
            for (String tag : results) {
                adapter.addTags(tag);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
