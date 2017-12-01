package com.yskj.wdh.url;

/**
 * Created by Administrator on 2016/10/10 0010.
 */
public class Urls {
    //登陆
    public static final String LOGIN = Ips.API_URL+"api/v1/auth/oauth/token";
    public static final String BUSINESS = Ips.API_URL+"api/v1/life/shop/info";
    public static final String consumptionInvokeUrl = Ips.API_URL+"api/v1/life/shop/couponActivate";//消费卷激活
    public static final String SHOUYIJILU = Ips.API_URL+"api/v1/life/shop/profit/records";//收益记录
    public static final String QRCODEFK = Ips.API_URL+"api/v1/life/payCode/get";//获取二维码标识
    public static final String QRCODWPAY = Ips.API_URL+"api/v1/life/payCode/scanReceipt";//商家扫码
    //查询银行卡
    public static final String BANKLIST = Ips.API_URL+"api/v1/user/card/findByUserID";
    //提现记录
    public static final String RECORDLIST = Ips.API_URL+"api/v1/user/fund/withdraw/records";
    //提现
    public static final String REEORD = Ips.API_URL+"api/v1/user/fund/withdraw/apply";
    public static final String TRANSFERTOUSER = Ips.API_URL+"api/v1/life/shop/transfer/toUser";//商家资金转入可用云豆
    public static final String TRANSFERTOLOCALSHOP = Ips.API_URL+"api/v1/life/shop/transfer/toShop";//可用云豆转入商家可用资金
    public static final String checkedNumberUrl = Ips.API_URL+"api/v1/life/shop/couponActivate/records";//验证历史验证量
    public static final String projectNumberUrl = Ips.API_URL+"api/v1/life/shop/couponActivate/records";//验证项目数
    //获取个人信息
    public static final String USERINFO = Ips.API_URL+"api/v1/user/getInfo";


    //请求验证码
    public static final String VIERFI = Ips.API_URL+"api/v1/sms/sendCode";

    //查询实名姓名
    public static final String GETREALNAME = Ips.API_URL+"api/v1/user/realname/getRealName";

    //验证验证码
    public static final String VIERFICODE = Ips.API_URL+"api/v1/sms/checkCode";

    //更新银行卡信息
    public static final String UPDATEBANK = Ips.API_URL+"api/v1/user/card/updateById";

    //添加银行卡
    public static final String ADDBANK = Ips.API_URL+"api/v1/user/card/save";

    //查询实名认证
    public static final String REALNAME= Ips.API_URL+"api/v1/user/realname/show";

    //实名认证
    public static final String IDENRITY = Ips.API_URL+"api/v1/user/realname/save";

    //删除银行卡
    public static final String DELBANK = Ips.API_URL+"api/v1/user/card/deleteById";

    //查询手机号是否存在
    public static final String FINDBYMOBILE = Ips.API_URL+"api/v1/user/findByMobile";

    //修改支付密码
    public static final String UPDATEACCOUNT = Ips.API_URL+"api/v1/user/updateAccountPassword";
    //未登录修改会员密码
    public static final String NOTLOGINPW = Ips.API_URL+"api/v1/user/passwordUpdateNotLogin";
    //账单
    public static final String REVEBU = Ips.API_URL+"api/v1/user/msg/profitLog/records";
    //七牛云接口
    public static final String QNYJSON = "http://oojnq6nur.bkt.clouddn.com/wdh_sign.json";
    //商家申请开通仪器
    public static final String DECTION = Ips.API_URL+"api/v1/life/payCode/savePayCode";
    //检测详情
    public static final String DETIONDAT = Ips.SHAREURL+"/App/NewsPort/articleList/type/2/sk/2ab99a6af22365686e97992df974d5c2";

    //代理申请
    public static final String REGIONAGENT = Ips.API_URL+"api/v1/life/agent/save";
    //代理信息查询
    public static final String REGIONAGENTINFO = Ips.API_URL+"api/v1/life/agent/detail";
    //分页直供商品   采购单列表查询
    public static final String PURCHASEORDER = Ips.API_URL+"api/v1/life/purchase/list";
    //分页查询直供商品列表
    public static final String STRAIGHTFOR = Ips.API_URL+"api/v1/life/purchase/goodsList";
    //分页查询分享商品
    public static final String SHAREFOR = Ips.API_URL+"api/v1/life/sharedList";
    //采购单详情查询
    public static final String BUYERINFO = Ips.API_URL+"api/v1/life/purchase/detail";
    //申请采购商取消订单
    public static final String PURCHASECANCELORDER = Ips.API_URL+"api/v1/life/purchase/cancel";
    //采购单审批接口
    public static final String APPLYORDER = Ips.API_URL+"api/v1/life/purchase/check";
    //直供商品采购申请
    public static final String STRAIGHTFORAPPLY = Ips.API_URL+"api/v1/life/purchase/apply";
    //收银台
    public static final String PROCSE = Ips.API_URL+"api/v1/life/goodOrder/list";
    //订单详情
    public static final String DETAIL = Ips.API_URL+"api/v1/life/goodOrder/detail";
    //根据条形码识别商品
    public static final String INFOR = Ips.API_URL+"api/v1/life/shop/goods/info";
    //创建临时订单
    public static final String BILLING = Ips.API_URL+"api/v1/life/goodOrder/billing";
    //分享商品详情查看
    public static final String SHAREGOODSINFO = Ips.API_URL+"api/v1/life/goodsDetail";
    //分享商品
    public static final String SHARLIFE = Ips.API_URL+"api/v1/life/update";
    //本地生活分类弹窗
    public static final String LOCALSERVERPOPWIN = Ips.API_URL + "api/v1/life/category/list";
    //上传头像
    public static final String UPDATRSVTEA = Ips.API_URL + "api/v1/mgs/file/imageUpload";
    //商家入住
    public static final String APPLY = Ips.API_URL + "api/v1/life/shop/edit";
    //删除直供商品订单
    public static final String DEL = Ips.API_URL + "api/v1/life/goodOrder/del";
    //请求资金
    public static final String MONEYINFO  = Ips.API_URL+"api/v1/user/accountInfo";
    //供应商申请
    public static final String PROVIDERMANAGER  = Ips.API_URL+"api/v1/life/mill/apply";
    //商品列表查询
    public static final String PROVIDERGOODSLIST  = Ips.API_URL+"api/v1/life/shopGood/goodsPageList";
    //商品轮播图片上传接口
    public static final String PROVIDERGOODSIMGUPLOAD  = Ips.API_URL+"api/v1/mgs/goodImage/upload";
    //商品详情图片上传
    public static final String PROVIDERGOODSIMGDETAILUPLOAD  = Ips.API_URL+"api/v1/mgs/goodImage/contentImgUpload";
    //商品详情编辑/添加
    public static final String PROVIDERGOODSINFO  = Ips.API_URL+"api/v1/life/shopGood/goodContentEdit";
    //添加/编辑商品
    public static final String PROVIDERGOODS  = Ips.API_URL+"api/v1/life/shopGood/goodAdd";
    //分页查询供应商订单列表
    public static final String MALLORDERLIST  = Ips.API_URL+"api/v1/life/mgorder/millOrderlist";
    //查询供应商订单数量
    public static final String MALLORDERLISTCOUNT  = Ips.API_URL+"api/v1/life/mgorder/countOrder";
    //供应商查询订单详情
    public static final String MALLORDERDETAIL  = Ips.API_URL+"api/v1/life/mgorder/millOrderdetail";
    //运费模板编辑/新增
    public static final String EDITEXPRESS  = Ips.API_URL+"api/v1/life/shopGood/transportEdit";
    //查询商品分类列表
    public static final String GOODSCATEGORY  = Ips.API_URL+"api/v1/life/shopGood/goodsCategory";
    //查询供应商总收入
    public static final String MILLINFO  = Ips.API_URL+"api/v1/life/mill/info";
    //查询账单金额
    public static final String GETPROFITAMOUNTBYUSER  = Ips.API_URL+"api/v1/user/msg/profitLog/getProfitAmountByUser";
    //查询商品分类
    public static final String GETGOODSCATE  = Ips.API_URL+"api/v1/life/goodsCate/list";
    //商品状态变更
    public static final String GOODSSTATUSCHANGE  = Ips.API_URL+"api/v1/life/shopGood/goodStatusChange";
    //查询商品详细信息
    public static final String GOTGOODSDETAIL  = Ips.API_URL+"api/v1/life/shopGood/goodsDetail";
    //根据供应商id,查询供应商下的运费模板
    public static final String FINDEXPRESSMODE  = Ips.API_URL+"api/v1/life/shopGood/goodsTransportPageList";
    //根据运费模板id和运费模板类型查询运费模板详情
    public static final String FINDEXPRESSMODEDETAIL  = Ips.API_URL+"api/v1/life/shopGood/millTransportDetail";
    //完善物流信息—填写发货单号
    public static final String LOGISTICSSEND  = Ips.API_URL+"api/v1/life/mgorder/completeTransport";
}
