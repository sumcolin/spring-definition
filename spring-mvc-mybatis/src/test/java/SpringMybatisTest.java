import com.ssm.demo.pojo.Account;
import com.ssm.demo.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @auth 邹新
 * @email 741779841@qq.com
 * @date 2021/6/30
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
public class SpringMybatisTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void testSpringMybatis() throws Exception {
        List<Account> accounts = accountService.queryAccountList();
        accounts.forEach(v->{
            System.out.println(v.toString());
        });
    }
}
