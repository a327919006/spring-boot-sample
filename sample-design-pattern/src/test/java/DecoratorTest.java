import com.cn.boot.sample.design.pattern.decorator.BigBike;
import com.cn.boot.sample.design.pattern.decorator.Bike;
import com.cn.boot.sample.design.pattern.decorator.ColorDecorator;
import com.cn.boot.sample.design.pattern.decorator.SuonaDecorator;
import org.junit.Test;

/**
 * @author Chen Nan
 */
public class DecoratorTest {

    @Test
    public void test() {
        Bike bike = new BigBike();

        bike = new ColorDecorator(bike);
        bike = new SuonaDecorator(bike);
        bike = new SuonaDecorator(bike);

        System.out.println(bike.getDescribe());
        System.out.println(bike.getPrice());
    }
}
