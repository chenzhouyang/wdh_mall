package com.yskj.wdh.ui;

/**
 * Created by liuchaoya on 2016/11/7.
 * For yskj
 * Project Name : LSK
 */


import com.yskj.wdh.url.Ips;

/**统一约定*/
public interface MealCategory {
    /**从何处跳转*/
    String WHERE_FROM = "where_from";
    /**从品类选择跳回*/
    int CATEGORY_SELECT = 1;
    /**从套餐内容跳回*/
    int SET_MEAL = 2;
    /**管理食品品类选择*/
    String CATEGORY_NAME = "category_name";
    String CATEGORY_ID = "category_id";
    int SNACK = 1;
    int SHORT_ORDER = 2;
    int DEFAULT = -101;
    int DEFAULTERROR = -1001;

    /**跳到品类选择请求码*/
    int TO_CATEGORY_SELECT = 1;
    int TO_SET_MEAL_SELECT = 2;
    int TO_NOTICE_SELECT = 3;
    int TO_PHOTO = 4;
    int TO_CAMERA = 5;
    int TO_TAILOE = 6;
    /**新增-内容*/
    int NEW_ADD = 101;
    int EDIT = 102;

    /**购买须知*/
    int on = 1;
    int off = 0;

    String[] arrayvalidity = {"6","7","8","9","10","11","12"};

    String[] arrayimage = {"从相册选择","拍一张照片"};

    int CUT_PHOTO = 91;
    int CUT_CAMERA = 92;
    int FROM_PHOTO = 101;
    int FROM_CAMERA = 102;

    /**模拟数据显示*/
    int[] id = new int[]{};

    /**项目食品套餐条目查询返回的状态*/
    int text = 0;   //草稿
    int uped = 2;     //已上线
    int downed = 4;   //已下线
    int uping = 1;  //申请上线，待审批
    int downing = 3;//申请下线，待审批
    int abandoned = 5;//禁用

    /**上下线文字*/
    String SHOW_UP = "上架";
    String SHOW_DOWN = "下架";

    int noSelect = 100;
    int Selected = 101;

    String noSelects = "未选择";
    String Selecteds = "已选择";

    /**商品编辑提醒*/
    String tip1 = "请上传套餐图片";
    String tip2 = "请选择套餐品类";
    String tip3 = "请填写套餐名称";
    String tip4 = "请选择套餐内容";
    String tip5 = "请填写团购价";
    String tip6 = "请填写赠送红包数量";
    String tip7 = "赠送红包金额不能大于团购价哦";
    String tip8 = "请填写套餐描述";
    /**发送红包数据提醒*/
    String tip00 = "";
    String tip01 = "亲！至少要上传一张图片哦";
    String tip02 = "请填写您的广告语";
    String tip03 = "请填写红包数量";
    String tip031 = "红包数量不能小于0";
    String tip04 = "请填写红包总金额";
    String tip041 = "红包总金额不能小于0";
    String tip042 = "您的余额不足，请充值后再试";
    String tip05 = "您的红包投放范围太小啦";
    String tip06 = "定位失败啦，手动点取投放位置吧";
    String tip07 = "网络太差啦，换个姿势重新点取吧";
    String tip08 = "平均每个红包金额不能小于0.1元哦";
    /**接口-测试*/
    String URL= Ips.API_URL;
    String PM_LIST_QUERY = URL+"api/v1/life/shop/item/records";     //项目管理列表查询
    int ALL = 11;       //查询全部（包括草稿）
    int ON_LINED = 2;  //查询已上架
    int OUT_LINED = 4; //查询已下架
    int ABANDONED = 5; //查询已废弃
    int REFRESH_VIEW = 101; //刷新界面
    String PM_LIST_ITEM_EDIT = URL+"api/v1/life/shop/item/detail";     //项目管理列表条目编辑，拉取数据接口
    String PM_NEW_ADD_ITEM = URL+"api/v1/life/shop/item/save";     //项目管理新增条目保存，修改完成后保存接口
    String PM_ON_OR_OUT_LINE= URL+"api/v1/life/shop/item/online";     //项目管理条目上线，下线接口
    int UP_STATE = 1;
    int DOWN_STATE = 0;
    String PM_NO_ON_LINE_DELETE= URL+"api/v1/life/shop/item/del";     //项目管理未上线过的条目删除，仅仅是保存过的草稿，未进行上下线操作
    /**新建套餐*/
    String SET_MEAL_NEW_ADD = URL+"api/v1/life/shop/setMeal/save";
    /**更新套餐*/
    String SET_MEAL_UPDATA = URL+"api/v1/life/shop/setMeal/save";
    /**根据ID查询套餐信息*/
    String SET_MEAL_QUERY_BY_ID = URL+"api/v1/api/setMeal/querySetMealById";
    /**查询套餐列表*/
    String SET_MEAL_QUERY_LIST = URL+"api/v1/life/shop/setMeal/records";
    /**上传文件*/
    String UPLOAD_IMAGE = URL+"api/v1/mgs/file/imageUpload";

    /**本地生活分类查询*/
    String CATEGORY_QUERY_LIST = URL+"api/v1/life/category/list";
}
