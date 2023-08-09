package org.hsgt.strategy;

import org.hsgt.entities.pricing.Competitor;
import org.hsgt.entities.pricing.Offer;

import java.util.List;

public interface Strategy {

    public static final float POSITIVE_INFINITY = 99999;
    public static final int APPROVED = 0;       // 接受
    public static final int REJECTED_PRICE_FULFILLED = 1;  // 价格已经满足调节
    public static final int REJECTED_LOSS = 2; // 价格会造成亏损
    public static final int REJECTED_RISK_LOSS = 3; // 价格会有可能造成亏损
    public static final int REJECTED_INVALID_PRICE = 4; //  价格无效，例如价格低于0
    public static final int REJECTED_NO_COMPETITORS = 5; //  没有竞争者
    public static final int REJECTED_LARGE_ADJUST = 6;   // 调幅过大

    /*
     * @param null:
      * @return null
     * @author Lin
     * @description TODO 进行价格比价，返回比价后能取得优势的结果
     * @date 20.Dec.2022 020 02:09
     */
    public abstract Competitor execute(Competitor me, List<Competitor> competitorList, Offer offer) ;
    public int getState();
}
