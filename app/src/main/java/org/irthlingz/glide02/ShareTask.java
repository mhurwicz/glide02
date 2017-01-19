package com.example.glide02;
/**
 * Created by mh on 1/18/17.
 */
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.FileProvider;
import android.util.Log;
import java.io.File;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

class ShareTask extends AsyncTask<String, Void, File> {
    private final Context context;

    public ShareTask(Context context) {
        this.context = context;
    }
    @Override protected File doInBackground(String... params) {
        String url = params[0]; // should be easy to extend to share multiple images at once
        try {
            return Glide
                    .with(context)
                    .load(url)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get() // needs to be called on background thread
                    ;
        } catch (Exception ex) {
            Log.w("SHARE", "Sharing " + url + " failed", ex);
            return null;
        }
    }
    @Override protected void onPostExecute(File result) {
        if (result == null) { return; }
        Uri uri = FileProvider.getUriForFile(context, context.getPackageName(), result);
        share(uri); // startActivity probably needs UI thread
    }

    private void share(Uri result) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Shared image");
        intent.putExtra(Intent.EXTRA_TEXT, "Look what I found!");
        intent.putExtra(Intent.EXTRA_STREAM, result);
        context.startActivity(Intent.createChooser(intent, "Share image"));
    }
}


