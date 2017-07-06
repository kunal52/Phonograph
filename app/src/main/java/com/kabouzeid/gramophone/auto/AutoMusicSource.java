package com.kabouzeid.gramophone.auto;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.media.MediaMetadataCompat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Beesham on 3/28/2017.
 */
public class AutoMusicSource implements MusicProviderSource {

    private static final String TAG = AutoMusicSource.class.getName();
    private Context mContext;

    public AutoMusicSource(Context context) {
        mContext = context;
    }

    @Override
    public Iterator<MediaMetadataCompat> iterator() {
        // All songs
        final ContentResolver contentResolver = mContext.getContentResolver();
        final Uri uriSongs = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        final Cursor cursor = contentResolver.query(uriSongs, null, null, null, null);

        final List<MediaMetadataCompat> tracks = new ArrayList<>();

        if (cursor == null) {
            return null;
        } else if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {    //TODO: change this t0 cursor.count()
                tracks.add(buildSongsMediaMetadata(cursor));
                cursor.moveToNext();
            }
        }

        return tracks.iterator();
    }

    private MediaMetadataCompat buildSongsMediaMetadata(Cursor c) {
        final String _ID = c.getString(c.getColumnIndex(MediaStore.Audio.Media._ID));
        final String source = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));
        final String album = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM));
        final String albumId = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
        final String artist = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        final int duration = c.getInt(c.getColumnIndex(MediaStore.Audio.Media.DURATION)) * 1000; // ms
        final String title = c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE));
        final int trackNumber = c.getInt(c.getColumnIndex(MediaStore.Audio.Media.TRACK));

        return new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, _ID)
                .putString(MusicProviderSource.CUSTOM_METADATA_TRACK_SOURCE, source)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album)
                .putString(MusicProviderSource.CUSTOM_METADATA_ALBUM_ID, albumId)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, trackNumber)
                .build();
    }
}