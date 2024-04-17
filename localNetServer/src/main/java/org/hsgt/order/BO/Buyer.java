package org.hsgt.order.BO;

import lombok.Data;
import org.hsgt.order.domain.BuyerDO;

@Data
public class Buyer {
    private String id;
    private String email;
    private String phone;
    private String mobile;

    public static Buyer converToBO(BuyerDO item) {
        if (item == null) {
            return null;
        }
        Buyer result = new Buyer();
        result.setId(item.getCode());
        result.setEmail(item.getEmail());
        result.setPhone(item.getPhone());
        result.setMobile(item.getMobile());
        return result;
    }
}
