package  com.study.login;

import com.study.login.vo.UserVO;

public interface ILoginService {
	
	public UserVO getUser(String id);
	
}
