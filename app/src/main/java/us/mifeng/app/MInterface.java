package us.mifeng.app;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by k on 2016/11/24.
 */

public class MInterface {
    /**
     * 服务器的主机地址
     */
    public static String zhuji = "http://192.168.4.188/Goods";
    /**
     * 注册激活的接口
     */
    public static String zhuce = "/app/common/reg.json";
    /**
     *登录验证的接口
     */
    public static String denglu = "/app/common/login.json";
    /**
     *版本更新的接口
     */
    public static  String update = "/app/common/version.json";
    /**
     *已发布的商品列表查询
     */
    public static String yiFaBuLieBiao = "/app/user/issue_list.json";
    /**
     * 已关注的商品列表查询
     */
    public static String yiGuanZhuLieBiao = "/app/user/follow_list.json";
    /**
     * 个人信息查看
     */
    public static String geren = "/app/user/info.json";
    /**
     * 上传头像
     */
    public static String touxiang = "app/user/upload.json";
    /**
     * 邀请码
     */
    public static String yaoqingma = "/app/user/invite.json";
    /**
     * 发布的商品
     */
    public static String fabu = "/app/item/issue.json";
    /**
     * 商品列表查询
     */
    public static String shangpinliebiao = "/app/item/issue.json";
    /**
     * 商品详细查询
     */
    public static String xiangqing = "/app/item/detail.json";
    /**
     * 商品状态变更
     */
    public static String zhuangtai = "/app/item/modify.json";
    /**
     * 关注商品
     */
    public static String guanzhu = "/app/item/follow.json";

    /**
     * 获取登录后的TOKEN值
     */
    public static String getSp(Context context){
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        String token = sp.getString("token", null);
        return token;
    }
    /**
     * 获取账户信息
     */
    public static int getType(Context context){
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        Integer type = sp.getInt("type", 0);
        return type;
    }
    /**
     * 获取当前账号的名字
     */
    public static String getName(Context context){
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        String name = sp.getString("name", "");

        return name;

    }

}
