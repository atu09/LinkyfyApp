package com.atirek.alm.linkyfyapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.volokh.danylo.hashtaghelper.HashTagHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    Button btn_hash;
    TextView tv_hash, tv_hashView;
    EditText et_hash;
    HashTagHelper tv_hashTagHelper, et_hashTagHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uri data = this.getIntent().getData();
        if (data != null && data.isHierarchical()) {
            String uri = this.getIntent().getDataString();
            Log.i("Linkify", "Deep link clicked " + uri);
        }

        btn_hash = (Button) findViewById(R.id.btn_hash);
        tv_hash = (TextView) findViewById(R.id.tv_hash);
        tv_hashView = (TextView) findViewById(R.id.tv_hashView);
        et_hash = (EditText) findViewById(R.id.et_hash);


        Linkify.addLinks(et_hash, Linkify.WEB_URLS);
        Linkify.addLinks(tv_hash, Linkify.WEB_URLS);

        final Linkify.TransformFilter filter = new Linkify.TransformFilter() {
            public final String transformUrl(final Matcher match, String url) {
                return match.group();
            }
        };

        //final Pattern hashtagPattern = Pattern.compile("#([ء-يA-Za-z0-9_-]+)");
        final Pattern hashtagPattern = Pattern.compile("#([A-Za-z0-9-_]+)");
        final String hashtagScheme = "content://com.hashtag.jojo/";
        char[] mAdditionalHashTagChars = {'$', '_', '-'};
        final Pattern urlPattern = Patterns.WEB_URL;

        et_hash.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                System.out.println(count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Linkify.addLinks(s, hashtagPattern, hashtagScheme, null, filter);
                Linkify.addLinks(s, urlPattern, null, null, filter);
            }
        });


        tv_hash.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                System.out.println(count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Linkify.addLinks(s, hashtagPattern, hashtagScheme, null, filter);
                Linkify.addLinks(s, urlPattern, null, null, filter);
            }
        });


        tv_hashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimaryDark), new HashTagHelper.OnHashTagClickListener() {
            @Override
            public void onHashTagClicked(String hashTag) {
                tv_hashView.setText(hashTag);

            }
        }, mAdditionalHashTagChars);

        // pass a TextView or any descendant of it (incliding EditText) here.
        // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
        tv_hashTagHelper.handle(tv_hash);


        et_hashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimaryDark), null);

        // pass a TextView or any descendant of it (incliding EditText) here.
        // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
        et_hashTagHelper.handle(et_hash);

        btn_hash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tv_hash.setText(et_hash.getText());
                //String text = "<a href='https://linkify.com/app/'>LinkifyApp</a>";
                //shareOptions(MainActivity.this, Html.fromHtml(text));

            }
        });


    }

    private static Bitmap takeScreenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    private static void savePic(Bitmap b, String strFileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(strFileName);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void share() {

        //  BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        //    Bitmap bitmap = drawable.getBitmap();
        Bitmap icon = takeScreenShot(MainActivity.this);
        ;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "temporary_file.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        share.putExtra(Intent.EXTRA_TEXT, "https://yelow.in/product/yelow-kits/ready-kits/Wanderer---Mens-Travel-Kit/MzE4");
        share.putExtra(Intent.EXTRA_STREAM,
                Uri.parse("file:///sdcard/temporary_file.jpg"));
        startActivity(Intent.createChooser(share, "Share Image"));

    }


    public static void shareOptions(Context context, Spanned shareString) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareString);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

}
