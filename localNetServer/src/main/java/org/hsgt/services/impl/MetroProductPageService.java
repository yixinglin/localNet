package org.hsgt.services.impl;

import org.hsgt.api.SellerApi;
import org.hsgt.builders.metro.MetroProductPageBuilder;
import org.hsgt.config.Global;
import org.hsgt.entities.common.ProductPage;
import org.hsgt.entities.pricing.Competitor;
import org.hsgt.entities.pricing.Offer;
import org.hsgt.mappers.CompetitorMapper;
import org.hsgt.mappers.OfferMapper;
import org.hsgt.mappers.ShippingGroupMapper;
import org.hsgt.mappers.SqlService;
import org.hsgt.services.ProductPageService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetroProductPageService implements ProductPageService {
    @Autowired
    private OfferMapper offerMapper;
    @Autowired
    private ShippingGroupMapper shippingGroupMapper;
    @Autowired
    private CompetitorMapper competitorMapper;

    private SellerApi api;

    public MetroProductPageService() {
        this.api = Global.getMetroApiInstance();
    }

    /*
     * @param id: ID for the query
      * @return ProductPage
     * @author Lin
     * @description TODO [UTD] Query data of a product page from API. The data is up-to-date.
     *               After that the latest data will be store in the databse.
     * @date 16.Aug.2023 016 02:21
     */
    @Override
    public ProductPage queryById(String id) {
        // Product page data from API to database
        String s = this.api.selectProductPageById(id).getContent();
        JSONObject jPage = new JSONObject(s);
        MetroProductPageBuilder builder = new MetroProductPageBuilder(this.api.accountName());
        ProductPage productPage = builder.pageInfo(jPage).sellers(jPage).build();
        for (Competitor c: productPage.getCompetitors()) {
            SqlService.sqlInsertOrUpdate(c, competitorMapper);
            SqlService.sqlInsertOrUpdate(c.getShippingGroup(), shippingGroupMapper);
        }
        // Product data from database.
        List<Competitor> competitors = competitorMapper.findAllCompetitorByProductId(productPage.getCode());
        List<Competitor> self = competitors.stream().filter(c -> c.getShopName().equals(this.api.accountName())).collect(Collectors.toList());
        productPage.setCompetitors(competitors);
        productPage.setSelf(self.get(0));
        return productPage;
    }

    /*
     * @param ids:
      * @return List<ProductPage>
     * @author Lin
     * @description TODO [UTD] Query data of a product pages from API. The data is up-to-date.
     *               After that the latest data will be store in the databse.
     * @date 16.Aug.2023 016 02:28
     */
    public List<ProductPage> queryById(List<String> ids) {
        List<ProductPage> productPages = new ArrayList<>();
        for (String id: ids) {
            productPages.add(this.queryById(id));
        }
        return productPages;
    }

    /*
     * @param :
      * @return List<ProductPage>
     * @author Lin
     * @description TODO Query all product pages from database. The data may not be up-to-date
     * @date 16.Aug.2023 016 02:51
     */
    @Override
    public List<ProductPage> queryAll() {
        List<ProductPage> pages = new ArrayList<>();
        List<Offer> offers = this.offerMapper.selectList(null);
        for (Offer offer: offers) {
            ProductPage page = new ProductPage();
            List<Competitor> sellers = this.competitorMapper.findAllCompetitorByProductId(page.getId());
            page.setCompetitors(sellers);
            page.setId(offer.getProductKey());
            page.setCode(offer.getId());
            page.setProductName(offer.getProductName());
            pages.add(page);
        }
        return pages;
    }

}
