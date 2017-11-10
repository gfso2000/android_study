package com.example.i325639.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements HeadlinesFragment.OnHeadlineSelectedListener {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    ShareActionProvider mShareActionProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(this.findViewById(R.id.fragment_container) != null) {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            HeadlinesFragment fragment = new HeadlinesFragment();
            this.getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        MenuItem item = menu.findItem(R.id.item1);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        mShareActionProvider.setShareIntent(pickContactIntent);
        return true;
    }

    public void shareMessage(MenuItem view) {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        mShareActionProvider.setShareIntent(pickContactIntent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(this.getApplicationContext(),"response",Toast.LENGTH_LONG).show();
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Uri contactUri = data.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(column);
                Toast.makeText(this.getApplicationContext(), "aaa:"+number, Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == 22222  && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView mImageView = (ImageView)findViewById(R.id.imageView);
            mImageView.setImageBitmap(imageBitmap);
        } else if (requestCode == 33333  && resultCode == RESULT_OK) {
            //the image is stored to external file
            ImageView mImageView = (ImageView)findViewById(R.id.imageView);
            int targetW = mImageView.getWidth();
            int targetH = mImageView.getHeight();

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            mImageView.setImageBitmap(bitmap);

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            Uri photoURI = FileProvider.getUriForFile(this,"com.example.myapp.fileprovider", f);
            mediaScanIntent.setData(photoURI);
            //if (mediaScanIntent.resolveActivity(getPackageManager()) != null) {
                this.sendBroadcast(mediaScanIntent);
            //}

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(this, "com.example.myapp.fileprovider", f);
            intent.setDataAndType(uri, "image/jpeg");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        } else if (requestCode == 44444  && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            VideoView mVideoView = (VideoView)findViewById(R.id.videoView);
            mVideoView.setVideoURI(videoUri);
        }
    }

    String mCurrentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1111:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this.getApplicationContext(),"got permission", Toast.LENGTH_LONG).show();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this.getApplicationContext(),"no permission", Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
        }
    }

    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

//        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
//        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
//        startActivityForResult(pickContactIntent, 1);

//        File subdir=new File(getFilesDir(), "images");
//        if(!subdir.exists()) {
//            subdir.mkdirs();
//        }
//        String filename = "myfile";
//        File file = new File(this.getFilesDir()+"/images", filename);
//        String string = "<html><body>Hello world!"+(new Date())+"</body></html>";
//        try {
//            FileOutputStream fos = new FileOutputStream(file);
//            fos.write(string.getBytes());
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        Uri uri = FileProvider.getUriForFile(this, "com.example.myapp.fileprovider", file);
//        intent.setDataAndType(uri, "text/plain");
//        PackageManager pm = this.getPackageManager();
//        if (intent.resolveActivity(pm) != null) {
//            startActivity(intent);
//        }

//        Uri imageUri = Uri.parse("android.resource://" + getPackageName() + "/drawable/icons8_share.png");
//        Intent sendIntent = new Intent();
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//        sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//        sendIntent.setData(imageUri);
//        sendIntent.setType("image/png");
//        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        PackageManager pm = this.getPackageManager();
//        if (sendIntent.resolveActivity(pm) != null) {
//            startActivity(sendIntent);
//        }

//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, 22222);
//        }

//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//            }
//            Uri photoURI = FileProvider.getUriForFile(this,"com.example.myapp.fileprovider", photoFile);
//            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//            startActivityForResult(takePictureIntent, 33333);
//        }

//        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takeVideoIntent, 44444);
//        }
    }

    @Override
    public void onArticleSelected(int position) {
        // The user selected the headline of an article from the HeadlinesFragment

        // Capture the article fragment from the activity layout
        ArticleFragment articleFrag = (ArticleFragment)
                getSupportFragmentManager().findFragmentById(R.id.article_fragment);
        if (articleFrag != null) {
            // If article frag is available, we're in two-pane layout...

            // Call a method in the ArticleFragment to update its content
            articleFrag.updateArticleView(position);

        } else {
            // If the frag is not available, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            ArticleFragment newFragment = new ArticleFragment();
            Bundle args = new Bundle();
            args.putInt(ArticleFragment.ARG_POSITION, position);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    }
}
