package com.idogfooding.backbone.network;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.exception.HttpException;
import com.lzy.okgo.exception.StorageException;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;

/**
 * base network Callback
 * 支持显示加载中对话框,解析JSON,处理错误
 *
 * @author Charles
 */
public abstract class BaseCallback<T> extends AbsCallback<T> {

    protected Context context;
    protected Fragment fragment;

    // loading progress
    private MaterialDialog progressView;

    /**
     * 初始化加载中对话框
     *
     * @param context
     */
    private void initProcessDialog(Context context, String loadingContent) {
        progressView = new MaterialDialog.Builder(context)
                .content(TextUtils.isEmpty(loadingContent) ? context.getString(com.idogfooding.backbone.R.string.msg_loading) : loadingContent)
                .progress(true, 0)
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .build();
    }

    public BaseCallback(Context context) {
        this(context, false);
    }

    public BaseCallback(Fragment fragment) {
        this(fragment, false);
    }

    public BaseCallback(Context context, boolean showDialog) {
        this(context, showDialog, null);
    }

    public BaseCallback(Fragment fragment, boolean showDialog) {
        this(fragment, showDialog, null);
    }

    public BaseCallback(Context context, boolean showDialog, String loadingContent) {
        super();
        this.context = context;
        if (showDialog) {
            initProcessDialog(context, loadingContent);
        }
    }

    public BaseCallback(Fragment fragment, boolean showDialog, String loadingContent) {
        this(fragment.getContext(), showDialog, loadingContent);
        this.fragment = fragment;
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        if (progressView != null && !progressView.isShowing()) {
            progressView.show();
        }
    }

    @Override
    public void onFinish() {
        if (progressView != null && progressView.isShowing()) {
            progressView.dismiss();
        }
    }

    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {
        ResponseBody body = response.body();
        if (null == body)
            return null;

        JsonReader jsonReader = new JsonReader(body.charStream());
        // genType是Callback类,Callback必须带泛型,肯定是ParameterizedType
        Type genType = getClass().getGenericSuperclass();
        // type是Callback的泛型类,比如HttpResult,HttpResult也可能还带有第二层泛型
        Type type = ((ParameterizedType) genType).getActualTypeArguments()[0];
        // Callback的泛型类的真实类型
        Type rawType;
        // 第二层数据的泛型真实类型
        Type typeArgument = Void.class;
        if (type instanceof ParameterizedType) {
            // Callback泛型(比如HttpResult),带有第二层泛型
            // Callback泛型数据的真实类型
            // 要获取这个rawType是因为typ带泛型以后,无法直接通过instanceof或者==和HttpResult类做判断
            rawType = ((ParameterizedType) type).getRawType();
            typeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
        } else {
            // Callback泛型(比如HttpResult),没有再继续指定泛型
            rawType = type;
        }

        if (rawType == getResultType()) {
            return convertResultType(jsonReader, typeArgument, type);
        } else {
            // Callback泛型非HttpResult
            // Gson解析接口返回数据流
            T data = getGson().fromJson(jsonReader, type);
            response.close();
            return data;
        }
    }

    /**
     * 自定义json builder
     * 比如根据实际的时间格式解析Date
     */
    protected Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        // 如果返回是long格式,则如下注册type
        // builder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()));
        return builder.create();
    }

    /**
     * 返回解析的返回实体包
     */
    protected Type getResultType() {
        return HttpResult.class;
    }

    /**
     * 解析返回实体包
     */
    @SuppressWarnings("unchecked")
    protected T convertResultType(JsonReader jsonReader, Type typeArgument, Type type) throws Throwable {
        // Callback泛型是HttpResult,直接解析成HttpResult
        HttpResult httpResult = getGson().fromJson(jsonReader, Void.class == typeArgument ? HttpResult.class : type);
        if (null == httpResult) {
            throw new IllegalArgumentException("数据解析错误");
        } else {
            if (httpResult.isSuccess()) {
                return (T) httpResult;
            } else {
                throw new ApiException(httpResult.getCode(), httpResult.getMsg());
            }
        }
    }

    @Override
    public void onSuccess(Response<T> response) {
        if (response == null) {
            response = new Response<>();
            response.setException(new IllegalStateException("网络请求返回数据失败"));
            onError(response);
        } else if (response.body() == null) {
            response.setException(new IllegalStateException("网络请求返回数据未空"));
            onError(response);
        } else {
            onSuccessData(response, response.body());
        }
    }

    protected void onSuccessData(Response<T> response, T data) {

    }

    /**
     * 网络请求发生错误,包括系统错误、接口返回错误等
     *
     * @param response
     */
    @Override
    public void onError(Response<T> response) {
        Throwable exception = response.getException();

        if (null != exception)
            exception.printStackTrace();

        if (null == exception) {
            response.setException(new IllegalStateException("网络请求发生未知错误"));
            onSysError(response);
        } else if (exception instanceof UnknownHostException || exception instanceof ConnectException) {
            response.setException(new IllegalStateException("网络连接失败,请检查网络链接!"));
            onSysError(response);
        } else if (exception instanceof SocketTimeoutException) {
            response.setException(new IllegalStateException("网络请求超时"));
            onSysError(response);
        } else if (exception instanceof HttpException) {
            response.setException(new IllegalStateException("服务器返回异常:" + exception.getMessage()));
            onSysError(response);
        } else if (exception instanceof StorageException) {
            response.setException(new IllegalStateException("存储读取异常,请检查存储是否存在并有权限"));
            onSysError(response);
        } else if (exception instanceof ApiException) {
            onApiError(response, (ApiException) exception);
        } else {
            response.setException(new IllegalStateException("网络请求未知错误"));
            onSysError(response);
        }
    }

    /**
     * 除服务器返回错误以外的系统错误
     *
     * @param response
     */
    protected void onSysError(Response<T> response) {
        ToastUtils.showShort(response.getException().getMessage());
    }

    /**
     * 服务器返回错误
     *
     * @param response
     * @param exception
     */
    protected void onApiError(Response<T> response, ApiException exception) {
        ToastUtils.showShort(exception.getMessage());
    }

}
