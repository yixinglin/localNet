package org.hsgt.entities.pricing;

import java.util.List;

public interface Strategy {

    /*
     * @param null:
      * @return null
     * @author Lin
     * @description TODO 进行价格比价，返回比价后能取得优势的结果
     * @date 20.Dec.2022 020 02:09
     */
    Competitor execute(Competitor me, List<Competitor> competitorList, Offer offer) ;
    public int getState();
    public void setState(int state);
}
