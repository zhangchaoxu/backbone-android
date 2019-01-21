package com.idogfooding.backbone;

/**
 * RequestCode
 * 预定义常用RequestCode
 *
 * @author Charles
 */
public interface RequestCode {

    // 1-50 预留TAG
    int TAG1 = 1;
    int TAG2 = 2;
    int TAG3 = 3;
    int TAG4 = 4;
    int TAG5 = 5;
    int TAG6 = 6;
    int TAG7 = 7;
    int TAG8 = 8;
    int TAG9 = 9;
    int TAG10 = 10;

    // 50-60 权限
    int PERMISSION_SINGLE = 50;
    int PERMISSION_MULTI = 51;
    int PERMISSION_SETTINGS = 52;

    // 80-90 通用实体操作
    int ENTITY_ADD = 80; // 实体新增
    int ENTITY_VIEW = 81; // 实体查看
    int ENTITY_EDIT = 82; // 实体编辑
    int ENTITY_DEL = 83; // 实体删除
    int ENTITY_AUDIT = 84; // 实体审核
    int ENTITY_LIST = 85; // 实体列表
    int ENTITY_PAGE = 86; // 实体分页

    // 100-200 图片
    int PHOTO_PICKER = 100; // 图片选择
    int PHOTO_PICKER1 = 101;
    int PHOTO_PICKER2 = 102;
    int PHOTO_PICKER3 = 103;
    int PHOTO_PICKER4 = 104;
    int PHOTO_PICKER5 = 105;
    int PHOTO_PICKER6 = 106;
    int PHOTO_PICKER7 = 107;
    int PHOTO_PICKER8 = 108;
    int PHOTO_PICKER9 = 109;
    int PHOTO_PICKER10 = 110;

    int PHOTO_PREVIEW = 120; // 图片查看
    int PHOTO_PREVIEW1 = 121;
    int PHOTO_PREVIEW2 = 122;
    int PHOTO_PREVIEW3 = 123;
    int PHOTO_PREVIEW4 = 124;
    int PHOTO_PREVIEW5 = 125;
    int PHOTO_PREVIEW6 = 126;
    int PHOTO_PREVIEW7 = 127;
    int PHOTO_PREVIEW8 = 128;
    int PHOTO_PREVIEW9 = 129;
    int PHOTO_PREVIEW10 = 130;

    int PHOTO_MULT_PICKER = 140;
    int PHOTO_MULT_PICKER1 = 141;
    int PHOTO_MULT_PICKER2 = 142;
    int PHOTO_MULT_PICKER3 = 143;
    int PHOTO_MULT_PICKER4 = 144;
    int PHOTO_MULT_PICKER5 = 145;
    int PHOTO_MULT_PICKER6 = 146;
    int PHOTO_MULT_PICKER7 = 147;
    int PHOTO_MULT_PICKER8 = 148;
    int PHOTO_MULT_PICKER9 = 149;
    int PHOTO_MULT_PICKER10 = 150;

    int PHOTO_MULT_PREVIEW = 160;
    int PHOTO_MULT_PREVIEW1 = 161;
    int PHOTO_MULT_PREVIEW2 = 162;
    int PHOTO_MULT_PREVIEW3 = 163;
    int PHOTO_MULT_PREVIEW4 = 164;
    int PHOTO_MULT_PREVIEW5 = 165;
    int PHOTO_MULT_PREVIEW6 = 166;
    int PHOTO_MULT_PREVIEW7 = 167;
    int PHOTO_MULT_PREVIEW8 = 168;
    int PHOTO_MULT_PREVIEW9 = 169;
    int PHOTO_MULT_PREVIEW10 = 170;

    int PHOTO_CROP_PICKER = 180;
    int PHOTO_CROP_PREVIEW = 181;

    // 从相机获取
    int PHOTO_CAMERA = 182;
    // 从相册获取
    int PHOTO_ALBUM = 183;

    // 200-300 位置
    int LOCATION_CHOOSE = 200; // 位置选择
    int PROVINCE_CHOOSE = 201; // 省选择
    int CITY_CHOOSE = 202; // 市选择
    int AREA_CHOOSE = 203; // 区选择
    int ADDRESS_CHOOSE = 204; // 地址选择

    // 300-400 form common user
    int USER_LOGIN = 300; // 用户登录
    int USER_EDIT = 301; // 用户编辑
    int USER_VIEW = 302; // 用户查看
    int USER_DEL = 303; // 用户删除

}
