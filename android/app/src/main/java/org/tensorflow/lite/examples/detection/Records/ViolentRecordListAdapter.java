package org.tensorflow.lite.examples.detection.Records;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.tensorflow.lite.examples.detection.KurumsalScreen.KurumsalRecords2Screen;
import org.tensorflow.lite.examples.detection.R;

import java.util.List;

public class ViolentRecordListAdapter extends ArrayAdapter<Record> {

    private Context context;
    private int resource;

    public ViolentRecordListAdapter(@NonNull Context context, int resource, @NonNull List<Record> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Record record = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);


        TextView vrli_timeTV = convertView.findViewById(R.id.vrli_timeTV);

        vrli_timeTV.setText(String.valueOf(position + 1) + ".  " + "İhlal Zamanı : " + record.getStartTime());

        return convertView;
    }
}
