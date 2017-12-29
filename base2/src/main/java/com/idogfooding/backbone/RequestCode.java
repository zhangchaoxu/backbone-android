package com.idogfooding.backbone;

/**
 * RequestCode
 *
 * @author Charles
 */
public interface RequestCode {

    // 1-100 for permission
    int PERMISSION_SINGLE = 0x0001;
    int PERMISSION_MULTI = 0x0002;
    int PERMISSION_SETTINGS = 0x0003;

    // 100-9999 form common user
    int USER_LOGIN = 0x0101; // 用户登录
    int USER_EDIT = 0x0102; // 用户编辑
    int USER_VIEW = 0x0103; // 用户查看
    int USER_DEL = 0x0104; // 用户删除
    int LOCATION_CHOOSE = 0x0105; // 位置选择
    int CITY_CHOOSE = 0x0106; // 城市选择
    int ENTITY_ADD = 0x0107; // 实体新增
    int ENTITY_VIEW = 0x0108; // 实体查看
    int ENTITY_EDIT = 0x0109; // 实体编辑
    int ENTITY_DEL = 0x0110; // 实体删除
    int ENTITY_AUDIT = 0x0111; // 实体审核
    int PHOTO_PICKER = 0x0112; // 图片选择
    int PHOTO_PREVIEW = 0x0113; // 图片查看
    int PHOTO_MULT_PICKER = 0x0114;
    int PHOTO_MULT_PREVIEW = 0x0115;
    int PHOTO_CROP_PICKER = 0x0116;
    int PHOTO_CROP_PREVIEW = 0x0117;
    int PHOTO_PICKER1 = 0x0118;
    int PHOTO_PREVIEW1 = 0x0119;
    int PHOTO_PICKER2 = 0x0120;
    int PHOTO_PREVIEW2 = 0x0121;
    int PHOTO_PICKER3 = 0x0122;
    int PHOTO_PREVIEW3 = 0x0123;
    int PHOTO_PICKER4 = 0x0124;
    int PHOTO_PREVIEW4 = 0x0125;
    int PHOTO_PICKER5 = 0x0126;
    int PHOTO_PREVIEW5 = 0x0127;
    int PHOTO_PICKER6 = 0x0128;
    int PHOTO_PREVIEW6 = 0x0129;
    int PHOTO_PICKER7 = 0x0130;
    int PHOTO_PREVIEW7 = 0x0131;
    int PHOTO_PICKER8 = 0x0132;
    int PHOTO_PREVIEW8 = 0x0133;
    int PHOTO_PICKER9 = 0x0133;
    int PHOTO_PREVIEW9 = 0x0134;
    int PHOTO_PICKER10 = 0x0135;
    int PHOTO_PREVIEW10 = 0x0136;
    int TAG1 = 0x0201;
    int TAG2 = 0x0202;
    int TAG3 = 0x0203;
    int TAG4 = 0x0204;
    int TAG5 = 0x0205;
    int TAG6 = 0x0206;
    int TAG7 = 0x0207;
    int TAG8 = 0x0208;
    int TAG9 = 0x0209;
    int TAG10 = 0x0210;

}
