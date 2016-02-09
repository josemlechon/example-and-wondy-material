package com.example.wondy.io;

import android.app.Application;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Converter;
import retrofit.JacksonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by jose m lechon on 08/02/16.
 *
 * @version 0.1.0
 * @since 1
 */
@Module
public class ApiModule {


    String mBaseUrl;

    public ApiModule(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(Cache cache) {

        OkHttpClient httpClient = new OkHttpClient();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.interceptors().clear();
        httpClient.interceptors().add(logging);
        httpClient.setCache(cache);
        return httpClient;

    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(application.getCacheDir(), cacheSize);
    }


    @Provides
    @Singleton
    ObjectMapper provideObjectMapper() {

        return  new ObjectMapper();
    }


        @Provides
    @Singleton
    Converter.Factory provideFactory(ObjectMapper mapper) {


        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);


        mapper.setAnnotationIntrospector(new IgnoreInheritedIntrospector());

        return JacksonConverterFactory.create(mapper);
    }


    @Provides
    @Singleton
    public ApiService apiService(OkHttpClient okHttpClient, Converter.Factory factoryMapper) {


        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(mBaseUrl)
                .addConverterFactory(factoryMapper).build()
                .create(ApiService.class);
    }

    private static class IgnoreInheritedIntrospector extends JacksonAnnotationIntrospector {
        @Override
        public boolean hasIgnoreMarker(final AnnotatedMember m) {
            return super.hasIgnoreMarker(m);
        }
    }


}
