import com.cn.boot.sample.design.pattern.adapter.clazz.BusinessAdapter;
import com.cn.boot.sample.design.pattern.adapter.clazz.NewBusiness;
import com.cn.boot.sample.design.pattern.adapter.interfaces.BusinessPay;
import org.junit.Test;

/**
 * 适配器模式
 *
 * @author Chen Nan
 */
public class AdapterTest {

    /**
     * 基于接口的适配器
     */
    @Test
    public void testInterface() {
        BusinessPay pay = new BusinessPay();
        pay.createOrder();
        pay.pay();
    }

    /**
     * 基于类的适配器
     */
    @Test
    public void testClass() {
        NewBusiness business = new BusinessAdapter();

        business.businessA();
        business.businessB();
        business.businessC();
    }
}
