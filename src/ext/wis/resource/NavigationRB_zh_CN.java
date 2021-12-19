/**
 * 
 */
package ext.wis.resource;

import wt.util.resource.RBEntry;
import wt.util.resource.RBPseudo;
import wt.util.resource.WTListResourceBundle;

/**
 * rbInfo文件标准写法
 * 1、继承自WTListResourceBundle
 * 2、类名为XxxRB_语言代码,没有语言代码的文件为默认内容XxxRB
 */
public class NavigationRB_zh_CN extends WTListResourceBundle {
    @RBEntry("报表")
    public static final String CONSTANTS_1 = "object.wisReport navigation.title";

    @RBEntry("报表")
    public static final String CONSTANTS_2 = "object.wisReport navigation.description";

    @RBEntry("报表")
    public static final String CONSTANTS_3 = "object.wisReport navigation.tooltip";
}
