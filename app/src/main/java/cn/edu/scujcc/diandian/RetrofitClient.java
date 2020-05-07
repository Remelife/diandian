package cn.edu.scujcc.diandian;

import com.squareup.moshi.Moshi;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitClient {
    private  static Retrofit INSTANCE =null;
    public  static Retrofit getInstance(){
        if (INSTANCE == null){
            Moshi moshi = new Moshi.Builder()
                    .add(new MyDateAadpter())
                    .build();
            INSTANCE = new Retrofit.Builder()
                    .baseUrl("http://47.115.82.64:8080")
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build();
        }
            return INSTANCE;
    }
}
