package com.hooooong.pholar.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.hooooong.pholar.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Hong on 2017-11-07.
 */
public class GalleryUtil {

    /**
     * Gallery 이미지 반환
     *
     * @param context
     * @return
     */
    public static List<Photo> getAllPhotoPathList(final Context context) {
        ArrayList<Photo> photoList = new ArrayList<>();

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_MODIFIED
        };

        Cursor cursor = context.getContentResolver().query(
                uri,
                projection,
                null,
                null,
                projection[1] + " DESC");
        int columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        while (cursor.moveToNext()) {
            Photo photo = new Photo(cursor.getString(columnIndexData), null);
            photoList.add(photo);
        }
        cursor.close();

        return photoList;
    }


    /**
     * Galery 이미지 반환
     *
     * @param context
     * @return
     */
    public static List<Photo> fetchAllImages(Context context) {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};

        Cursor imageCursor = context.getContentResolver().query(
                uri, // 이미지 컨텐트 테이블
                projection,                                  // DATA, _ID를 출력
                null,       // 모든 개체 출력
                null,
                null);      // 정렬 안 함

        List<Photo> result = new ArrayList<>(imageCursor.getCount());

        int dataColumnIndex = imageCursor.getColumnIndex(projection[0]);
        int idColumnIndex = imageCursor.getColumnIndex(projection[1]);

        if (imageCursor.moveToFirst()) {
            do {
                String filePath = imageCursor.getString(dataColumnIndex);
                String imageId = imageCursor.getString(idColumnIndex);

                Uri thumbnailUri = uriToThumbnail(context, imageId);
                Uri imageUri = Uri.parse(filePath);

                // 원본 이미지와 썸네일 이미지의 uri 를 모두 담을 수 있는 클래스를 선언합니다.
                Photo photo = new Photo(imageUri.getPath(), thumbnailUri.getPath());
                result.add(photo);
            } while (imageCursor.moveToNext());
        } else {
            // imageCursor 가 비었습니다.
        }
        imageCursor.close();
        return result;
    }

    private static Uri uriToThumbnail(Context context, String imageId) {
        // DATA는 이미지 파일의 스트림 데이터 경로를 나타냅니다.
        String[] projection = {MediaStore.Images.Thumbnails.DATA};
        ContentResolver contentResolver = context.getContentResolver();

        // 원본 이미지의 _ID가 매개변수 imageId인 썸네일을 출력
        Cursor thumbnailCursor = contentResolver.query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, // 썸네일 컨텐트 테이블
                projection, // DATA를 출력
                MediaStore.Images.Thumbnails.IMAGE_ID + "=?", // IMAGE_ID는 원본 이미지의 _ID를 나타냅니다.
                new String[]{imageId},
                null);

        if (thumbnailCursor.moveToFirst()) {
            int thumbnailColumnIndex = thumbnailCursor.getColumnIndex(projection[0]);

            String thumbnailPath = thumbnailCursor.getString(thumbnailColumnIndex);
            thumbnailCursor.close();
            return Uri.parse(thumbnailPath);
        } else {
            // thumbnailCursor가 비었습니다.
            // 이는 이미지 파일이 있더라도 썸네일이 존재하지 않을 수 있기 때문입니다.
            // 보통 이미지가 생성된 지 얼마 되지 않았을 때 그렇습니다.
            // 썸네일이 존재하지 않을 때에는 아래와 같이 썸네일을 생성하도록 요청합니다
            MediaStore.Images.Thumbnails.getThumbnail(contentResolver, Long.parseLong(imageId), MediaStore.Images.Thumbnails.MINI_KIND, null);
            thumbnailCursor.close();
            return uriToThumbnail(context, imageId);
        }
    }
}
