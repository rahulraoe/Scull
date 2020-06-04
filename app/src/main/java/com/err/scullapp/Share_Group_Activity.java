package com.err.scullapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Share_Group_Activity extends AppCompatActivity {
    TextView group_id,group_password;

    String DOC_ID,Active_Group;
FirebaseFirestore db=FirebaseFirestore.getInstance();

TextView click_to_copy;
String ID,PASSWORD;
    Button whatsapp;
    Button cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppBarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share__group_);

        getSupportActionBar().setTitle("Share Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        group_id=(TextView)findViewById(R.id.group_id);
        group_password=(TextView)findViewById(R.id.group_password);

          SharedPreferences preferences=getApplicationContext().getSharedPreferences("User_Details",0);
          DOC_ID=preferences.getString("Active_Group_Id","empty");
        Active_Group=preferences.getString("Active_Group","Empty");

          db.collection("GROUPS").document(DOC_ID)
                  .get()
                  .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                      @Override
                      public void onSuccess(DocumentSnapshot snapshot) {
                          ID=snapshot.getString("Group_Id");
                                  PASSWORD=snapshot.getString("Group_Password");
                          group_id.setText(ID);
                          group_password.setText(PASSWORD);
                      }
                  });






          AdLoader.Builder builder = new AdLoader.Builder(
                this, "ca-app-pub-3940256099942544/2247696110");

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                TemplateView template = findViewById(R.id.my_template);
                template.setNativeAd(unifiedNativeAd);
            }
        });

        AdLoader adLoader = builder.build();
        adLoader.loadAd(new AdRequest.Builder().build());





        click_to_copy=(TextView) findViewById(R.id.click_copy);
        click_to_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Share Group","Id :"+ID+"\n"+"Password :"+PASSWORD );
                clipboard.setPrimaryClip(clip);
                Toast.makeText(Share_Group_Activity.this, "Code copied", Toast.LENGTH_LONG).show();
            }
        });

        whatsapp=(Button)findViewById(R.id.share_in_whatsapp);
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager pm=getPackageManager();
                try {

                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                    waIntent.setType("text/plain");


                    String text="Join my group"+Active_Group+" on scull.By joining you can view all our trip memories."+"\n"+
                            "Id :"+ID+"\n"+"Password :"+PASSWORD+".\nInstall app from playstore\n"+
                   "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";

                    PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    //Check if package exists or not. If not then code
                    //in catch block will be called
                    waIntent.setPackage("com.whatsapp");

                    waIntent.putExtra(Intent.EXTRA_TEXT, text);
                    startActivity(Intent.createChooser(waIntent, "Share with"));

                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(Share_Group_Activity.this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });

        cardView=(Button) findViewById(R.id.referal_card);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String text="Join my group"+Active_Group+" on scull.By joining you can view all our trip memories."+"\n"+
                            "Id :"+ID+"\n"+"Password :"+PASSWORD+".\nInstall app from playstore\n"+
                            "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                    startActivity(Intent.createChooser(shareIntent, "Choose One"));
                } catch(Exception e) {
                    Toast.makeText(Share_Group_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
