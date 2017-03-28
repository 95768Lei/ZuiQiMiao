package com.zl.project.fisrt_project.Utils;

/**
 * Created by zhanglei on 2016/12/6.
 */

public class API {

    public static int LEFT = 1;
    public static int RIGHT = 2;

    /**
     * APK下载路径
     */
    public static String DOWNLOADPATH = "http://imtt.dd.qq.com/16891/587193700A3719C33719496AA13EC420.apk?fsname=com.zl.project.fisrt_project_1.1_2.apk&csr=4d5s";

    /**
     * 腾讯联盟申请的应用id
     */
    public static String APP_ID = "1105601425";

    /**
     * 开屏广告位id
     */
    public static String PUBLIC = "2080215891755267";

    /**
     * 横幅广告位id
     */
    public static String BANNER = "1050114891823981";

    /**
     * 插屏广告位id
     */
    public static String INSERT = "1050517861750236";

    /**
     * 参数type=top
     * top(头条，默认),shehui(社会),guonei(国内),guoji(国际),yule(娱乐),tiyu(体育)junshi(军事),keji(科技),caijing(财经),shishang(时尚)
     */
    public static String TOUTIAO = "http://v.juhe.cn/toutiao/index?&key=8a6dbcca30ff4c12c6c2a89c7982c916";

    /**
     * 笑话大全接口
     * &page=2&pagesize=10&sort=asc&time=1418745237
     * 参数：
     * sort string 	是 	类型，desc:指定时间之前发布的，asc:指定时间之后发布的
     */
    public static String JOKE = "http://japi.juhe.cn/joke/content/list.from?&key=992242effb2a152e639a33753b8cc55e";

    /**
     * 星座运势
     * consName=%E7%8B%AE%E5%AD%90%E5%BA%A7&type=today&
     */
    public static String LUCK = "http://web.juhe.cn:8080/constellation/getAll?key=9b3eff2f2a260d35d3579021af46c0ac";

    /**
     * 周公解梦
     * 参数：
     * &q= （关键字）
     */
    public static String ZG = "http://v.juhe.cn/dream/query?key=22eded71a86fb03513b3f158dd5949ae&q=";

    /**
     * 手机号归属地查询
     * 参数：
     * &postcode=
     */
    public static String PHONEQUERY = "http://apis.juhe.cn/mobile/get?&key=7b93f8141e83698016f69d1269f44a8a&phone=";

    /**
     * 问答机器人
     * 参数：info = (要问的内容)
     */
    public static String ROBOT = "http://op.juhe.cn/robot/index?&key=139d3401e0062e1ff3911dc320937ee7&info=";
}
