package com.zach.biz.view.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class StringUtil {

    //分割字符串正则表达式
    private final static Pattern SPLIT_PATTERN = Pattern.compile("\n|\\[|\\]|;|,|\\|");

    /**
     * 空字符串
     */
    public final static String EMPTY = "";

    /**
     * 字符串是否是空白字符串
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((!Character.isWhitespace(str.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 是否是true字符串
     *
     * @param str
     * @return
     */
    public static boolean isTrue(String str) {
        return "true".equalsIgnoreCase(str) || "t".equalsIgnoreCase(str) || "1".equals(str) || "yes".equalsIgnoreCase(str) || "on".equalsIgnoreCase(str);
    }

    /**
     * 删除空白字符串
     *
     * @param str
     * @return
     */
    public static String deleteWhitespace(String str) {
        if (str == null) {
            return null;
        }
        int sz = str.length();
        StringBuilder buffer = new StringBuilder(sz);
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                buffer.append(str.charAt(i));
            }
        }
        return buffer.toString();
    }

    /**
     * 连接连个对象数组
     *
     * @param src
     * @param dis
     * @return
     */
    public static Object[] join(Object[] src, Object[] dis) {
        Object[] answer = new Object[src.length + dis.length];
        System.arraycopy(src, 0, answer, 0, src.length);
        System.arraycopy(dis, 0, answer, src.length, dis.length);
        return answer;
    }

    /**
     * 按照\n|;|,|\\|分割字符串,不使用任何注释
     *
     * @param str 字符串
     * @return 分割结果
     */
    public static String[] split(String str) {
        return parseStringSplit(str, (char) 0);
    }

    /**
     * 按照\n|;|,|\\|分割字符串, 首字母是#号表示注释跳过
     *
     * @param str 字符串
     * @return 分割结果
     */
    public static String[] parseStringSplit(String str) {
        return parseStringSplit(str, '#');
    }

    /**
     * 分割字符串按照 \n|;|,|\\|
     *
     * @param str  字符串
     * @param comm 注释字符
     * @return 分割结果
     */
    public static String[] parseStringSplit(String str, char comm) {
        String[] answer = null;
        if (str != null && str.length() > 0) {
            String[] sp = SPLIT_PATTERN.split(str, 0);
            String[] array = new String[sp.length];
            int pos = 0;
            if (sp.length > 0) {
                for (String t : sp) {
                    t = t.trim();
                    if (t.length() == 0 || (comm > 0 && t.charAt(0) == comm)) continue;
                    array[pos++] = t;
                }
                answer = new String[pos];
                System.arraycopy(array, 0, answer, 0, pos);
            } else {
                answer = null;
            }
        }
        return answer;
    }

    /**
     * 占位符标识,支持多种格式占位符:  ${} %{} &{}
     */
    private static final char VARIABLE_TOKEN_OPEN_1_1 = '$';

    private static final char VARIABLE_TOKEN_OPEN_1_2 = '%';

    private static final char VARIABLE_TOKEN_OPEN_1_3 = '&';

    /**
     * 占位符开始
     */
    private static final char VARIABLE_TOKEN_OPEN_2 = '{';

    /**
     * 占位符结束
     */
    private static final char VARIABLE_TOKEN_CLOSE = '}';

    /**
     * 格式化字符串,例如: hello ${word},  ${word}被参数替换
     *
     * @param template 字符串
     * @param values   值
     * @return
     */
    public static String format(String template, Object... values) {
        return parsePropertyTokens(template, null, values);
    }

    /**
     * 格式化字符串,例如: hello ${word},  ${word}被变量替换
     *
     * @param template   字符串
     * @param properties 变量
     * @return
     */
    public static String format(String template, Map<String, Object> properties) {
        return parsePropertyTokens(template, properties, new Object[0]);
    }

    /**
     * 替换系统中的环境变量 ${}
     *
     * @param string     字符串
     * @param properties 配置
     * @return
     */
    public static String parsePropertyTokens(String string, Map<String, Object> properties, Object values[]) {
        String answer = null;
        if (string != null) {
            StringBuilder sb = new StringBuilder();
            char[] chars = string.toCharArray();
            Object propValue = null, propName = null;
            int begin = 0, open = 0, found = 0;
            for (int pos = 0, next = 0, len = chars.length; pos < len; ++pos) {
                next = pos + 1;
                if ((chars[pos] == VARIABLE_TOKEN_OPEN_1_1 || chars[pos] == VARIABLE_TOKEN_OPEN_1_2 || chars[pos] == VARIABLE_TOKEN_OPEN_1_3) && next < len
                    && chars[next] == VARIABLE_TOKEN_OPEN_2) {
                    if (begin >= 0 && pos > 0) {
                        sb.append(string.substring(begin, pos));
                        begin = pos;
                    }
                    open = pos = pos + 2;
                    for (; pos < len && chars[pos] != VARIABLE_TOKEN_CLOSE; pos++) {
                    }
                    if (pos > open) {
                        propName = string.substring(open, pos);
                        if (values != null && propValue == null && found < values.length) {
                            propValue = values[found];
                            found++;
                        } else if (properties != null) {
                            propValue = properties.get(propName);
                        }
                        if (propValue != null) {
                            sb.append(propValue);
                            begin = pos + 1;
                            propValue = null;
                        }
                    }
                }
            }
            if (begin < chars.length) {
                sb.append(string.substring(begin, chars.length));
            }
            answer = sb.toString();
        }
        return answer;
    }

    /**
     * 把Collection对象转换字符串
     *
     * @param collection 列表对象
     * @param sep        分割符号
     * @return
     */
    public static String serialize(Collection<?> collection, String sep) {
        StringBuilder sb = new StringBuilder();
        Iterator<?> it = collection.iterator();
        boolean bool = false;
        while (it.hasNext()) {
            if (bool) sb.append(sep);
            else bool = true;
            sb.append(it.next());
        }
        return sb.toString();
    }

    /**
     * 列出对象所有属性值
     *
     * @param obj 对象
     * @return
     */
    public static String dump(Object obj) {
        String answer = null;
        if (obj != null) {
            try {
                Class<?> cls = obj.getClass();
                answer = "[[" + obj.getClass().getName() + "[\n";
                Field fieldlist[] = cls.getDeclaredFields();
                for (int i = 0; i < fieldlist.length; i++) {
                    Field fld = fieldlist[i];
                    boolean bool = fld.isAccessible();
                    fld.setAccessible(true);
                    answer += "field [";
                    answer += "name = " + fld.getName() + " ";
                    answer += "value = " + fld.get(obj) + "";
                    //answer += "type = " + fld.getType() + "\n";
                    //answer += "modifiers = " + Modifier.toString(fld.getModifiers()) + "\n";
                    answer += "]\n";
                    fld.setAccessible(bool);
                }
                answer += "]" + "\n";
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return answer;
    }

    /**
     * str==null 或者 str.length ==0 返回true
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * Object[]==null 或者 Object[].length ==0 返回true
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(Object[] objects) {
        return objects == null || objects.length == 0;
    }

    /**
     * obj==null  返回true
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object object) {
        return object == null;
    }

    /**
     * collection== null 或者 collection.size()==0 返回true <br />
     * 用于判断 List 类和Set 类
     *
     * @param list
     * @return
     */
    public static boolean isEmpty(Collection<?> list) {
        return list == null || list.size() == 0;
    }

    /**
     * map== null 或者 map.size()==0 返回true
     *
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.size() == 0;
    }

    /**
     * iterator== null 或者 iterator.size()==0 返回true
     *
     * @param it
     * @return boolean
     */
    public static boolean isEmpty(Iterator<?> it) {
        return it == null || it.hasNext();
    }

    /**
     * enumeration== null 或者 enumeration.size()==0 返回true
     *
     * @param e
     * @return boolean
     */
    public static boolean isEmpty(Enumeration<?> e) {
        return e == null || e.hasMoreElements();
    }

    /**
     * 判定文件夹或者文件是否为空<br />
     *
     * @param file
     * @return boolean
     */
    public static boolean isEmpty(File file) {
        boolean answer = true;
        if (file != null && file.exists()) {
            if (file.isFile()) {
                answer = file.length() == 0;
            } else if (file.isDirectory()) {
                answer = file.listFiles().length == 0;
            }
        }
        return answer;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isNotEmpty(Object[] objects) {
        return !isEmpty(objects);
    }

    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }

    public static boolean isNotEmpty(Collection<?> list) {
        return !isEmpty(list);
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean isNotEmpty(Iterator<?> it) {
        return !isEmpty(it);
    }

    public static boolean isNotEmpty(Enumeration<?> e) {
        return !isEmpty(e);
    }


    /**
     * 驼峰命名   === > 下划线
     */
    public static String camel2underline(String param) {
        Pattern p = Pattern.compile("[A-Z]");
        if (param == null || param.equals("")) {
            return "";
        }
        StringBuilder builder = new StringBuilder(param);
        Matcher mc = p.matcher(param);
        int i = 0;
        while (mc.find()) {
            builder.replace(mc.start() + i, mc.end() + i, "_" + mc.group().toLowerCase());
            i++;
        }

        if ('_' == builder.charAt(0)) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }

    /**
     * 生产length长度的随机字符串
     *
     * @param length
     * @return
     */
    public static String getRandomStr(int length) {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String getUUIDString(int length) {
        if (length <= 0) {
            length = 6;
        }
        if (length > 32) {
            length = 32;
        }
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, length);
    }

    public static boolean isNumeric(String character) {
		Pattern pattern = Pattern.compile("[0-9,]*");
		Matcher matcher = pattern.matcher(character);
		if(matcher.matches()) {
			return true;
		}
		return false;
	}
    
    public static String processBigdecimalZero(String bigdecimalString) {
		if (StringUtil.isBlank(bigdecimalString)) {
			return "0";
		}else{
			if (bigdecimalString.contains(".")) {
				// -189.0   180.9
				String[] split = bigdecimalString.split("\\.");
				if (split.length == 2) {
					String isZero = split[1];
					if ("0".equals(isZero)) {
						return split[0];
					}
				}else if(split.length == 1){
					return split[0];
				}
				return bigdecimalString;
			}
			return bigdecimalString;
		}
	}
}
/* Honey v5.0.0 dev-1 - version:1 - OriginalChecksum:B5c86a46a10e6b562 (do not edit this line) */