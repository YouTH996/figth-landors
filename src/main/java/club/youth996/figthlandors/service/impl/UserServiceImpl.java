package club.youth996.figthlandors.service.impl;

import club.youth996.figthlandors.entity.User;
import club.youth996.figthlandors.mapper.UserMapper;
import club.youth996.figthlandors.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * user
 * @author Zhan Xinjian
 * @data 2020-04-17 05:59:57
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


}
