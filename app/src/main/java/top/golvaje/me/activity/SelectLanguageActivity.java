package top.golvaje.me.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import top.golvaje.me.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 7/14/2017.
 */

public class SelectLanguageActivity  extends ListActivity {



    List<String> myList;
    TextView noInternet;
    Button btnSaveLanguage;
    ImageView select_categorys;
    ArrayList<String> Title = new ArrayList();
    String selectedLang;
    LinearLayout slide_menu_icon;
    SwipeRefreshLayout swipeRefreshLayout;
    CheckBox chk;
    ListView l1;


    public void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_languages);
        this.btnSaveLanguage = ((Button)findViewById(R.id.submitLanguage));
        this.noInternet = ((TextView)findViewById(R.id.noInternet));
        this.chk = ((CheckBox)findViewById(R.id.chkAll));
        this.swipeRefreshLayout = ((SwipeRefreshLayout)findViewById(R.id.swipeRefresh));
        this.swipeRefreshLayout.setColorSchemeResources(new int[] { R.color.yelow });
        this.swipeRefreshLayout.setEnabled(false);
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            public void onRefresh()
            {
                SelectLanguageActivity.this.swipeRefreshLayout.setRefreshing(false);
            }
        });
        this.swipeRefreshLayout.setRefreshing(false);
        if (this.selectedLang != null)
        {
            this.btnSaveLanguage.setText("Save changes");
            this.selectedLang = this.selectedLang.replace(" ", "");
            this.myList = new ArrayList(Arrays.asList(this.selectedLang.split(",")));
        }
        this.Title.add("English");
        this.Title.add("Chinese");
        this.btnSaveLanguage.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
//                if (SelectedLanguageScreen.this.langcount.size() > 0)
//                {
//                    SelectedLanguageScreen.this.langId = SelectedLanguageScreen.this.langcount.toString();
//                    SelectedLanguageScreen.this.langId = SelectedLanguageScreen.this.langId.replace("[", "");
//                    SelectedLanguageScreen.this.langId = SelectedLanguageScreen.this.langId.replace("]", "");
//                    new UpdateLang(SelectedLanguageScreen.this.langcount, SelectedLanguageScreen.this.getApplicationContext());
//                    SelectedLanguageScreen.this.editor.putString("logoutForLanguageSelection", SelectedLanguageScreen.this.langId);
//                    SelectedLanguageScreen.this.editor.apply();
//                    Intent localIntent = new Intent(SelectedLanguageScreen.this, HomeScreen.class);
//                    localIntent.addFlags(335544320);
//                    localIntent.setFlags(32768);
//                    SelectedLanguageScreen.this.finish();
//                    SelectedLanguageScreen.this.startActivity(localIntent);
//                    return;
//                }
//                Toast.makeText(SelectedLanguageScreen.this.getApplicationContext(), "Please select at least one language", 0).show();

                Intent localIntent = new Intent(SelectLanguageActivity.this, HomeActivity.class);
                localIntent.addFlags(335544320);
                localIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                SelectLanguageActivity.this.finish();
                SelectLanguageActivity.this.startActivity(localIntent);
            }
        });
        setLang();
    }


    public void setLang() {

        setListAdapter(new ArrayAdapter(this, R.layout.simple_list_item_multiple_choice, this.Title));
        this.l1 = getListView();
        View.OnClickListener local4 = new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
//                CheckBox localCheckBox = (CheckBox) paramAnonymousView;
//                int i = SelectLanguageActivity.this.getListView().getCount();
//                int j = 0;
//                if (j < i) {
//                    SelectedLanguageScreen.this.getListView().setItemChecked(j, localCheckBox.isChecked());
//                    localCheckBox = (CheckBox) paramAnonymousView;
//                    if (localCheckBox.isChecked()) {
//                        String str2 = (String) SelectedLanguageScreen.this.Id.get(j);
//                        SelectedLanguageScreen.this.langcount.add(str2);
//                        HashSet localHashSet = new HashSet();
//                        localHashSet.addAll(SelectedLanguageScreen.this.langcount);
//                        SelectedLanguageScreen.this.langcount.clear();
//                        SelectedLanguageScreen.this.langcount.addAll(localHashSet);
//                    }
//                    for (; ; ) {
//                        j++;
//                        break;
//                        String str1 = (String) SelectedLanguageScreen.this.Id.get(j);
//                        SelectedLanguageScreen.this.langcount.remove(str1);
//                    }
//                }
            }
        };
//        AdapterView.OnItemClickListener local5 = new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
//                int i = SelectedLanguageScreen.this.getCheckedItemCount();
//                if (SelectedLanguageScreen.this.getListView().getCount() == i) {
//                    SelectedLanguageScreen.this.chk.setChecked(true);
//                    String str3 = (String) SelectedLanguageScreen.this.Id.get(paramAnonymousInt);
//                    SelectedLanguageScreen.this.langcount.add(str3);
//                    HashSet localHashSet2 = new HashSet();
//                    localHashSet2.addAll(SelectedLanguageScreen.this.langcount);
//                    SelectedLanguageScreen.this.langcount.clear();
//                    SelectedLanguageScreen.this.langcount.addAll(localHashSet2);
//                    return;
//                }
//                SelectedLanguageScreen.this.chk.setChecked(false);
//                if (((CheckedTextView) paramAnonymousView).isChecked()) {
//                    String str2 = (String) SelectedLanguageScreen.this.Id.get(paramAnonymousInt);
//                    SelectedLanguageScreen.this.langcount.add(str2);
//                    HashSet localHashSet1 = new HashSet();
//                    localHashSet1.addAll(SelectedLanguageScreen.this.langcount);
//                    SelectedLanguageScreen.this.langcount.clear();
//                    SelectedLanguageScreen.this.langcount.addAll(localHashSet1);
//                    return;
//                }
//                String str1 = (String) SelectedLanguageScreen.this.Id.get(paramAnonymousInt);
//                SelectedLanguageScreen.this.langcount.remove(str1);
//            }
//        };
        this.chk.setOnClickListener(local4);
        //this.l1.setOnItemClickListener(local5);
    }

}
