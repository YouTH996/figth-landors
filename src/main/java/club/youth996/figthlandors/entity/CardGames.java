package club.youth996.figthlandors.entity;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 1、牌局组对象
 * 2、在Tomcat服务器开启时，就新建一个牌局组对象，直到Tomcat服务器关闭
 * 3、一个牌局组对象包括牌局的标识符以及一个牌局对象
 * 
 * @author Lzx
 *
 */
@Slf4j
public class CardGames {
	private static Integer sign = 0;
	private static Map<Integer, CardGame> cardGames = new HashMap<Integer, CardGame>();
	
	public CardGames() {
		super();
	}
	
	public static synchronized void creatCardGame( User user ) {
		sign = sign + 1;
		user.setCardGameID(sign);
		CardGame x = new CardGame();
		x.setId(sign);
		cardGames.put(sign,x);
		log.info("cardGames.size():" + cardGames.size());
		log.info("新建" + x.getId());
	}
	
	public static Integer getSign() {
		return sign;
	}

	public static void setSign(Integer sign) {
		CardGames.sign = sign;
	}


	public Map<Integer, CardGame> getCardGames() {
		return cardGames;
	}

}
