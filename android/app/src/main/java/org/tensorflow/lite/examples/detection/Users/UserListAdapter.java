package org.tensorflow.lite.examples.detection.Users;

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
import org.tensorflow.lite.examples.detection.LoginScreen.LoginScreenBireyselCode2;
import org.tensorflow.lite.examples.detection.R;

import java.util.List;
import java.util.Locale;

public class UserListAdapter extends ArrayAdapter<User> {

    private Context context;
    private int resource;

    public UserListAdapter(@NonNull Context context, int resource, @NonNull List<User> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        User user = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(user.getActive().equals("1")){
                    Intent intent = new Intent(context, KurumsalRecords2Screen.class);
                    intent.putExtra("userId", user.getId());
                    intent.putExtra("recordId", user.getRecordId());
                    intent.putExtra("firstName", user.getFirstName());
                    intent.putExtra("lastName",user.getLastName());
                    context.startActivity(intent);
                }
                else{
                    Toast.makeText(context, "Kullanıcı aktif değil !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView uli_nameTV = convertView.findViewById(R.id.uli_nameTV);
        TextView uli_activeTV = convertView.findViewById(R.id.uli_activeTV);

        String activeStatus = "";

        if(user.getActive().equals("0")){ activeStatus = "Kayıtlı Değil"; }
        else { activeStatus = "Kayıtlı"; }

        if(activeStatus.equals("Kayıtlı")){
            uli_nameTV.setText("İsim - Soyisim : " + user.getFirstName().toUpperCase(Locale.ROOT) + " " + user.getLastName().toUpperCase(Locale.ROOT));
        }
        else{
            uli_nameTV.setText("Mail : " + user.getMail());
        }

        uli_activeTV.setText("Kayıt Durumu : " + activeStatus);


        return convertView;
    }
}
