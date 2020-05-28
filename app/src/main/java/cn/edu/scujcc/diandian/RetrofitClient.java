package cn.edu.scujcc.diandian;

import com.squareup.moshi.Moshi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitClient {
    private  static Retrofit INSTANCE =null;
    public  static Retrofit getInstance(){
        if (INSTANCE == null){
            Moshi moshi = new Moshi.Builder()
                    .add(new MyDateAadpter())
                    .build();
            //准备拦截器
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor())
                    .build();

            INSTANCE = new Retrofit.Builder()
                    .baseUrl("http://47.115.82.64:8080")
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .callFactory(client)  //注入自定义的okhttp
                    .build();

        }
            return INSTANCE;
    }
}
