package springcloud.mapper;

import com.mengxuegu.springcloud.entities.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * create by biji.zhao on 2020/10/18
 */
@Mapper
public interface ProductMapper {
    Product findById(Long pid);

    List<Product> findAll();

    boolean addProduct(Product product);
}
