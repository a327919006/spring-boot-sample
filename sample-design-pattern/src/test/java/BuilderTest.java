import com.cn.boot.sample.design.pattern.builder.Computer;
import com.cn.boot.sample.design.pattern.builder.Director;
import com.cn.boot.sample.design.pattern.builder.HighComputerBuilder;
import org.junit.Test;

/**
 * 建造者设计模式
 *
 * @author Chen Nan
 */
public class BuilderTest {

    @Test
    public void test() {
        Computer computer = Director.createComputer(new HighComputerBuilder());
        System.out.println(computer);
    }
}
