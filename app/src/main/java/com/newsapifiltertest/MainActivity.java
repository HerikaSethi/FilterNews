package com.newsapifiltertest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.newsapifiltertest.adapter.CategoryRcvAdapter;
import com.newsapifiltertest.adapter.NewsRcvAdapter;
import com.newsapifiltertest.modals.Articles;
import com.newsapifiltertest.modals.CategoryModal;
import com.newsapifiltertest.modals.NewsModal;
import com.newsapifiltertest.network.RetroitApiInterface;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryRcvAdapter.CategoryClickInterface{

    TextView apptitle;
    private RecyclerView newsrcv, categoryrcv;
    private ProgressBar loadingprogress;

    private CategoryRcvAdapter categoryRcvAdapter;
    private NewsRcvAdapter newsRcvAdapter;

    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryModal> categoryModalArrayList;

    boolean[] selectedCategory;
    ArrayList<Integer> categoryList = new ArrayList<>();
    String[] categoryArray = {"All", "Technology", "Science", "Sports", "Entertainment", "Health"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedCategory = new boolean[categoryArray.length];

        apptitle = findViewById(R.id.app_title);
        apptitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        MainActivity.this
                );
                builder.setTitle("Select Category");
                builder.setCancelable(false);

                builder.setMultiChoiceItems(categoryArray, selectedCategory, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b){
                            //---if checkbox selected----
                            categoryList.add(i);
                            Collections.sort(categoryList);
                        }else{
                            categoryList.remove(i);
                        }
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int x=0;x<categoryList.size();x++)
                        {
                            stringBuilder.append(categoryArray[categoryList.get(x)]);
                            if(x!=categoryList.size()-1){
                                //--adding comma-----
                                stringBuilder.append(", ");
                            }
                        }
                        apptitle.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for(int y =0;y<selectedCategory.length;y++)
                        {
                            //----removing all selection----
                            selectedCategory[y] = false;
                            categoryList.clear();
                            apptitle.setText("");
                        }
                    }
                });
                builder.show();

            }
        });

//        selectedCategory = new boolean[categoryArray.length];
//        apptitlecategory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//
//                builder.setTitle("Select category");
//                builder.setCancelable(false);
//                builder.setMultiChoiceItems(categoryArray, selectedCategory, new DialogInterface.OnMultiChoiceClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
//                        if (b){
//                            //-----if checkbox selected then adding position in categoryList-----
//                            categoryList.add(i);
//                            Collections.sort(categoryList);
//                        }else {
//                            //-------on unselecting checkbox removing position from daylist-------------
//                            categoryList.remove(i);
//                        }
//                    }
//                });
////                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialogInterface, int i) {
////                        StringBuilder stringBuilder = new StringBuilder();
////
////                        for (int x=0;x<categoryList.size();x++)
////                        {
////                            stringBuilder.append(categoryArray[categoryList.get(x)]);
////                            if (x!=categoryList.size()-1){
////                                    //--to add comma when condition satisfy-----
////                                stringBuilder.append(", ");
////                            }
////                        }
////                        apptitlecategory.setText(stringBuilder.toString());
////                    }
////                });
////
////                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialogInterface, int i) {
////                        dialogInterface.dismiss();
////                    }
////                });
////                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialogInterface, int i) {
////                        for (int j=0;j<selectedCategory.length;j++){
////                            //-----removing all selection-------
////                            selectedCategory[j] = false;
////                            //clearing category list
////                            categoryList.clear();
////                            //clearing textview
////                            apptitlecategory.setText("");
////                        }
////                    }
////                });
//                builder.show();
//            }
//        });

       // apptitle = findViewById(R.id.app_title);

        newsrcv = findViewById(R.id.rcvNews);
        categoryrcv = findViewById(R.id.categories_rcv);
        loadingprogress = findViewById(R.id.progressbar);

        //----------initializing array list------------------------
        articlesArrayList = new ArrayList<>();
        categoryModalArrayList = new ArrayList<>();

        newsRcvAdapter = new NewsRcvAdapter(articlesArrayList, this);
        categoryRcvAdapter = new CategoryRcvAdapter(categoryModalArrayList,this,this::onCategoryClick);

        //--------setting layout and adapter-------------
        newsrcv.setLayoutManager(new LinearLayoutManager(this));
        newsrcv.setAdapter(newsRcvAdapter);
        categoryrcv.setAdapter(categoryRcvAdapter);
        getCategories();
        showNews("All");
        newsRcvAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getCategories(){
        categoryModalArrayList.add(new CategoryModal("All","https://www.simplycoatings.co.uk/ekmps/shops/simplycoatings2/images/yester-30-matt-powder-coating-20kg-box--1761-p.jpg"));
        categoryModalArrayList.add(new CategoryModal("Technology","https://www.simplycoatings.co.uk/ekmps/shops/simplycoatings2/images/yester-30-matt-powder-coating-20kg-box--1761-p.jpg"));
        categoryModalArrayList.add(new CategoryModal("Science","https://www.simplycoatings.co.uk/ekmps/shops/simplycoatings2/images/yester-30-matt-powder-coating-20kg-box--1761-p.jpg"));
        categoryModalArrayList.add(new CategoryModal("Sports","https://www.simplycoatings.co.uk/ekmps/shops/simplycoatings2/images/yester-30-matt-powder-coating-20kg-box--1761-p.jpg"));
        categoryModalArrayList.add(new CategoryModal("Business","https://www.simplycoatings.co.uk/ekmps/shops/simplycoatings2/images/yester-30-matt-powder-coating-20kg-box--1761-p.jpg"));
        categoryModalArrayList.add(new CategoryModal("Entertainment","https://www.simplycoatings.co.uk/ekmps/shops/simplycoatings2/images/yester-30-matt-powder-coating-20kg-box--1761-p.jpg"));
        categoryModalArrayList.add(new CategoryModal("Health","https://www.simplycoatings.co.uk/ekmps/shops/simplycoatings2/images/yester-30-matt-powder-coating-20kg-box--1761-p.jpg"));

        categoryRcvAdapter.notifyDataSetChanged();
    }

    private void showNews(String category){
        loadingprogress.setVisibility(View.VISIBLE);
        articlesArrayList.clear(); //clearing existing data if any

        String categoryURL = "https://newsapi.org/v2/top-headlines?country=in&category="+category+"&apikey=2ed5101c94ca43c18b17c2db6e15b5b5";
        String baseURL = "https://newsapi.org";
        String url = "https://newsapi.org/v2/top-headlines?country=in&excludeDomains=sackoverflow.com&sortBy=publishedAt&language=en&apikey=2ed5101c94ca43c18b17c2db6e15b5b5";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetroitApiInterface retroitApiInterface = retrofit.create(RetroitApiInterface.class);
        Call<NewsModal> call;
        if(category.equals("All")){
            call = retroitApiInterface.getAllNews(url);
        }else{
            call = retroitApiInterface.getNewsByCategory(categoryURL);
        }

        call.enqueue(new Callback<NewsModal>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<NewsModal> call, Response<NewsModal> response) {
                //----storing response in newsmodal class------
                NewsModal newsModal = response.body();
                loadingprogress.setVisibility(View.GONE);
                assert newsModal != null;  //
                ArrayList<Articles> articles = newsModal.getArticles();
                for(int i=0;i<articles.size();i++)
                {
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(),articles.get(i).getDescription(),articles.get(i).getUrlToImage(),articles.get(i).getUrl(),articles.get(i).getContent()));
                }
                newsRcvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewsModal> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed o fetch news", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryModalArrayList.get(position).getCategory();
        showNews(category);
    }


}