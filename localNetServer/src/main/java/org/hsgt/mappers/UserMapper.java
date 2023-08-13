package org.hsgt.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.hsgt.entities.common.User;

@Mapper
public interface UserMapper extends BaseMapperV2<User> {

    public User selectByName(String username);
    public User selectByToken(String token);

    public String selectTokenById(User user);
    public int updateTokenById(String userId, String token);


}
