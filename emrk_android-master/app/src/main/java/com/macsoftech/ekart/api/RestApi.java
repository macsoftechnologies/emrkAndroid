package com.macsoftech.ekart.api;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.macsoftech.ekart.BuildConfig;
import com.macsoftech.ekart.app.BaseApp;
import com.macsoftech.ekart.model.LocationResponseRoot;
import com.macsoftech.ekart.model.LoginRootResponse;
import com.macsoftech.ekart.model.NotificationsRoot;
import com.macsoftech.ekart.model.ProductDetailsRoot;
import com.macsoftech.ekart.model.language.LanguageRootResponse;
import com.macsoftech.ekart.model.register.RegistrationRootResponse;
import com.macsoftech.ekart.model.search.GetUserResponseRoot;
import com.macsoftech.ekart.model.search.ListOfVendorsResponse;
import com.macsoftech.ekart.model.search.ListOfVendorsResponse2;
import com.macsoftech.ekart.model.search.SearchRootResponse;
import com.macsoftech.ekart.model.sizes.SizeModelRootResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Ramesh on 11/06/21.
 */

public class RestApi {

    private static final String CACHE_CONTROL = "Cache-Control";


    public static String BASE_URL = "http://43.204.146.144:4000/";
    private static RestApi mRestApi;


    public static synchronized RestApi getInstance() {
        if (mRestApi == null) {
            mRestApi = new RestApi();
        }
        return mRestApi;
    }

    public MyService getService() {
        return buildAdapter(BASE_URL, buildOkHttpClient(), MyService.class);
    }


    private OkHttpClient buildOkHttpClient() {
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient defaultHttpClient = builder
                .addInterceptor(interceptor)
                .authenticator((route, response) -> {
                    if (BaseApp.getInstance() != null) {
                        LocalBroadcastManager.getInstance(BaseApp.getInstance())
                                .sendBroadcast(new Intent("LOGOUT"));
                    }
                    return null;
                })
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
//                .protocols(CollectionUtils.listOf(Protocol.HTTP_1_1))
                .protocols(Arrays.asList(Protocol.HTTP_1_1))
                .build();
        return defaultHttpClient;
    }

    <T> T buildAdapter(String baseUrl, OkHttpClient defaultHttpClient, Class<T> clazz) {
        //Build Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .client(defaultHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(clazz);
    }


    public interface MyService {

        @GET
        Call<ResponseBody> callDynamicUrl(@Url String url);

        //        {
//            "emailId":"gowthami@gmail.com",
//                "password":"gfdsdf"
//        }
        @GET("languages/getLang")
        Call<LanguageRootResponse> getLanguages();

        @GET("admin/getProductSize")
        Call<SizeModelRootResponse> getProductSizes();

        @GET("admin/getLocations")
        Call<LocationResponseRoot> getLocations();

        @GET("admin-product/getProductAndVendorById/{productId}")
        Call<ProductDetailsRoot> getProductAndVendorById(@Path("productId") String productId);


        @GET("admin-product/getVendorProductByLength/{min}/{max}")
        Call<ListOfVendorsResponse2> getVendorProductByLength(
                @Path("min") String min,
                @Path("max") String max
        );

        @POST("admin-product/getVendorProductByLocation")
        Call<ListOfVendorsResponse2> getVendorProductByLocation(@Body Map<String, String> body);

        @POST("users/updateUser")
        Call<ResponseBody> updateUser(@Body Map<String, Object> body);


        @Multipart
        @POST("users/updateUser")
        Call<RegistrationRootResponse> updateUser(
                @Part MultipartBody.Part paramImage1,
                @Part MultipartBody.Part paramImage2,
                @PartMap() Map<String, RequestBody> partMap
        );


        @GET("admin-product/getVendorProductsByLocationFilter")
        Call<SearchRootResponse> getVendorProductsByLocationFilter(
                @Query("state") String state,
                @Query("district") String district,
                @Query("mandal") String mandal,
                @Query("village") String village
        );

        @POST("admin-product/getVendorProductByQuantity")
        Call<ListOfVendorsResponse2> getVendorProductByQuantity(@Body Map<String, String> body);

        @GET("admin/getProductLength")
        Call<SizeModelRootResponse> getProductLength();

        @POST("users/login")
        Call<LoginRootResponse> login(@Body Map<String, String> body);

        @POST("users/verifyOtp")
        Call<LoginRootResponse> verifyOtp(@Body Map<String, String> body);


        @POST("admin-product/search")
        Call<SearchRootResponse> searchProducts(@Body Map<String, String> body);

        @POST("admin-product/getVendorProductBySize")
        Call<ListOfVendorsResponse2> getVendorProductBySize(@Body Map<String, String> body);


//        @POST("admin-product/getVendorProduct")
//        Call<ListOfVendorsResponse> getVendorProduct(@Body Map<String, String> body);

//        @GET("admin-product/getVendorProductsList")
//        Call<ListOfVendorsResponse> getVendorProduct(@Body Map<String, String> body);

        @GET("admin-product/getProductAndVendorById/{id}")
        Call<ListOfVendorsResponse> getVendorProduct(@Path("id") String productId);


        @Multipart
        @POST("users/register")
        Call<RegistrationRootResponse> register(
                @Part MultipartBody.Part paramImage1,
                @Part MultipartBody.Part paramImage2,
                @PartMap() Map<String, RequestBody> partMap
        );

        @Multipart
        @POST("users/createUnavailbleproduct")
        Call<ResponseBody> createUnavailbleproduct(
                @Part MultipartBody.Part paramImage1,
                @PartMap() Map<String, RequestBody> partMap
        );

        @Multipart
        @POST("users/createUnavailbleproduct")
        Call<ResponseBody> createUnavailbleproduct(
                @PartMap() Map<String, RequestBody> partMap
        );

        @POST("users/unavailLcreate")
        Call<ResponseBody> unavailLocationCreate(@Body Map<String, String> body);

        @POST("users/getUser")
        Call<GetUserResponseRoot> getUser(@Body Map<String, String> body);

        @POST("admin-product/getUserProducts")
        Call<SearchRootResponse> getUserProducts(@Body Map<String, String> body);


        @Multipart
        @POST("admin-product/addVendorProduct")
        Call<RegistrationRootResponse> addProduct(
                @PartMap() Map<String, RequestBody> partMap,
                @Part MultipartBody.Part... paramImage
        );

        @POST("users/report")
        Call<ResponseBody> addReport(@Body Map<String, String> body);

        //        @POST("notifications/createnotification")
        @POST("notifications/saveFCMToken")
        Call<ResponseBody> saveGCM(@Body Map<String, String> body);

        @POST("users/userFeedback")
        Call<ResponseBody> addUserFeedback(@Body Map<String, String> body);

        @POST("notifications/getPushNotificationByUserId")
        Call<NotificationsRoot> getPushNotificationByUserId(@Body Map<String, String> body);


    }

    //FILE UPLOAD BLOCK

    public static Map<String, RequestBody> prepareBodyPart(Map<String, String> map) {
        // add another part within the multipart request
        Map<String, RequestBody> partMap = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            partMap.put(entry.getKey(),
                    RequestBody.create(MediaType.parse("text/plain"),
                            entry.getValue()));

        }
        return partMap;

    }

    @Nullable
    public static MultipartBody.Part prepareFilePart(String partName, String filePath, UploadCallbacks callbacks) {
        try {
            ProgressRequestBody fileBody = new ProgressRequestBody(new File(filePath), callbacks);
            File file = new File(filePath);
            if (!file.exists()) {
                return null;
            }
            if (callbacks == null) {
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                return MultipartBody.Part.createFormData(partName, file.getName(), reqFile);
            } else {
                return MultipartBody.Part.createFormData(partName, file.getName(), fileBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface UploadCallbacks {
        void onProgressUpdate(int percentage);

        void onError();

        void onFinish();
    }

    public static class ProgressRequestBody extends RequestBody {
        private File mFile;
        private String mPath;
        private UploadCallbacks mListener;

        private static final int DEFAULT_BUFFER_SIZE = 2048;


        public ProgressRequestBody(final File file, final UploadCallbacks listener) {
            mFile = file;
            mListener = listener;
        }

        @Override
        public MediaType contentType() {
            // i want to upload only images
            return MediaType.parse("image/*");
        }

        @Override
        public long contentLength() throws IOException {
            return mFile.length();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            long fileLength = mFile.length();
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            FileInputStream in = new FileInputStream(mFile);
            long uploaded = 0;

            try {
                int read;
                Handler handler = new Handler(Looper.getMainLooper());
                while ((read = in.read(buffer)) != -1) {
                    uploaded += read;
                    sink.write(buffer, 0, read);
                    // update progress on UI thread
                    handler.post(new ProgressUpdater(uploaded, fileLength));
                }
            } finally {
                in.close();
            }
        }

        private class ProgressUpdater implements Runnable {
            private long mUploaded;
            private long mTotal;

            public ProgressUpdater(long uploaded, long total) {
                mUploaded = uploaded;
                mTotal = total;
            }

            @Override
            public void run() {
                mListener.onProgressUpdate((int) (100 * mUploaded / mTotal));
            }
        }
    }
    //

}
