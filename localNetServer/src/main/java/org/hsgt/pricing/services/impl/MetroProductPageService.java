package org.hsgt.pricing.services.impl;

import org.hsgt.core.mapper.SqlService;
import org.hsgt.pricing.rest.builders.metro.MetroProductPageBuilder;
import org.hsgt.pricing.config.MetroPricingConfig;
import org.hsgt.pricing.domain.ProductPage;
import org.hsgt.pricing.domain.pricing.Competitor;
import org.hsgt.pricing.domain.pricing.Configure;
import org.hsgt.pricing.domain.Offer;
import org.hsgt.pricing.mapper.*;
import org.hsgt.pricing.rest.common.SellerApi;
import org.hsgt.pricing.services.ProductPageService;
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
    @Autowired
    private ConfigureMapper configureMapper;

    @Autowired
    private MetroPricingConfig pricingConfig;

    public MetroProductPageService() {
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
        SellerApi api = pricingConfig.getApiInstance();
        // Product page data from API to database
        Offer offer = this.offerMapper.selectById(id);
        Configure configure = this.configureMapper.selectById(id);
        String s = api.selectProductPageById(offer.getProductKey()).getContent();
        JSONObject jPage = new JSONObject(s);
        MetroProductPageBuilder builder = new MetroProductPageBuilder(api.accountName());
        ProductPage productPage = builder.pageInfo(jPage).sellers(jPage).build();
        int numSeller0 = productPage.getCompetitors().size();   // Number of sellers acquired from API
        int n = competitorMapper.deleteById(productPage.getCode());   // Delete sellers from database by product id.
        for (Competitor c: productPage.getCompetitors()) {
            // SqlService.sqlInsertOrUpdate(c, competitorMapper);
            competitorMapper.insert(c);
            SqlService.sqlInsertOrUpdate(c.getShippingGroup(), shippingGroupMapper);
        }
        // Product data from database.
        List<Competitor> competitors = competitorMapper.findAllCompetitorByProductId(productPage.getCode());
        configure.getStrategy().sort(competitors);
        int numSeller1 = competitors.size();
        assert numSeller1 == numSeller0: "Number of sellers acquired from API should be the same as from database";
        List<Competitor> self = competitors.stream().filter(c -> c.getShopName().equals(api.accountName())).collect(Collectors.toList());
        if (self.size() == 0)
            throw new RuntimeException("Your product is NOT in a competition!");
        productPage.setCompetitors(competitors);
        productPage.setSelf(self.get(0));
        return productPage;
    }

    /*
     * @param ids:
      * @return List<ProductPage>
     * @author Lin
     * @description TODO Query data of a product pages from database.
     *               The result is not up-to-date
     * @date 16.Aug.2023 016 02:28
     */
    public List<ProductPage> queryById(List<String> ids) {
        List<ProductPage> pages = new ArrayList<>();
        for (String id: ids) {
            ProductPage page = new ProductPage();
            Configure configure = this.configureMapper.selectById(id);
            Offer offer = this.offerMapper.selectById(id);
            List<Competitor> sellers = this.competitorMapper.findAllCompetitorByProductId(id);
            configure.getStrategy().sort(sellers);
            page.setCompetitors(sellers);
            page.setId(offer.getProductKey());
            page.setCode(offer.getId());
            page.setProductName(offer.getProductName());
            pages.add(page);
        }
        return pages;
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
        return null;
    }

}
