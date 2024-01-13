package org.hsgt.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.hsgt.core.domain.User;

@Mapper
public interface UserMapper extends BaseMapperV2<User> {

    User selectByName(String username);
     User selectByToken(String token);

     String selectTokenById(User user);
     int updateTokenById(String userId, String token);


}
