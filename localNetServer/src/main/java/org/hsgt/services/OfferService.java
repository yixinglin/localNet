package org.hsgt.services;

import com.baomidou.mybatisplus.extension.service.IService;
import org.hsgt.entities.pricing.Offer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public interface OfferService extends IService<Offer> {
}
