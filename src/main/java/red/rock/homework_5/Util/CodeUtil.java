package red.rock.homework_5.Util;

import java.util.Random;

/**
 * TODO
 *
 * @Date 2019/5/914:38
 * @Author tudou
 */


public class CodeUtil {

    public static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 生成随机字符串
     * @return String
     */
    public static String getCode(){
        StringBuffer sb = new StringBuffer();
        Random random=new Random();
        for(int i=0;i<20;i++){
            sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
        }
        return sb.toString();
    }
}
