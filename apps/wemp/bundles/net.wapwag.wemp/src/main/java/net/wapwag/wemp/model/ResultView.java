package net.wapwag.wemp.model;

/**
 * Result view class for json parse
 * Created by Administrator on 2016/10/31 0031.
 */
public class ResultView {

    public final long count;

    public ResultView(long count) {
        this.count = count;
    }

    public static ResultView newInstance(long count) {
        return new ResultView(count);
    }

    public static ResultView newInstance(boolean flag) {
        return new ResultView(flag ? 1 : 0);
    }
}
