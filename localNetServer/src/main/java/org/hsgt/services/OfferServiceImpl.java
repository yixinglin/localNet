package org.hsgt.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hsgt.entities.pricing.Offer;
import org.hsgt.mappers.OfferMapper;
import org.springframework.stereotype.Service;

@Service
public class OfferServiceImpl extends ServiceImpl<OfferMapper, Offer> implements OfferService {

}
