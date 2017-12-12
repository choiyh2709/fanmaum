package specup.fanmind.common.http;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import specup.fanmind.R;
import specup.fanmind.common.Util.Utils;


public class HttpRequest {

    /**
     * HTTP통신
     *
     * @param context
     * @param mAsyncTask AsyncTask
     * @param mParams    List<NameValuePair>
     * @param url        URL주소
     * @param execute    AsyncTask 이름
     * @param task       Ontask context
     */
    public static void setHttp(Context context, AsyncTask mAsyncTask, List<NameValuePair> mParams, String url, String execute, OnTask task) {

        try {
            Log.d("setHttp", url);
            if (mAsyncTask != null) {
                mAsyncTask.cancel(true);
            }
            mAsyncTask = new AsyncTask(context, url, mParams, true);
            mAsyncTask.setListenr(task);
            mAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,execute);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * http 통신
     *
     * @param context
     * @param url
     * @param mParams
     * @param task
     */
    public static void setHttp1(Context context, String url, List<NameValuePair> mParams, OnTask task) {
        try {
            AsyncTask mAsyncTask = new AsyncTask(context, url, mParams, true);
            mAsyncTask.setListenr(task);
            mAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * HTTP  get 통신
     *
     * @param context
     * @param mParams List<NameValuePair>
     * @param url     URL주소
     * @param task    Ontask context
     */
    public static void setHttpGet1(Context context, String url, List<NameValuePair> mParams, OnTask task) {
        try {
            AsyncTask mAsyncTask = new AsyncTask(context, url, mParams, false);
            mAsyncTask.setListenr(task);
            mAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * HTTP  get 통신
     *
     * @param context
     * @param mAsyncTask AsyncTask
     * @param mParams    List<NameValuePair>
     * @param url        URL주소
     * @param execute    AsyncTask 이름
     * @param task       Ontask context
     */
    public static void setHttpGet(Context context, AsyncTask mAsyncTask, List<NameValuePair> mParams, String url, String execute, OnTask task) {
        try {
            if (mAsyncTask != null) {
                mAsyncTask.cancel(true);
            }
            mAsyncTask = new AsyncTask(context, url, mParams, false);
            mAsyncTask.setListenr(task);
            mAsyncTask.execute(execute);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * HTTP통신
     *
     * @param context
     * @param mAsyncTask AsyncTask
     * @param mParams    List<NameValuePair>
     * @param url        URL주소
     * @param execute    AsyncTask 이름
     * @param task       Ontask context
     */
    public static void setHttpSetHeader(Context context, HashMap<String, String> header, AsyncTask mAsyncTask, List<NameValuePair> mParams, String url, String execute, OnTask task, boolean isPost) {
        try {
            if (mAsyncTask != null) {
                mAsyncTask.cancel(true);
            }
            mAsyncTask = new AsyncTask(context, header, url, mParams, isPost);
            mAsyncTask.setListenr(task);
            mAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,execute);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * post방식 hearder  수정 버전.
     */
    public static String postSetHeader(String url, List<NameValuePair> params, HashMap<String, String> header) {

        String response = null;
        HttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost();
        try {
            httpPost.setHeader("Accept", "application/json");
            httpPost.setURI(new URI(url));

            if (header.size() > 0) {
                try {
                    Set<String> setData = header.keySet();

                    Iterator<String> tempIterator = setData.iterator();
                    while (tempIterator.hasNext()) {
                        String temp = tempIterator.next();
                        httpPost.setHeader(temp, header.get(temp));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            if (params != null) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
                httpPost.setEntity(entity);
            }
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity resEntity = httpResponse.getEntity();
            response = Utils.getUnicodeDecode(EntityUtils.toString(resEntity));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    //post방식
    /*public static String post(String url, List<NameValuePair> params) {
        if (params == null) {
            throw new IllegalArgumentException();
        }
        String response = null;
        HttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost();
        try {
            httpPost.setHeader("Accept", "application/json");
            httpPost.setURI(new URI(url));
            if (params != null) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
                httpPost.setEntity(entity);
            }

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity resEntity = httpResponse.getEntity();
            response = Utils.getUnicodeDecode(EntityUtils.toString(resEntity));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }*/

    public static String post(String paramString, List<NameValuePair> paramList)
    {
        if (paramList == null) {
            throw new IllegalArgumentException();
        }
        HttpClient localHttpClient = getHttpClient();
        HttpPost localHttpPost = new HttpPost();
        try
        {
            localHttpPost.setHeader("Accept", "application/json");
            localHttpPost.setURI(new URI(paramString));
            if (paramList != null) {
                localHttpPost.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            }
            paramString = Utils.getUnicodeDecode(EntityUtils.toString(localHttpClient.execute(localHttpPost).getEntity()));
            return paramString;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static String postJson(String url, String srl, String mode) {
        String response = null;
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(url);
        try {
            post.setHeader("Content-type", "application/json");
            post.setHeader("Accept", "application/json");
            JSONObject obj = new JSONObject();
            obj.put("reply_srl", srl);
            obj.put("mode", mode);
            post.setEntity(new StringEntity(obj.toString(), "UTF-8"));
            HttpResponse httpResponse = client.execute(post);
            HttpEntity resEntity = httpResponse.getEntity();
            response = EntityUtils.toString(resEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    //get방식
    public static String get(String url, List<NameValuePair> params) {

        String response = null;

        HttpClient client = getHttpClient();
        try {
            if (params != null) {
                url += "?";
                for (int i = 0; i < params.size(); i++) {

                    if (i != 0) url += "&";
                    url += params.get(i).getName() + "=" + params.get(i).getValue();
                }
            }
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "application/json");
            HttpResponse httpResponse = client.execute(httpGet);
            HttpEntity resEntity = httpResponse.getEntity();
            if (resEntity != null) {
                response = EntityUtils.toString(resEntity);
                response = Utils.getUnicodeDecode(response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * SSL 통신(HTTPS)
     *
     * @return
     */
    public static HttpClient getHttpClient() {
        try {
            String certificateString = "-----BEGIN CERTIFICATE-----\n" +
                    "MIIDdzCCAl+gAwIBAgIJAMapd+KIAR0MMA0GCSqGSIb3DBQUAMFIxCzAJBgNV\n" +
                    "BAYTAktSMRQwEgYDVQQIDAtHeWVvbmdnaS1kbzEUMB1UEBwwLU2VvbmduYW0t\n" +
                    "c2kxFzAVBgNVBAoMDlRoZWV5ZSBDb21wYW55MB4E2MDIyMDA5MjEyMVoXDTE3\n" +
                    "MDIxOTA5MjEyMVowUjELMAkGA1UEBhMCS1IxSBgNVBAgMC0d5ZW9uZ2dpLWRv\n" +
                    "MRQwEgYDVQQHDAtTZW9uZ25hbS1zaTEXMA1UECgwOVGhlZXllIENvbXBhbnkw\n" +
                    "ggEiMA0GCSqGSIb3DQEBAQUAA4IBDwgEKAoIBAQDbrzdE+u+cIMnGJRZWlOZ+\n" +
                    "8fBil6pVPQntbu3nz92omfiTAeGjb25gkwkLamsM0QISnXSmI+gl3iJmuCyFw\n" +
                    "r3F+GiAUXS7e87vJoOKclbGVIQHPtEsbx+IjK7C2MJgoToNczedFzhr1xI9NV\n" +
                    "ZpFMKV+YONM7qCsKKMjVrj4TE7DCZUaWu8QlPYC6p8kR5ZM6DNtxoE2QOWpiB\n" +
                    "RwRwM6k88KfUVXysuvj3LCRVOqN3GrpCGF8GjuMohrI0zcK2XHvZ6D8GaFUEA\n" +
                    "TLG9QcfD1N/Jo1tHpdWe3kQIYjtGBvjZ3VfUmqt9PFsqjK5sx6Kppd9yPreQp\n" +
                    "AgMBAAGjUDBOGA1UdDgQWBBTjlXdy+BlcQGHfVdF9/8QkW1ciTTAfBgNVHSME\n" +
                    "GDAWgBTjl+BlcQGHfVdF9/8QkW1ciTTAMBgNVHRMEBTADAQH/MA0GCSqGSIb3\n" +
                    "DQEBBQ4IBAQCpWmUEr5p4CdWSGWHSopcNGPgJIODJ4K6Ir/IFJRb5tyzYY02R\n" +
                    "mfUtyP39M8GnnEz5QqYNobCNIVV3cSgAMKeUTAbBQDbE+F04LzR6DRXtdhjp7\n" +
                    "VtRG6McnecEi4D2Zq2ZGdFuAncfzhvdjybgkhkn1TZnbbtsiYqazKsNhdBvzR\n" +
                    "mvEpRkC7eqpAw36A9zHyjvP9tJW0mVJjlUtevARDkLyX+VpMtKOUWHYikLXgK\n" +
                    "UfRm9rtQMIpCC9n0qTMvDkxuBJakdSI7YRCpPYrsv0IEP23UN3Y32j1lthVIU\n" +
                    "9S5KyRYKhDzJXadgO3dNI9bFL0H2SZ6mXqYL\n" +
                    "-----END CERTIFICATE-----";

            ByteArrayInputStream derInputStream = new ByteArrayInputStream(certificateString.getBytes());
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certificateFactory.generateCertificate(derInputStream);
            String alias = cert.getSubjectX500Principal().getName();

            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            trustStore.setCertificateEntry(alias,cert);

            SSLSocketFactory sf = new SFSSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
            HttpConnectionParams.setSoTimeout(params, 20 * 1000);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }


    public static String upLoad(String url, MultipartEntity reqEntity) {
        String Result = null;
        try {
            Log.d("siba - url", url);
            //			Image.getDrawingCache();
            //			Bitmap bm  = Image.getDrawingCache();
            //			ByteArrayOutputStream bos = new ByteArrayOutputStream();
            //			bm.compress(CompressFormat.JPEG, 100, bos);
            //			byte[] data = bos.toByteArray();
            //			ByteArrayBody bab = new ByteArrayBody(data, idx);
            HttpClient client = getHttpClient();
            HttpPost post = new HttpPost(url);
            //			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            post.setEntity((HttpEntity) reqEntity);

            HttpResponse response = client.execute(post);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                Result = EntityUtils.toString(resEntity);
                Log.e("RESPONSE", Result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result;
    }

    /**
     * 둥근 이미지 로더
     *
     * @param url
     * @param imageView
     */
    public static void getNetworkCircleImage(Context context,String url, ImageView imageView) {
        url = url.replaceAll("null", "");
        if( url.length() == 0 ) return ;
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate().bitmapTransform(new CropCircleTransformation(context)).placeholder(R.drawable.profile_basic01).into(imageView);
    }

    public static void frescoCircleImage(Context mContext,Uri uri, SimpleDraweeView simpleDraweeView){
        int overlayColor = mContext.getResources().getColor(R.color.white);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setBorder(overlayColor, 1.0f);
        roundingParams.setRoundAsCircle(true);
        GenericDraweeHierarchy hierarchy = simpleDraweeView.getHierarchy();
        hierarchy.setPlaceholderImage(R.drawable.profile_basic01);
        simpleDraweeView.getHierarchy().setRoundingParams(roundingParams);

        int width = 120, height = 120;

        com.facebook.imagepipeline.request.ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).setResizeOptions(new ResizeOptions(width,height)).setProgressiveRenderingEnabled(false).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder().setOldController(simpleDraweeView.getController()).setImageRequest(request).setAutoPlayAnimations(false).build();
        simpleDraweeView.setController(controller);
    }

    public static void frescoToproundImage(Uri uri, SimpleDraweeView simpleDraweeView){
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);

        int width = 600, height = 300;
        com.facebook.imagepipeline.request.ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).setResizeOptions(new ResizeOptions(width,height)).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder().setOldController(simpleDraweeView.getController()).setImageRequest(request).build();
        simpleDraweeView.setController(controller);
    }

    public static void frescoImage(Uri uri, SimpleDraweeView simpleDraweeView){

        int width = 400, height = 200;
        com.facebook.imagepipeline.request.ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).setResizeOptions(new ResizeOptions(width,height)).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder().setOldController(simpleDraweeView.getController()).setImageRequest(request).build();
        simpleDraweeView.setController(controller);
    }




   /* *//**
     * top round 이미지 로더
     *
     * @param url
     * @param imageView
     *//*
    public static void getNetworkImageTopRound(Context context, String url, ImageView imageView) {
        url = url.replaceAll("null", "");
        if( url.length() == 0 ) return ;
        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.image_loading)
                .showImageOnLoading(R.drawable.image_loading)
                .showImageForEmptyUri(R.drawable.image_loading)
                .resetViewBeforeLoading(false)
                .cacheOnDisk(true)
                .displayer(new TopRoundBitmapDisplayer(context, imageView))
                .build();
        ImageLoader.getInstance().displayImage(url, imageView, imageOptions);
    }
*/
    /**
     * top round glide 라이브러리 이미지 로더
     *
     * @param url
     * @param imageView
     */
    public static void glideImage(Context context,String url,ImageView imageView){
        url = url.replaceAll("null", "");
        if( url.length() == 0 ) return ;
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate().into(imageView);
    }

/*    *//**
     * 이미지 로더
     *
     * @param url
     * @param imageView
     *//*
    public static void getNetworkImage(String url, ImageView imageView) {
        url = url.replaceAll("null", "");
        if( url.length() == 0 ) return ;
        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder().showImageOnFail(R.drawable.image_loading)
                .showImageOnLoading(R.drawable.image_loading)
                .showImageForEmptyUri(R.drawable.image_loading)
                .resetViewBeforeLoading(false)
                .cacheOnDisk(true)
                .build();
        ImageLoader.getInstance().displayImage(url, imageView, imageOptions);
    }*/
}
