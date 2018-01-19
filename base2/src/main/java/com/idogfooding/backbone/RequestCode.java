package com.idogfooding.backbone;

/**
 * RequestCode
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

    // 100-200 图片
    int PHOTO_PICKER = 100; // 图片选择
    int PHOTO_PREVIEW = 101; // 图片查看
    int PHOTO_MULT_PICKER = 102;
    int PHOTO_MULT_PREVIEW = 103;
    int PHOTO_CROP_PICKER = 104;
    int PHOTO_CROP_PREVIEW = 105;
    int PHOTO_PICKER1 = 111;
    int PHOTO_PICKER2 = 112;
    int PHOTO_PICKER3 = 113;
    int PHOTO_PICKER4 = 114;
    int PHOTO_PICKER5 = 115;
    int PHOTO_PICKER6 = 116;
    int PHOTO_PICKER7 = 117;
    int PHOTO_PICKER8 = 118;
    int PHOTO_PICKER9 = 119;
    int PHOTO_PICKER10 = 120;
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

    // 200-300 位置
    int LOCATION_CHOOSE = 200; // 位置选择
    int CITY_CHOOSE = 201; // 城市选择

    // 300-400 form common user
    int USER_LOGIN = 300; // 用户登录
    int USER_EDIT = 301; // 用户编辑
    int USER_VIEW = 302; // 用户查看
    int USER_DEL = 303; // 用户删除

}
