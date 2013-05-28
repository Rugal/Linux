package rugal.food.action;

import com.opensymphony.xwork2.ActionSupport;
import org.springframework.stereotype.Controller;

@Controller
public class UserAction extends ActionSupport {
    private static final long serialVersionUID = -1417237614181805435L;

    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 跳转到登录界面
     * @return
     */
    public String login_input() {
        return SUCCESS;
    }

    /**
     * 登录
     * @return
     */
    public String login() {
        System.out.println("name->" + name);
        System.out.println("password->" + password);

        return SUCCESS;
    }
}
