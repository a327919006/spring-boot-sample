import com.cn.boot.sample.design.pattern.singleton.SingletonHungry;
import com.cn.boot.sample.design.pattern.singleton.SingletonLazy;
import org.junit.Test;

/**
 * 单例模式
 *
 * @author Chen Nan
 */
public class SingletonTest {

    /**
     * 懒汉
     */
    @Test
    public void lazy() {
        SingletonLazy lazy = SingletonLazy.getInstance();
        lazy.business();
    }

    /**
     * 饿汉
     */
    @Test
    public void hungry() {
        SingletonHungry hungry = SingletonHungry.getInstance();
        hungry.business();
    }
}
