package cn.cambridge.hexohero.common.service;

import cn.cambridge.hexohero.basic.util.CommonResultUtil;
import cn.cambridge.hexohero.common.vo.UserDTO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoginService {
    private Logger logger = LoggerFactory.getLogger(LoginService.class);

    /**
     * 用户登录
     * @param user 用户名和密码信息
     * @return 登录状态（成功/失败）
     */
    public Map<String, Object> login(UserDTO user) {
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (Exception e) {
            return CommonResultUtil.returnFalse(CommonResultUtil.MessageCode.USERNAME_OR_PASSWORD_NOT_TRUE);
        }
        if (subject.isAuthenticated()) {
            logger.info("User '" + user.getUsername() + "' successfully logged in.");
            return CommonResultUtil.returnTrue("登陆成功");
        } else {
            return CommonResultUtil.returnFalse(CommonResultUtil.MessageCode.LOGIN_FAILED);
        }
    }

    /**
     * 用户退出
     * @return 退出状态（成功/失败）
     */
    public Map<String, Object> logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return CommonResultUtil.returnTrue("退出成功");
    }

}