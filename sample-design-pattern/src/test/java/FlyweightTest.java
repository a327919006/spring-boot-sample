import com.cn.boot.sample.design.pattern.flyweight.Company;
import com.cn.boot.sample.design.pattern.flyweight.WebSide;
import com.cn.boot.sample.design.pattern.flyweight.WebSideFactory;
import org.junit.Test;

/**
 * @author Chen Nan
 */
public class FlyweightTest {

    @Test
    public void test() {
        WebSideFactory factory = new WebSideFactory();

        WebSide webSide1 = factory.getWebSize("电商");
        webSide1.run(new Company("阿里巴巴"));

        WebSide webSide2 = factory.getWebSize("电商");
        webSide2.run(new Company("京东"));

        WebSide webSide3 = factory.getWebSize("招聘");
        webSide3.run(new Company("智联"));

        WebSide webSide4 = factory.getWebSize("招聘");
        webSide4.run(new Company("BOSS"));

        System.out.println(factory.getSize());
    }
}
