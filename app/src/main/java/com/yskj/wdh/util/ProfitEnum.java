package com.yskj.wdh.util;


import com.yskj.wdh.R;

/**
 * Created with Android Studio.
 * 作者: wanglei
 * 日期: 2016/6/5 11:31
 */
public enum ProfitEnum {
    //充值体现
    Profit_1000(1000, "提", "用户提现", false),
    Profit_1001(1001, "转", "转账记录-转出", false),
    Profit_1002(1002, "转", "转账记录-转入", true),
    Profit_1003(1003, "购", "支付宝购买", true),
    Profit_1004(1004, "购", "微信充购买", true),
    Profit_1005(1005, "购", "银联购买", true),
    Profit_1006(1006, "购", "WEB-支付宝购买", true),
    Profit_1007(1007, "购", "WEB-微信充值购买", true),
    Profit_1009(1009, "购", "WEB-银联充值购买", true),
    Profit_1010(1010, "购", "商城购买", false),
    Profit_1011(1011, "加", "分润", true),
    profit_1012(1012, "加", "GP值增加", true),
    profit_1013(1013, "拆", "拆红包-可用通贝", true),
    //    商城购物
    Profit_1015(1015, "得", "会员注册", true),
    Profit_1016(1016, "得", "邀请朋友", true),
    Profit_1017(1017, "收", "商家收款-消费券", true),
    Profit_1018(1018, "得", "消费券商家赠送云积分", true),
    Profit_1019(1019, "拆", "消费红包", true),
    Profit_1020(1020, "拆", "推广红包", true),
    Profit_1021(1021, "购", "本地生活购买-云积分", false),
    Profit_1022(1022, "购", "本地生活购买-可用通贝", false),
    Profit_1023(1023, "退", "本地生活退款-云积分", true),
    Profit_1024(1024, "退", "本地生活退款-可用通贝", true),
    Profit_1025(1025, "分", "分红宝-分红", true),
    Profit_1026(1026, "退", "提现失败，退还通贝", true),
    Profit_1027(1027, "充", "管理员充值-可用通贝", true),
    Profit_1028(1028, "扣", "管理员扣款-可用通贝", false),
    Profit_1029(1029, "充", "管理员充值-云积分", true),
    Profit_1030(1030, "扣", "管理员扣款-云积分", false),
    Profit_1035(1035, "宝", "分红宝收益-月", true),
    Profit_1036(1036, "宝", "分红宝收益-季", true),
    Profit_1037(1037, "收", "扫码支付-用户支付", true),
    Profit_1038(1038, "收", "扫码支付-商家收款", true),
    Profit_1039(1039, "转", "会员转账给商家", true),
    Profit_1040(1040, "收", "商家收到会员的转账", true),
    Profit_1041(1041, "转", "商家转账给会员", false),
    Profit_1042(1042, "收", "会员收到商家转账", false),
    Profit_1058(1058, "收", "仪器体验扫码支付-商家收款", true),
    Profit_1062(1062, "收", "本地生活非直供商品-商家收款", true),
    Profit_1063(1063, "分", "本地生活直供商品-代理分润", true),
    Profit_1064(1064, "分", "本地生活直供商品-商家分润", true),
    Profit_1065(1065, "分", "本地生活直供商品-成本收益", true),
    Profit_1066(1066, "分", "分红收益-推广者收益", true),
    Profit_1067(1067, "加", "直供商品购物，增加M值", true),
    Profit_1068(1068, "购", "直供商品购物，累加总计购物金额", false),
    Profit_1069(1069, "赠", "商城购物-赠送云积分", true),
    Profit_1070(1070, "赠", "直供商品-赠送云积分", true),
    Profit_1071(1071, "加", "本地生活消费代理分红", true),
    Profit_1072(1072, "赠", "会员注册-赠送红包值", true),
    Profit_1073(1073, "拆", "商家红包-可用通贝", true),
    Profit_1074(1074, "加", "本地生活直供商品,随机奖金-可用通贝", true),
    Profit_1079(1079, "购", "供应商-用户购物-可用通贝", false),
    Profit_1081(1081, "收", "供应商-成本收益", true),
    Profit_1082(1082, "收", "供应商-分销收益", true),
    Profit_1083(1083, "收", "供应商-消费者收货地址区域代理分润", true),
    Profit_1084(1084, "收", "供应商-消费者随机奖金", true),
    Profit_1085(1085, "收", "供应商-增加M值", true),
    Profit_1086(1086, "购", "供应商-累加总计购物金额", false),
    Profit_1087(1087, "拆", "拓展红包", true),
    ;
    int type;
    String leixing;
    String text;
    boolean isProfit;

    ProfitEnum(int type, String leixing, String text, boolean isProfit) {
        this.type = type;
        this.leixing = leixing;
        this.text = text;
        this.isProfit = isProfit;
    }

    public int getType() {
        return type;
    }

    public String getLeixing() {
        return leixing;
    }

    public String getText() {
        return text;
    }

    public boolean isProfit() {
        return isProfit;
    }

    public static ProfitEnum getByType(int type) {
        for (ProfitEnum p : values()) {
            if (p.getType() == type) {
                return p;
            }
        }
        return null;
    }

    public int getProfitColor() {
        return isProfit ? R.color.red2 : R.color.green;
    }

    public int getLeiXingColor() {
        switch (leixing) {
            case "收":
                return R.drawable.shou_text;
            case "充":
                return R.drawable.chong_text;
            case "转":
                return R.drawable.zhuan_text;
            case "提":
                return R.drawable.ti_text;
            case "购":
                return R.drawable.gou_text;
            case "发":
                return R.drawable.fa_text;
            case "拆":
                return R.drawable.chai_text;
            case "奖":
                return R.drawable.chai_text_jiang;
            case "支":
                return R.drawable.zhuan_text;
            case "结":
                return R.drawable.gou_text;
            case "反":
                return R.drawable.zhuan_text;
            case "加":
                return R.drawable.chai_text;
            case "退":
                return R.drawable.chong_text;
            case "返":
                return R.drawable.fa_text;
            case "申":
                return R.drawable.chai_text_jiang;
            case "得":
                return R.drawable.zhuan_text;
        }
        return R.drawable.shou_text;
    }
}
