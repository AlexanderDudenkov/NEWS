package com.dudencovgmail.news.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import com.dudencovgmail.news.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoadPhotoUrl {
    private Bitmap mBitmap;
    private Context mContext;
    private Article mArticle;

    public void loadPhoto(Context context, ImageView imageView, String url) {
        mContext = context;
        Picasso.with(context)
                .load(url)
                .placeholder(R.color.cardview_dark_background)
                .into(imageView);
    }

    public Bitmap downloadImage() {
        String url = mArticle.getUrlImage();
        Picasso.with(mContext).
                load(url).
                placeholder(R.color.cardview_dark_background).
                into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        mBitmap = bitmap;
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        Toast.makeText(mContext, R.string.error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
        return mBitmap;
    }

    public File getImageFile(Article article) {
        mArticle = article;
        FileOutputStream fileOutputStream;
        File pathDcim = Environment.getExternalStorageDirectory();
        File dirNews = new File(pathDcim.getAbsolutePath() + "/NEWS");
        dirNews.mkdirs();

        if (!dirNews.exists() && !dirNews.mkdirs()) {
            Toast.makeText(mContext, R.string.failure_create_dir, Toast.LENGTH_SHORT).show();
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(mContext.getString(R.string.date_format));
        String date = simpleDateFormat.format(new Date());
        String name = "Img" + mArticle.getIdArticle() + "_" + date + ".jpg";
        String fileName = dirNews.getAbsolutePath() + "/" + name;
        File newFile = new File(fileName);
        try {
            fileOutputStream = new FileOutputStream(newFile);
            Bitmap bitmap = downloadImage();
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                Toast.makeText(mContext, mContext.getString(R.string.save_file_success), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, R.string.error, Toast.LENGTH_SHORT).show();
            }
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFile;
    }

    public void deleteImage(Article article) {
        File file = new File(article.getFileName());
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            file.delete();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
