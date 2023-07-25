package org.hsgt.controllers;
import org.hsgt.entities.pricing.Offer;
import org.hsgt.mappers.OfferMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/offer")
public class OfferController {

    @Autowired
    private OfferMapper offerMapper;

    @GetMapping("/selectAll")
    public List<Offer> selectAll() {
        List<Offer> offers = offerMapper.selectList(null);
        // List<Offer> offers = offerMapper.findAllOffers();
       //System.out.println(offers);
        return offers;
    }

}
