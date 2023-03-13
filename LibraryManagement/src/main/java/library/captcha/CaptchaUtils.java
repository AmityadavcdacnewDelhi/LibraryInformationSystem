package library.captcha;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import nl.captcha.Captcha;

public class CaptchaUtils {
	
	public static String encodeBase64(Captcha captcha)
	{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			ImageIO.write(captcha.getImage(),"png",outputStream);
			return DatatypeConverter.printBase64Binary(outputStream.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return null;
		
	}

}
