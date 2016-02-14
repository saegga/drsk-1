package ru.drsk.httptest2.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import ru.drsk.httptest2.R;

/**
 * Created by sergei on 13.02.2016.
 */
public class FileChooseActivity extends AppCompatActivity {

    private ArrayList<File> listFile = new ArrayList<>();
    private ListView listViewFiles;
    private File currentPath;
    private String startPath;
    private ActionBar toolbar;
    private View emptyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        listViewFiles = (ListView) findViewById(R.id.my_list_files);
        emptyView = getLayoutInflater().inflate(R.layout.empty_directory, null);
        addContentView(emptyView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        listViewFiles.setEmptyView(emptyView);
        toolbar = getSupportActionBar();
        setToolbarTitle("");
        FileAdapter adapter = new FileAdapter(this);
        listViewFiles.setAdapter(adapter);
        File path = new File("/");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory();
            startPath = path.getPath();
        }

        readDirectory(path);

        listViewFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPath = (File) parent.getAdapter().getItem(position);
                setToolbarTitle(currentPath.getAbsolutePath());
                if (currentPath.isFile()) {
                    emptyView.setVisibility(View.INVISIBLE);
                    showConfirmDialog();
                }
                //emptyView.setVisibility(View.VISIBLE);
                readDirectory(currentPath);
            }
        });
    }

    private void readDirectory(File path) {
        listFile.clear();
        currentPath = path;
        File[] files = currentPath.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().charAt(0) == '.') continue;
                listFile.add(files[i]);
            }
        }
        setToolbarTitle(currentPath.getAbsolutePath());
        ((BaseAdapter) listViewFiles.getAdapter()).notifyDataSetChanged();
    }
    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Подтвердите выбранный файл: " + currentPath.getName())
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        complete(currentPath.getPath(), currentPath.getName());
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        readDirectory(currentPath.getParentFile());
                    }
                });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        //File parentFile = currentPath.getParentFile();
        if (currentPath.getPath().equals(startPath)) {
            super.onBackPressed();
            currentPath = null;
            complete(null, null);
        } else {
            File parentFile = currentPath.getParentFile();
            readDirectory(parentFile);
        }
    }


    private void complete(String absPath, String fileName) {
        Intent i = new Intent();
        if (absPath == null && fileName == null) {
            setResult(RESULT_OK, i);
            finish();
        }
        i.putExtra(ActivityAddFile.FILE_ABS_PATH, absPath);
        i.putExtra(ActivityAddFile.FILE_CHOOSE_NAME, fileName);
        setResult(RESULT_OK, i);
        finish();

    }

    public void setToolbarTitle(String title){
        toolbar.setTitle(title);
    }

    class FileAdapter extends BaseAdapter {

        private Context mContext;

        public FileAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return listFile.size();
        }

        @Override
        public Object getItem(int position) {
            return listFile.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            File file = listFile.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_file_item, parent, false);
            }
            ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);

            if (file.isDirectory()) {
                thumbnail.setImageResource(R.mipmap.ic_folder);
            } else {
                thumbnail.setImageResource(R.mipmap.ic_insert_drive_file_black_24dp);
            }
            TextView filename = (TextView) convertView.findViewById(R.id.filename);
            filename.setText(file.getName());
            return convertView;
        }
    }


}
