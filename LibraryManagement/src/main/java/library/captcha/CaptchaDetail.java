package library.captcha;

import java.io.Serializable;


import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.WebUtils;

import com.wf.captcha.base.Captcha;

public class CaptchaDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String answer;
	private final Captcha captcha;
	
	public CaptchaDetail(HttpServletRequest request)
	{
		this.answer= request.getParameter("captcha");
		this.captcha=(Captcha)WebUtils.getSessionAttribute(request, "captcha");
	}
	
	
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/**
	 * @return the answer
	 */
	public String getAnswer() {
		return answer;
	}
	/**
	 * @return the captcha
	 */
	public Captcha getCaptcha() {
		return captcha;
	}

}
