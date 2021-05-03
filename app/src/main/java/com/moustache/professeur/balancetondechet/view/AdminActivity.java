package com.moustache.professeur.balancetondechet.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.moustache.professeur.balancetondechet.R;
import com.moustache.professeur.balancetondechet.model.PendingTrashes;
import com.moustache.professeur.balancetondechet.model.Trash;
import com.moustache.professeur.balancetondechet.model.Trashes;
import com.moustache.professeur.balancetondechet.persistance.ImageSaver;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

public class AdminActivity extends AppCompatActivity implements Observer {
    private ListView trashListView;
    private ArrayAdapter adapter;
    private ImageButton backbuttonAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        PendingTrashes.getInstance().addObserver(this);

        backbuttonAdmin = findViewById(R.id.backbuttonAdmin);
        backbuttonAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        for (Trash t : PendingTrashes.getInstance().getPendingTrashes()){
            Log.v("trashes admin",t.toString());
        }

        adapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1, PendingTrashes.getInstance().getPendingTrashes().stream().map(Trash::getName).collect(Collectors.toList()));

        trashListView = (ListView) findViewById(R.id.trash_list_view);
        trashListView.setAdapter(adapter);

        trashListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trash currentTrash = PendingTrashes.getInstance().getPendingTrashes().get(position);
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AdminActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_trash_admin,null);
                TextView trashName =(TextView)  mView.findViewById(R.id.trash_name_dialog);
                TextView trashDesc =(TextView) mView.findViewById(R.id.trash_desc_dialog);
                TextView trashType= (TextView) mView.findViewById(R.id.trash_type_dialog);
                ImageView trashImg = (ImageView) mView.findViewById(R.id.trash_img_dialog);
                Button approuverbutton = mView.findViewById((R.id.approuver_button));
                Button supprimerButton = mView.findViewById((R.id.supprimer_button));

                trashName.setText(currentTrash.getName());
                trashImg.setImageBitmap(new ImageSaver(getApplicationContext()).
                        setFileName(currentTrash.getImgPath()).
                        setDirectoryName("images").
                        load());
                trashDesc.setText(currentTrash.getDesc());
                trashType.setText("Type : "+currentTrash.getType().toString());

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

                approuverbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PendingTrashes.getInstance().getPendingTrashes().get(position).setApproved();
                        Trash t = PendingTrashes.getInstance().getPendingTrashes().get(position);
                        PendingTrashes.getInstance().remove(position);
                        Trashes.getInstance().add(t);
                        Toast.makeText(getApplicationContext(),"Le post a été approuvé",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                supprimerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PendingTrashes.getInstance().remove(position);
                        Toast.makeText(getApplicationContext(),"Le post a été supprimé",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

            }
        });

    }

    @Override
    public void update(Observable o, Object arg) {
        Log.v("update","triggered");
        Trash t = (Trash) arg;
        if (t!=null) {
            Log.v("update",t.toString()+" a été supprimé"+PendingTrashes.getInstance().getPendingTrashes().toString());
            adapter.clear();
            adapter.addAll(PendingTrashes.getInstance().getPendingTrashes().stream().map(Trash::getName).collect(Collectors.toList()));
            adapter.notifyDataSetChanged();
        }
    }
}