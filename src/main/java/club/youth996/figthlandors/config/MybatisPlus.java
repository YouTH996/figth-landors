package club.youth996.figthlandors.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Zhan Xinjian
 * @date 2020/3/31 20:01
 * <p>
 *     Mybatis Plus分页拦截器
 * </p>
 */
@Configuration
public class MybatisPlus {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }
}
