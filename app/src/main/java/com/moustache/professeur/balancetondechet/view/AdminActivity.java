package com.moustache.professeur.balancetondechet.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.moustache.professeur.balancetondechet.R;
import com.moustache.professeur.balancetondechet.model.Trash;
import com.moustache.professeur.balancetondechet.model.Trashes;
import com.moustache.professeur.balancetondechet.persistance.ImageSaver;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    private ListView trashListView;

    private String names[];
    private String descs[];
    private String images[];

    private ImageButton backbuttonAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        backbuttonAdmin = findViewById(R.id.backbuttonAdmin);

        backbuttonAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        names = Trashes.getInstance().getTrashes().stream().map(Trash::getName).toArray(String[]::new);
        descs = Trashes.getInstance().getTrashes().stream().map(Trash::getDesc).toArray(String[]::new);
        images = Trashes.getInstance().getTrashes().stream().map(Trash::getImgPath).toArray(String[]::new);

        for (Trash t : Trashes.getInstance().getTrashes()){
            Log.v("trashes admin",t.toString());
        }

        AdminTrashListAdapter adapter=new AdminTrashListAdapter(this, names, descs,images);

        trashListView = (ListView) findViewById(R.id.trash_list_view);
        trashListView.setAdapter(adapter);

        trashListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trash currentTrash = Trashes.getInstance().getTrashes().get(position);
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AdminActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_trash_admin,null);
                TextView trashName =(TextView)  mView.findViewById(R.id.trash_name_dialog);
                TextView trashDesc =(TextView) mView.findViewById(R.id.trash_desc_dialog);
                ImageView trashImg = (ImageView) mView.findViewById(R.id.trash_img_dialog);
                Button approuverbutton = mView.findViewById((R.id.approuver_button));
                Button supprimerButton = mView.findViewById((R.id.supprimer_button));

                trashName.setText(currentTrash.getName());
                trashImg.setImageBitmap(new ImageSaver(getApplicationContext()).
                        setFileName(currentTrash.getImgPath()).
                        setDirectoryName("images").
                        load());
                trashDesc.setText(currentTrash.getDesc());

                approuverbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Trashes.getInstance().getTrashes().get(position).setApproved();
                        Toast.makeText(getApplicationContext(),"Le post a été approuvé",Toast.LENGTH_SHORT).show();
                    }
                });

                supprimerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Trashes.getInstance().getTrashes().remove(position);
                        Toast.makeText(getApplicationContext(),"Le post a été supprimé",Toast.LENGTH_SHORT).show();
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

    }
}