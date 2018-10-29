package com.idogfooding.backbone.network;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.idogfooding.backbone.R;
import com.kongzue.dialog.v2.WaitDialog;
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
 * 基础网络调用的回调
 * 支持显示加载中对话框,解析JSON,处理错误
 * see {https://github.com/jeasonlzy/okhttp-OkGo/wiki/Callback}
 *
 * @author Charles
 */
public abstract class BaseCallback<T> extends AbsCallback<T> {

    protected Context context;
    protected Fragment fragment;

    // 是否在完成调用后,dismiss加载框
    protected boolean dismissLoading = true;
    // 是否在启动时候，显示加载框
    protected boolean showLoading = true;

    public BaseCallback(Context context) {
        this(context, false);
    }

    public BaseCallback(Fragment fragment) {
        this(fragment, false);
    }

    public BaseCallback(Context context, boolean showLoading) {
        this(context, showLoading, true, null);
    }

    public BaseCallback(Fragment fragment, boolean showLoading) {
        this(fragment, showLoading, true, null);
    }

    public BaseCallback(Fragment fragment, boolean showDialog, boolean dismissLoading, String loadingContent) {
        this(fragment.getContext(), showDialog, dismissLoading, loadingContent);
        this.fragment = fragment;
    }

    public BaseCallback(Context context, boolean showLoading, boolean dismissLoading, String loadingContent) {
        super();
        this.context = context;
        this.showLoading = showLoading;
        this.dismissLoading = dismissLoading;
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        if (showLoading) {
            WaitDialog.show(context, context.getString(R.string.msg_loading));
        }
    }

    @Override
    public void onFinish() {
        if (dismissLoading) {
            WaitDialog.dismiss();
        }
    }

    @Override
    public T convertResponse(okhttp3.Response response) {
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
        builder.registerTypeAdapter(Boolean.class, new BooleanTypeAdapter()); // 注册boolean的
        //builder.registerTypeAdapter(Date.class, new DateTypeAdapter()); // 注册Date的
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
    protected T convertResultType(JsonReader jsonReader, Type typeArgument, Type type) {
        // Callback泛型是HttpResult,直接解析成HttpResult
        HttpResult httpResult = getGson().fromJson(jsonReader, Void.class == typeArgument ? HttpResult.class : type);
        if (null == httpResult) {
            throw new BoneException(BoneException.CODE_JSON_EXCEPTION);
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
            response.setException(new BoneException(BoneException.CODE_NO_RESPONSE_EXCEPTION));
            onError(response);
        } else if (response.body() == null) {
            response.setException(new BoneException(BoneException.CODE_NO_BODY_EXCEPTION));
            onError(response);
        } else {
            onSuccessData(response, response.body());
        }
    }

    /**
     * 当真的获取到数据，并且数据不为空
     *
     * @param response
     * @param data
     */
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
            onSysError(response, BoneException.CODE_NULL_EXCEPTION);
        } else if (exception instanceof UnknownHostException) {
            onSysError(response, BoneException.CODE_UNKNOWNHOST_EXCEPTION);
        } else if (exception instanceof ConnectException) {
            onSysError(response, BoneException.CODE_CONNECT_EXCEPTION);
        } else if (exception instanceof SocketTimeoutException) {
            onSysError(response, BoneException.CODE_SOCKETTIMEOUT_EXCEPTION);
        } else if (exception instanceof HttpException) {
            onSysError(response, BoneException.CODE_HTTP_EXCEPTION);
        } else if (exception instanceof StorageException) {
            onSysError(response, BoneException.CODE_STORAGE_EXCEPTION);
        } else if (exception instanceof JsonParseException) {
            onSysError(response, BoneException.CODE_JSON_EXCEPTION);
        } else if (exception instanceof ApiException) {
            onSysError(response, BoneException.CODE_API_EXCEPTION);
        } else if (exception instanceof BoneException) {
            onSysError(response, ((BoneException) exception).getCode());
        } else {
            onSysError(response, BoneException.CODE_OTHER_EXCEPTION);
        }
    }

    /**
     * 除服务器返回错误以外的系统错误
     *
     * @param response
     */
    protected void onSysError(Response<T> response, int errorCode) {
        if (errorCode == BoneException.CODE_API_EXCEPTION) {
            onApiError(response, (ApiException) response.getException());
        } else {
            ToastUtils.showShort(BoneException.getMsgByCode(errorCode));
        }
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
