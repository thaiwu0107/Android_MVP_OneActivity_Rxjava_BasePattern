package com.pattern.thai.patternapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;
import static java.lang.String.format;

/**
 * Created by ggttoo44 on 2017/5/8.
 */

public class BitMapUtil {
    private static String bitmap2base64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(byteArray,Base64.NO_WRAP);
    }

    private static Bitmap base642bitmap(String base64){
        byte[] decodedString = Base64.decode(base64, Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static void base64toBitmap(String base64){
        Flowable.just(base64)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(BitMapUtil::base642bitmap)
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Bitmap s) {
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public static String bitmaptobase64(Bitmap bitmap){
        return bitmap2base64(bitmap);
    }

    public static Bitmap saveScaledPhotoToFile(final Bitmap photoBm) {
        //get its orginal dimensions
        final int bmOriginalWidth = photoBm.getWidth();
        final int bmOriginalHeight = photoBm.getHeight();
        final double originalWidthToHeightRatio =  1.0 * bmOriginalWidth / bmOriginalHeight;
        final double originalHeightToWidthRatio =  1.0 * bmOriginalHeight / bmOriginalWidth;
        //choose a maximum height
        final int maxHeight = 560;
        //choose a max width
        final int maxWidth = 560;

        return getScaledBitmap(photoBm, bmOriginalWidth, bmOriginalHeight,
                originalWidthToHeightRatio, originalHeightToWidthRatio,
                maxHeight, maxWidth);
    }

    private static Bitmap getScaledBitmap(Bitmap bm, int bmOriginalWidth, int bmOriginalHeight, double originalWidthToHeightRatio, double originalHeightToWidthRatio, int maxHeight, int maxWidth) {
        if(bmOriginalWidth > maxWidth || bmOriginalHeight > maxHeight) {
            Log.v(TAG, format("RESIZING bitmap FROM %sx%s ", bmOriginalWidth, bmOriginalHeight));

            if(bmOriginalWidth > bmOriginalHeight) {
                bm = scaleDeminsFromWidth(bm, maxWidth, bmOriginalHeight, originalHeightToWidthRatio);
            } else {
                bm = scaleDeminsFromHeight(bm, maxHeight, bmOriginalHeight, originalWidthToHeightRatio);
            }

            Log.v(TAG, format("RESIZED bitmap TO %sx%s ", bm.getWidth(), bm.getHeight()));
        }
        return bm;
    }

    private static Bitmap scaleDeminsFromHeight(Bitmap bm, int maxHeight, int bmOriginalHeight, double originalWidthToHeightRatio) {
        int newHeight = (int) Math.min(maxHeight, bmOriginalHeight * .55);
        int newWidth = (int) (newHeight * originalWidthToHeightRatio);
        bm = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
        return bm;
    }

    private static Bitmap scaleDeminsFromWidth(Bitmap bm, int maxWidth, int bmOriginalWidth, double originalHeightToWidthRatio) {
        //scale the width
        int newWidth = (int) Math.min(maxWidth, bmOriginalWidth * .75);
        int newHeight = (int) (newWidth * originalHeightToWidthRatio);
        bm = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
        return bm;
    }
}
