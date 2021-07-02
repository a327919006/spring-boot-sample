import com.cn.boot.sample.design.pattern.strategy.DiscountStrategy;
import com.cn.boot.sample.design.pattern.strategy.NormalStrategy;
import com.cn.boot.sample.design.pattern.strategy.PriceContext;
import org.junit.Test;

/**
 * @author Chen Nan
 */
public class StrategyTest {

    @Test
    public void test() {
        PriceContext normal = new PriceContext(new NormalStrategy());
        double normalPrice = normal.execute(100);
        System.out.println(normalPrice);

        PriceContext discount = new PriceContext(new DiscountStrategy());
        double discountPrice = discount.execute(100);
        System.out.println(discountPrice);
    }
}
