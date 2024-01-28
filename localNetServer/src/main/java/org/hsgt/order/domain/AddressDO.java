package org.hsgt.order.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_address")
public class AddressDO {
    @TableId(type = IdType.AUTO)
    Long id;
    Integer type;
    String firstName;
    String lastName;
    String companyName;
    String supplementField;
    String streetName;
    String houseNumber;
    String zipCode;
    String city;
    String stateOrProvince;
    String country;
    String countryCode;
    String taxNumber;
    String companyVat;
    String email;
    String phone;
    String mobile;

}
