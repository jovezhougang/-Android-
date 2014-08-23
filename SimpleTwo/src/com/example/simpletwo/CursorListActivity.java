package com.example.simpletwo;

import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.ContentObservable;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CursorListActivity extends ListActivity
        implements
            LoaderCallbacks<Cursor> {
    MyContentObserver mMyContentObserver = new MyContentObserver(new Handler());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
        getContentResolver().registerContentObserver(URI_BATCH, false,
                mMyContentObserver);
    }

    class MyContentObserver extends ContentObserver {

        public MyContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            getLoaderManager().restartLoader(0, null, CursorListActivity.this);
        }

    }
    static final Uri URI_BATCH = new Uri.Builder().scheme("content")
            .authority("com.csii.ContentProvider").appendPath("books").build();
    CursorLoader cursorLoader;
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        cursorLoader = new CursorLoader(getApplicationContext(), URI_BATCH,
                null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        getListView().setAdapter(
                new MyCursorAdapter(getApplicationContext(), data, true));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader.startLoading();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // getLoaderManager().restartLoader(0, null, this);
    }
    static class MyCursorAdapter extends CursorAdapter {

        class ViewHolder {
            TextView id;
            TextView name;
            TextView author;
            TextView publish_time;
            TextView modifiy;
        }
        Context context;
        public MyCursorAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
            this.context = context;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {

            return LayoutInflater.from(context).inflate(R.layout.item_layout,
                    null, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            if (null == view.getTag()) {
                ViewHolder holder = new ViewHolder();
                holder.id = (TextView) view.findViewById(R.id.id);
                holder.name = (TextView) view.findViewById(R.id.name);
                holder.author = (TextView) view.findViewById(R.id.author);
                holder.publish_time = (TextView) view
                        .findViewById(R.id.publish_time);
                holder.modifiy = (TextView) view.findViewById(R.id.modifiy);
                view.setTag(holder);
            }
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.id.setText(cursor.getInt(cursor.getColumnIndex("_id")) + "");
            holder.name.setText(cursor.getString(cursor.getColumnIndex("name"))
                    + "");
            holder.author.setText(cursor.getString(cursor
                    .getColumnIndex("author")) + "");
            holder.publish_time.setText(cursor.getString(cursor
                    .getColumnIndex("publish_time")) + "");
            holder.modifiy.setText(cursor.getString(cursor
                    .getColumnIndex("modifiy")) + "");
        }
    }
}
