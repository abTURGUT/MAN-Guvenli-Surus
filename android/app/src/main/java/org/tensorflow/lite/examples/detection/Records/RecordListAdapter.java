package org.tensorflow.lite.examples.detection.Records;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.tensorflow.lite.examples.detection.KurumsalScreen.KurumsalRecords2Screen;
import org.tensorflow.lite.examples.detection.KurumsalScreen.KurumsalRecords3Screen;
import org.tensorflow.lite.examples.detection.R;

import java.util.ArrayList;
import java.util.List;

public class RecordListAdapter extends ArrayAdapter<Record> {

    private Context context;
    private int resource;

    public RecordListAdapter(@NonNull Context context, int resource, @NonNull List<Record> objects) {
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

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, KurumsalRecords3Screen.class);
                intent.putExtra("userId", record.getUserId());
                intent.putExtra("recordId", record.getId());
                intent.putExtra("userFirstName", record.getUserFirstName());
                intent.putExtra("userLastName", record.getUserLastName());
                intent.putExtra("recordDate", record.getDate());
                context.startActivity(intent);
            }
        });

        TextView rli_dateTV = convertView.findViewById(R.id.rli_dateTV);
        TextView rli_timeTV = convertView.findViewById(R.id.rli_timeTV);

        rli_dateTV.setText("Kayıt Tarihi : " + record.getDate());
        rli_timeTV.setText("Kayıt Süresi : " + record.getStartTime() + " - " + record.getEndTime());


        return convertView;
    }
}
