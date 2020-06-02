package club.youth996.figthlandors.util;


import club.youth996.figthlandors.entity.Card;
import club.youth996.figthlandors.entity.CardGame;
import club.youth996.figthlandors.entity.CardGroup;
import club.youth996.figthlandors.entity.CardType;

import java.util.*;

/**
 * 1、用于整个游戏过程中的各种规则的判断
 * 2、用于整个游戏中人机出牌的判断
 * 
 * @author Lzx
 *
 */
public class CardPlay {
	/**
	 * 该方法用于开始游戏的初始化
	 * 
	 * @param cardGame 牌局对象
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> begin(CardGame cardGame) {
		CardPlay.cardInit(cardGame);
		List<Card> playerList = cardGame.getPlayerList()[1];
		List<Card> lordList = cardGame.getLordList();
		Map<String, Object> map = new HashMap<>(16);
		map.put("playerListId", playerList);
		map.put("lordListId", lordList);
		return map;
	}

	/**
	 * 该方法用于牌局的初始化
	 * 
	 * @param cardGame 牌局对象
	 * @return void
	 */
	public static void cardInit(CardGame cardGame) {
		int count = 1;
		// 初始化牌
		int color = 5;
		int value = 13;
		for (int i = 1; i <= color; i++) {
			for (int j = 1; j <= value; j++) {
				if ((i == 5) && (j > 2)) {
					break;
				} else {
					cardGame.getCard()[count] = new Card(i + "-" + j);
					count++;
				}
			}
		}
		// 打乱顺序
		int cnt = 100;
		for (int i = 0; i < cnt; i++) {
			Random random = new Random();
			int randomNumber1 = random.nextInt(54) + 1;
			int randomNumber2 = random.nextInt(54) + 1;
			Card card = cardGame.getCard()[randomNumber1];
			cardGame.getCard()[randomNumber1] = cardGame.getCard()[randomNumber2];
			cardGame.getCard()[randomNumber2] = card;
		}
		// 开始发牌
		int role = 3;
		for (int i = 0; i < role; i++) {
			// 初始化对应玩家牌
			cardGame.getPlayerList()[i] = new ArrayList<Card>();
		}
		int roleFlag = 0;
		// 发完牌排序，从大到小
		Card card;
		int cardNumber = 54;
		for (int i = 1; i <= cardNumber; i++) {
			// 分地主牌
			if (i >= 52) {
				card = cardGame.getCard()[i];
				cardGame.getLordList().add(card);
				continue;
			}
			switch ((++roleFlag) % 3) {
			// 左边玩家
			case 0:
				card = cardGame.getCard()[i];
				cardGame.getPlayerList()[0].add(card);
				break;
			// 我
			case 1:
				card = cardGame.getCard()[i];
				cardGame.getPlayerList()[1].add(card);
				break;
			// 右边玩家
			case 2:
				card = cardGame.getCard()[i];
				cardGame.getPlayerList()[2].add(card);
				break;
			default:
			}
		}
		for (int i = 0; i < role; i++) {
			List<Card> playerList = cardGame.getPlayerList()[i];
			order(playerList);
		}
	}

	/**
	 * 该方法用于对用户的牌进行排序，按照花色的不同，值的大小不同进行不同的比较
	 * 
	 * @param list 用户的手牌
	 * @return void
	 */
	public static void order(List<Card> list) {
		Collections.sort(list, new Comparator<Card>() {
			@Override
			public int compare(Card card1, Card card2) {
				// TODO Auto-generated method stub
				String subString;
				// 获取两幅牌的花色
				subString = card1.getName().substring(0, 1);
				int color1 = Integer.parseInt(subString);
				subString = card2.getName().substring(0, 1);
				int color2 = Integer.parseInt(subString);
				// 获取两幅牌的数值
				subString = card1.getName().substring(2, card1.getName().length());
				int value1 = Integer.parseInt(subString);
				subString = card2.getName().substring(2, card2.getName().length());
				int value2 = Integer.parseInt(subString);

				int flag = 0;
				// 如果是王的话
				int kingColor = 5;
				int kingValue = 1;
				if (color1 == kingColor) {
					value1 += 100;
				}
				if (color1 == kingColor && value1 == kingValue) {
					value1 += 50;
				}
				if (color2 == kingColor) {
					value2 += 100;
				}
				if (color2 == kingColor && value2 == kingValue) {
					value2 += 50;
				}
				// 如果是A或者2
				int aValue = 1;
				int bValue = 2;
				if (value1 == aValue) {
					value1 += 20;
				}
				if (value2 == aValue) {
					value2 += 20;
				}
				if (value1 == bValue) {
					value1 += 30;
				}
				if (value2 == bValue) {
					value2 += 30;
				}
				flag = value2 - value1;
				if (flag == 0) {
					return color2 - color1;
				} else {
					return flag;
				}
			}
		});
	}

	/**
	 * 该方法用于判断用户所选择的牌的牌型
	 * 
	 * @param cardList 用户所选择的牌
	 * @return CardType
	 */
	public static CardType jugdeType(List<Card> cardList) {
		// 因为之前排序过所以比较好判断
		int len = cardList.size();
		// 单牌,对子，3不带，4个一样炸弹
		int cardTypeLen = 4;
		// 如果第一个和最后个相同，说明全部相同
		if (len <= cardTypeLen) {
			if (len > 0 && CardPlay.getValue(cardList.get(0)) == CardPlay.getValue(cardList.get(len - 1))) {
				switch (len) {
				case 1:
					return CardType.c1;
				case 2:
					return CardType.c2;
				case 3:
					return CardType.c3;
				case 4:
					return CardType.c4;
				default:
				}
			}
			// 双王,化为对子返回
			int cardTypeLen1 = 2;
			int kingColor = 5;
			if (len == cardTypeLen1 && CardPlay.getColor(cardList.get(1)) == kingColor) {
				return CardType.c2;
			}
			// 当第一个和最后个不同时,3带1
			int cardTypeLen2 = 4;
			int end = 2;
			boolean existed = (len == cardTypeLen2
					&& ((CardPlay.getValue(cardList.get(0)) == CardPlay.getValue(cardList.get(len - end)))
							|| CardPlay.getValue(cardList.get(1)) == CardPlay.getValue(cardList.get(len - 1))));
			if (existed)
				return CardType.c31;
			else {
				return CardType.c0;
			}
		}
		// 当5张以上时，连字，3带2，飞机，2顺，4带2等等
		cardTypeLen = 5;
		// 现在按相同数字最大出现次数
		if (len >= cardTypeLen) {
			return jugdeType2(cardList);
		}
		return CardType.c0;
	}

	/**
	 * 该方法用于判断当用户所选择的牌超过4张后的牌型
	 * 
	 * @param cardList 用户所选择的牌
	 * @return CardType
	 */
	public static CardType jugdeType2(List<Card> cardList) {
		int len = cardList.size();
		@SuppressWarnings("rawtypes")
		List[] cardIndex = new ArrayList[4];
		int cardIndexSize = 4;
		for (int i = 0; i < cardIndexSize; i++) {
			cardIndex[i] = new ArrayList<Integer>();
		}
		/*
		 * 求出各种数字出现频率 a[0,1,2,3]分别表示重复1,2,3,4次的牌
		 */
		CardPlay.getMax(cardIndex, cardList);
		// 3带2 -----必含重复3次的牌
		int repeat4 = 3;
		int repeat3 = 2;
		int repeat2 = 1;
		int repeat1 = 0;
		int cardTypeLen = 5;
		if (cardIndex[repeat3].size() == 1 && cardIndex[repeat2].size() == 1 && len == cardTypeLen) {
			return CardType.c32;
		}
		// 4带2(单,双)
		cardTypeLen = 6;
		if (cardIndex[repeat4].size() == 1 && len == cardTypeLen) {
			return CardType.c411;
		}
		cardTypeLen = 8;
		cardIndexSize = 2;
		if (cardIndex[repeat4].size() == 1 && cardIndex[repeat2].size() == cardIndexSize && len == cardTypeLen) {
			return CardType.c422;
		}
		// 单连,保证不存在王
		int kingColor = 5;
		if ((CardPlay.getColor(cardList.get(0)) != kingColor) && (cardIndex[repeat1].size() == len)
				&& (CardPlay.getValue(cardList.get(0)) - CardPlay.getValue(cardList.get(len - 1)) == len - 1)) {
			return CardType.c123;
		}
		int v2 = 2;
		int v3 = 3;
		int v4 = 4;
		int v5 = 5;
		// 连队
		if (cardIndex[repeat2].size() == len / v2 && len % v2 == 0 && len / v2 >= v3
				&& (CardPlay.getValue(cardList.get(0)) - CardPlay.getValue(cardList.get(len - 1)) == (len / 2 - 1))) {
			return CardType.c1122;
		}

		// 飞机
		if (cardIndex[repeat3].size() == len / v3 && (len % v3 == 0)
				&& (CardPlay.getValue(cardList.get(0)) - CardPlay.getValue(cardList.get(len - 1)) == (len / v3 - 1))) {
			return CardType.c111222;
		}

		// 飞机带n单,n/2对
		if (cardIndex[repeat3].size() == len / v4 && ((Integer) (cardIndex[repeat3].get(len / v4 - 1))
				- (Integer) (cardIndex[repeat3].get(0)) == len / 4 - 1)) {
			return CardType.c11122234;
		}

		// 飞机带n双
		if (cardIndex[repeat3].size() == len / v5 && cardIndex[repeat3].size() == len / v5
				&& ((Integer) (cardIndex[repeat3].get(len / 5 - 1)) - (Integer) (cardIndex[repeat3].get(0)) == len / 5
						- 1)) {
			return CardType.c11122234;
		}
		return CardType.c0;
	}

	/**
	 * 该方法用于检查用户所选的牌是否能够出
	 * 
	 * @param clickCard 用户当前所选的牌
	 * @param curent    当前已经出的牌
	 * @return int
	 */
	public static int checkCards(List<Card> clickCard, List<Card>[] current) {
		// 找出当前最大的牌是哪个电脑出的,c是点选的牌
		List<Card> currentlist = (current[0].size() > 0) ? current[0] : current[2];
		CardType clickCardType = jugdeType(clickCard);
		// 如果对方出的不是炸弹 我们都可以出炸弹
		int kingColor = 5;
		boolean clicKingJudge = (clickCardType == CardType.c4
				|| (clickCardType == CardType.c2 && getColor(clickCard.get(0)) == kingColor));
		boolean currentlistKingJudge = (jugdeType(currentlist) != CardType.c4);
		boolean existed = clicKingJudge && currentlistKingJudge;
		if (existed) {
			return 1;
		}

		// 如果张数不同直接过滤
		if (clickCardType != CardType.c4 && clickCard.size() != currentlist.size()) {
			return 0;
		}
		// 比较我的出牌类型
		if (jugdeType(clickCard) != jugdeType(currentlist)) {
			return 0;
		}

		// 比较出的牌是否要大
		// 王炸弹
		int kingBombSize = 2;
		if (clickCardType == CardType.c4) {
			if (clickCard.size() == kingBombSize) {
				return 1;
			}
			if (currentlist.size() == kingBombSize) {
				return 0;
			}
		}
		// 单牌,对子,3带,4炸弹
		if (clickCardType == CardType.c1 || clickCardType == CardType.c2 || clickCardType == CardType.c3
				|| clickCardType == CardType.c4) {
			if (getValue(clickCard.get(0)) <= getValue(currentlist.get(0))) {
				return 0;
			} else {
				return 1;
			}
		}
		// 顺子,连队，飞机裸
		if (clickCardType == CardType.c123 || clickCardType == CardType.c1122 || clickCardType == CardType.c111222) {
			if (getValue(clickCard.get(0)) <= getValue(currentlist.get(0))) {
				return 0;
			} else {
				return 1;
			}
		}
		// 按重复多少排序
		// 3带1,3带2 ,飞机带单，双,4带1,2,只需比较第一个就行，独一无二的
		if (clickCardType == CardType.c31 || clickCardType == CardType.c32 || clickCardType == CardType.c411
				|| clickCardType == CardType.c422 || clickCardType == CardType.c11122234
				|| clickCardType == CardType.c1112223344) {
			if (getValue(clickCard.get(0)) < getValue(currentlist.get(0))) {
				return 0;
			}
		}
		return 1;
	}

	/**
	 * 该方法用于获取牌的花色
	 * 
	 * @param card 牌对象
	 * @return int
	 */
	public static int getColor(Card card) {
		return Integer.parseInt(card.getName().substring(0, 1));
	}

	/**
	 * 该方法用于获取牌的值
	 * 
	 * @param card 牌对象
	 * @return int
	 */
	public static int getValue(Card card) {
		String subString = card.getName().substring(2, card.getName().length());
		int value = Integer.parseInt(subString);
		String substring2 = card.getName().substring(2, card.getName().length());
		String value1 = "1";
		String value2 = "2";
		int kingColor = 5;
		// 如果是2
		if (value2.equals(substring2)) {
			value += 13;
		}
		// 如果是A
		if (value1.equals(substring2)) {
			value += 13;
		}
		// 如果是王
		if (CardPlay.getColor(card) == kingColor) {
			value += 2;
		}
		return value;
	}

	/**
	 * 该方法用于获取牌的值
	 * 
	 * @param card 牌的名称
	 * @return int
	 */
	public static int getValue(String card) {
		String[] name = card.split(",");
		String na = name[0];
		int value = Integer.parseInt(na.substring(2, na.length()));
		String kingColor = "5";
		if (na.substring(0, 1).equals(kingColor)) {
			value += 3;
		}
		String substring = na.substring(2, na.length());
		String str1 = "1";
		String str2 = "2";
		if (str1.equals(substring) || str2.equals(substring)) {
			value += 13;
		}
		return value;
	}

	/**
	 * 该方法用于计算传入的牌各个牌值的最大重复次数
	 * 
	 * @param cardIndex 存储最大重复次数的数组
	 * @param list      传入的手牌
	 * @return void
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void getMax(List[] cardIndex, List<Card> list) {
		// 1-13各算一种,王算第14种
		int[] count = new int[14];
		int cardTypeSize = 14;
		for (int i = 0; i < cardTypeSize; i++) {
			count[i] = 0;
		}
		int len = list.size();
		for (int i = 0; i < len; i++) {
			if (CardPlay.getColor(list.get(i)) == 5)
				count[13]++;
			else {
				int value = CardPlay.getValue(list.get(i)) - 3;
				count[value]++;
			}
		}
		for (int i = 0; i < cardTypeSize; i++) {
			switch (count[i]) {
			case 1:
				cardIndex[0].add(i + 1);
				break;
			case 2:
				cardIndex[1].add(i + 1);
				break;
			case 3:
				cardIndex[2].add(i + 1);
				break;
			case 4:
				cardIndex[3].add(i + 1);
				break;
			default:
			}
		}
	}

	/**
	 * 该方法用于拆分人机的手牌
	 * 
	 * @param cardList 一组手牌
	 * @return CardGroup
	 */
	public static CardGroup getCardGroup(List<Card> cardList) {
		// 先复制一个list
		List<Card> cloneCardList = new ArrayList<Card>(cardList);
		CardGroup cardGroup = new CardGroup();
		// 拆炸弹
		getBomb(cloneCardList, cardGroup);
		// 拆3带
		getThreeZones(cloneCardList, cardGroup);
		// 拆飞机
		getPlane(cloneCardList, cardGroup);
		// 拆对
		getPair(cloneCardList, cardGroup);
		// 拆连队
		getEvenPair(cloneCardList, cardGroup);
		// 拆顺子
		getStraight(cloneCardList, cardGroup);
		// 拆单
		getSingle(cloneCardList, cardGroup);
		return cardGroup;
	}

	/**
	 * 该方法用于拆分出手牌中的炸弹
	 * 
	 * @param cardList  一组手牌
	 * @param cardGroup 牌型组
	 * @return void
	 */
	public static void getBomb(List<Card> cardList, CardGroup cardGroup) {
		// TODO Auto-generated method stub
		// 要删除的牌
		List<Card> del = new ArrayList<Card>();
		// 先判断王炸
		int kingBombSize = 2;
		int kingColor = 5;
		if (cardList.size() == kingBombSize && getColor(cardList.get(0)) == kingColor
				&& getColor(cardList.get(1)) == kingColor) {
			cardGroup.bomb.add(cardList.get(0).getName() + "," + cardList.get(1).getName());
			del.add(cardList.get(0));
			del.add(cardList.get(1));
		}
		// 如果王不成炸 就为单牌
		if (cardList.size() >= kingBombSize && getColor(cardList.get(0)) == kingColor
				&& getColor(cardList.get(1)) != kingColor) {
			cardGroup.single.add(cardList.get(0).getName());
			del.add(cardList.get(0));
		}
		cardList.removeAll(del);
		// 遍历list 判断一般炸弹 连续4张一样的
		int len = cardList.size();
		for (int i = 0; i < len; i++) {
			if (i + 3 >= len) {
				break;
			}
			int bombSize = 3;
			if (getValue(cardList.get(i)) == getValue(cardList.get(i + 3))) {
				String add = cardList.get(i).getName() + ",";
				add += cardList.get(i + 1).getName() + ",";
				add += cardList.get(i + 2).getName() + ",";
				add += cardList.get(i + 3).getName();
				cardGroup.bomb.add(add);
				for (int j = i; j <= i + bombSize; j++) {
					del.add(cardList.get(j));
				}
				i = i + 3;
			}
		}
		cardList.removeAll(del);
	}

	/**
	 * 该方法用于拆分出手牌中的3带
	 * 
	 * @param cardList  一组手牌
	 * @param cardGroup 牌型组
	 * @return void
	 */
	public static void getThreeZones(List<Card> cardList, CardGroup cardGroup) {
		// TODO Auto-generated method stub
		// 要删除的牌
		List<Card> del = new ArrayList<Card>();
		int len = cardList.size();
		for (int i = 0; i < len; i++) {
			if (i + 2 >= len) {
				break;
			}
			int threeZonesSize = 2;
			if (getValue(cardList.get(i)) == getValue(cardList.get(i + 2))) {
				String add = cardList.get(i).getName() + ",";
				add += cardList.get(i + 1).getName() + ",";
				add += cardList.get(i + 2).getName();
				cardGroup.threeZones.add(add);
				for (int j = i; j <= i + threeZonesSize; j++) {
					del.add(cardList.get(j));
				}
				i = i + 2;
			}
		}
		cardList.removeAll(del);
	}

	/**
	 * 该方法用于拆分出手牌中的飞机
	 * 
	 * @param cardList  一组手牌
	 * @param cardGroup 牌型组
	 * @return void
	 */
	public static void getPlane(List<Card> cardList, CardGroup cardGroup) {
		// TODO Auto-generated method stub
		// 要删除的牌
		List<Card> del = new ArrayList<Card>();
		// 从3带里找 连续两个飞机才符合条件
		List<String> threeZones = cardGroup.threeZones;
		int threeZonesSize = threeZones.size();
		int len = 2;
		if (threeZonesSize < len) {
			// 手牌中少于两个3带 则不存在飞机
			return;
		}
		Integer[] value = new Integer[len];
		for (int i = 0; i < len; i++) {
			String[] name = threeZones.get(i).split(",");
			value[i] = Integer.parseInt(name[0].substring(2, name[0].length()));
		}
		for (int i = 0; i < len; i++) {
			int end = i;
			for (int j = i; j < len; j++) {
				if (value[j] == 2) {
					// 2不参与飞机
					continue;
				}
				if (value[j] == 1) {
					// 1后面是13 所以值加上13
					value[j] += 13;
				}
				if (value[i] - value[j] == j - i) {
					end = j;
				}
			}
			// 说明从 i 到 end 为飞机
			if (end != i) {
				String add = "";
				for (int j = i; j < end; j++) {
					add += threeZones.get(j) + ",";
					del.add(new Card(threeZones.get(end)));
				}
				add += threeZones.get(end);
				del.add(new Card(threeZones.get(end)));
				cardGroup.plane.add(add);
				i = end;
			}
		}
		cardList.removeAll(del);
	}

	/**
	 * 该方法用于拆分出手牌中的对子
	 * 
	 * @param cardList  一组手牌
	 * @param cardGroup 牌型组
	 * @return void
	 */
	public static void getPair(List<Card> cardList, CardGroup cardGroup) {
		// TODO Auto-generated method stub
		// 要删除的牌
		List<Card> del = new ArrayList<Card>();
		int len = cardList.size();
		for (int i = 0; i < len; i++) {
			if (i + 1 < len && getValue(cardList.get(i)) == getValue(cardList.get(i + 1))) {
				String add = cardList.get(i).getName() + ",";
				add += cardList.get(i + 1).getName();
				cardGroup.pair.add(add);
				del.add(cardList.get(i));
				del.add(cardList.get(i + 1));
				i = i + 1;
			}
		}
		cardList.removeAll(del);
	}

	/**
	 * 该方法用于拆分出手牌中的连对
	 * 
	 * @param cardList  一组手牌
	 * @param cardGroup 牌型组
	 * @return void
	 */
	public static void getEvenPair(List<Card> cardList, CardGroup cardGroup) {
		// TODO Auto-generated method stub
		// 要删除的牌
		List<Card> del = new ArrayList<Card>();
		// 从对子里找连对 连续三对才符合条件
		List<String> pair = cardGroup.pair;
		int pairSize = pair.size();
		int len = 3;
		if (pairSize < len) {
			return;
		}
		Integer[] value = new Integer[len];
		for (int i = 0; i < len; i++) {
			String[] name = pair.get(i).split(",");
			value[i] = Integer.parseInt(name[0].substring(2, name[0].length()));
		}
		for (int i = 0; i < len; i++) {
			int end = i;
			for (int j = i; j < len; j++) {
				if (value[j] == 2) {
					// 2不参与连对
					continue;
				}
				if (value[j] == 1) {
					// 1后面是13 所以值加上13
					value[j] += 13;
				}
				if (value[i] - value[j] == j - i) {
					end = j;
				}
			}
			if (end - i >= 2) {
				String add = "";
				for (int j = i; j < end; j++) {
					add += pair.get(j) + ",";
					del.add(new Card(pair.get(j)));
				}
				add += pair.get(end);
				del.add(new Card(pair.get(end)));
				cardGroup.evenPair.add(add);
				i = end;
			}
		}
		cardList.removeAll(del);
	}

	/**
	 * 该方法用于拆分出手牌中的顺子
	 * 
	 * @param cardList  一组手牌
	 * @param cardGroup 牌型组
	 * @return void
	 */
	public static void getStraight(List<Card> cardList, CardGroup cardGroup) {
		// TODO Auto-generated method stub
		// 要删除的牌
		List<Card> del = new ArrayList<Card>();
		int len = cardList.size();
		// 如果最大的牌小于7 或者最小的牌大于10 或者剩余牌的数量小于5 则不存在顺子
		int maxCardValue = 7;
		int minCardValue = 10;
		boolean valueJudge = (getValue(cardList.get(0)) < maxCardValue
				|| getValue(cardList.get(len - 1)) > minCardValue);
		boolean existed = (len > 0 && valueJudge);
		if (existed) {
			return;
		}
		int evenPairSize = 5;
		if (len < evenPairSize) {
			return;
		}

		for (int i = 0; i < len; i++) {
			int end = i;
			for (int j = i; j < len; j++) {
				if (getValue(cardList.get(i)) - getValue(cardList.get(j)) == j - i) {
					end = j;
				}
			}
			// 说明从i到end为顺子
			if (end - i >= 4) {
				String add = "";
				for (int j = i; j < end; j++) {
					add += cardList.get(j) + ",";
					del.add(cardList.get(j));
				}
				add += cardList.get(end);
				del.add(cardList.get(end));
				cardGroup.evenPair.add(add);
				i = end;
			}
		}
		cardList.removeAll(del);
	}

	/**
	 * 该方法用于拆分出手牌中的单牌
	 * 
	 * @param cardList  一组手牌
	 * @param cardGroup 牌型组
	 * @return void
	 */
	public static void getSingle(List<Card> cardList, CardGroup cardGroup) {
		// 要删除的牌
		List<Card> del = new ArrayList<Card>();
		int len = cardList.size();
		for (int i = 0; i < len; i++) {
			cardGroup.single.add(cardList.get(i).getName());
			del.add(cardList.get(i));
		}
		cardList.removeAll(del);
	}

	/**
	 * 该方法用于计算人机手牌的权重 大小王，A，2的数量越多，权重越大
	 * 
	 * @param cardList 一组手牌
	 * @return int
	 */
	public static int getScore(List<Card> cardList) {
		int count = 0;
		int len = cardList.size();
		for (int i = 0; i < len; i++) {
			Card card = cardList.get(i);
			if (card.getName().substring(0, 1).equals("5")) {
				count += 5;
			}
			if (card.getName().substring(2, card.getName().length()).equals("2")) {
				count += 2;
			}
			if (card.getName().substring(2, card.getName().length()).equals("1")) {
				count += 1;
			}
		}
		return count;
	}

	/**
	 * 该方法通过用户是否抢地主，判断人机是否需要抢地主 如果用户抢地主，则地主为用户 如果用户没有抢地主，则通过比较人机手牌的权重，权重大的获得地主
	 * 
	 * @param flag     用户是否有抢地主，1为抢地主，0为没有抢地主
	 * @param cardGame 牌局对象
	 * @return void
	 */
	public static void jugdeLord(int flag, CardGame cardGame) {
		List<Card> lordList = cardGame.getLordList();
		int role0 = 0;
		int role1 = 1;
		int role2 = 2;
		// 我有抢地主 则地主就是我
		if (flag == 1) {
			cardGame.setLandLordsFlag(1);
			List<Card>[] playerList = cardGame.getPlayerList();
			playerList[role1].addAll(lordList);
			order(cardGame.getPlayerList()[role1]);
		}
		// 我没有抢地主 电脑通过牌的权重大小选择是否抢地主
		else {
			if (getScore(cardGame.getPlayerList()[role0]) > getScore(cardGame.getPlayerList()[role2])) {
				cardGame.setLandLordsFlag(0);
				cardGame.getPlayerList()[role0].addAll(lordList);
				order(cardGame.getPlayerList()[role0]);
			} else {
				cardGame.setLandLordsFlag(2);
				cardGame.getPlayerList()[role2].addAll(lordList);
				order(cardGame.getPlayerList()[role2]);
			}
		}
	}

	/**
	 * 该方法用于人机自动出牌的计算
	 * 
	 * @param role     当前出牌的人机角色
	 * @param cardGame 牌局对象
	 * @return List<String>
	 */
	public static List<String> comptuerShowCards(int role, CardGame cardGame) {
		CardGroup cardGroup = getCardGroup(cardGame.getPlayerList()[role]);

		List<String> satyOut = new ArrayList<String>();
		int add1 = 1;
		int add2 = 2;
		int roleSize = 3;
		// 人机主动出牌
		if (cardGame.getFlag()[(role + add1) % roleSize] == false
				&& cardGame.getFlag()[(role + add2) % roleSize] == false) {
			activePlay(role, cardGame, cardGroup, satyOut);
		}
		// 人机跟牌
		else {
			followPlay(role, cardGame, cardGroup, satyOut);
		}

//		if (!satyOut.isEmpty())
//			System.out.println("satyOut.get(0)   " + satyOut.get(0));
//		else
//			System.out.println("satyOut.get(0)为空");

		if (!satyOut.isEmpty()) {
			List<Card> del = JsonChange.toCard(satyOut);
			cardGame.getPlayerList()[role].removeAll(del);
		}

		return satyOut;
	}

	/**
	 * 该方法用于人机主动出牌时的计算
	 * 
	 * @param role      当前出牌的人机角色
	 * @param cardGame  牌局对象
	 * @param cardGroup 人机手牌拆分后的牌组
	 * @param satyOut   人机即将要出的牌
	 * @return List<String>
	 */
	public static List<String> activePlay(int role, CardGame cardGame, CardGroup cardGroup, List<String> satyOut) {
		if (cardGroup.single.size() > (cardGroup.plane.size() + cardGroup.threeZones.size())) {
			// 有单出单 优先出最小的单 除去飞机 3带1带的单牌
			satyOut.add(cardGroup.single.get(cardGroup.single.size() - 1));
		} else if (cardGroup.pair.size() > (cardGroup.plane.size() + cardGroup.threeZones.size())) {
			// 有对出对 优先出最小的对 除去飞机 3带1带的对
			satyOut.add(cardGroup.pair.get(cardGroup.pair.size() - 1));
		} else if (cardGroup.straight.size() > 0) {
			// 有顺子出顺子
			satyOut.add(cardGroup.straight.get(cardGroup.straight.size() - 1));
		} else if (cardGroup.threeZones.size() > 0) {
			// 有3带出3带 能带就带，不能带就出光3
			satyOut.add(cardGroup.threeZones.get(cardGroup.threeZones.size() - 1));
			// 有单带单
			if (cardGroup.single.size() > 0) {
				satyOut.add(cardGroup.single.get(cardGroup.single.size() - 1));
			} else if (cardGroup.pair.size() > 0) {
				satyOut.add(cardGroup.pair.get(cardGroup.pair.size() - 1));
			}
		} else if (cardGroup.evenPair.size() > 0) {
			// 有双顺出双顺
			satyOut.add(cardGroup.evenPair.get(cardGroup.evenPair.size() - 1));
		} else if (cardGroup.plane.size() > 0) {
			// 有飞机出飞机
			String[] name = cardGroup.plane.get(cardGroup.plane.size() - 1).split(",");
			satyOut.add(cardGroup.plane.get(cardGroup.plane.size() - 1));
			int len = name.length / 3;
			if (len <= cardGroup.single.size()) {
				// 带单
				for (int i = 0; i < len; i++) {
					satyOut.add(cardGroup.single.get(i));
				}
			} else if (len <= cardGroup.pair.size()) {
				// 带双
				for (int i = 0; i < len; i++) {
					satyOut.add(cardGroup.pair.get(i));
				}
			}
		} else if (cardGroup.bomb.size() > 0) {
			// 前面都没有 那剩下的就是炸了咯
			satyOut.add(cardGroup.bomb.get(cardGroup.bomb.size() - 1));
			int beltSize = 2;
			if (cardGroup.single.size() >= beltSize) {
				// 带两单
				satyOut.add(cardGroup.single.get(cardGroup.single.size() - 1));
				satyOut.add(cardGroup.single.get(cardGroup.single.size() - 2));
			} else if (cardGroup.pair.size() >= beltSize) {
				// 带两双
				satyOut.add(cardGroup.pair.get(cardGroup.pair.size() - 1));
				satyOut.add(cardGroup.pair.get(cardGroup.pair.size() - 2));
			}
		}
		return satyOut;
	}

	/**
	 * 该方法用于人机跟牌时的计算
	 * 
	 * @param role      当前出牌的人机角色
	 * @param cardGame  牌局对象
	 * @param cardGroup 人机手牌拆分后的牌组
	 * @param satyOut   人机即将要出的牌
	 * @return List<String>
	 */
	public static List<String> followPlay(int role, CardGame cardGame, CardGroup cardGroup, List<String> satyOut) {
		// 最近出的牌
		List<Card> player = cardGame.getCurrentList()[(role + 2) % 3] != null
				? cardGame.getCurrentList()[(role + 2) % 3]
				: cardGame.getCurrentList()[(role + 1) % 3];
		// 判断该牌牌型
		CardType cType = jugdeType(player);
		int landLordsFlag = cardGame.getLandLordsFlag();
		// 判牌
		if (cType == CardType.c1) {
			// 单牌
			autoChoice1(cardGroup.single, player, satyOut, role, landLordsFlag);
		} else if (cType == CardType.c2) {
			// 对
			autoChoice1(cardGroup.pair, player, satyOut, role, landLordsFlag);
		} else if (cType == CardType.c3) {
			// 三不带
			autoChoice1(cardGroup.threeZones, player, satyOut, role, landLordsFlag);
		} else if (cType == CardType.c4) {
			// 炸弹
			autoChoice1(cardGroup.bomb, player, satyOut, role, landLordsFlag);
		} else if (cType == CardType.c31) {
			// 三带1
			autoChoice2(cardGroup.threeZones, cardGroup.single, player, satyOut, role);
		} else if (cType == CardType.c32) {
			// 三带2
			autoChoice2(cardGroup.threeZones, cardGroup.pair, player, satyOut, role);
		} else if (cType == CardType.c411) {
			// 炸弹带两单
			autoChoice5(cardGroup.bomb, cardGroup.single, player, satyOut, role);
		} else if (cType == CardType.c422) {
			// 炸弹带两对
			autoChoice5(cardGroup.bomb, cardGroup.pair, player, satyOut, role);
		} else if (cType == CardType.c123) {
			// 顺子
			autoChoice3(cardGroup.straight, player, satyOut, role);
		} else if (cType == CardType.c1122) {
			// 连对
			autoChoice3(cardGroup.evenPair, player, satyOut, role);
		} else if (cType == CardType.c11122234) {
			// 飞机带单
			autoChoice4(cardGroup.plane, cardGroup.single, player, satyOut, role);
		} else if (cType == CardType.c1112223344) {
			// 飞机带对
			autoChoice4(cardGroup.plane, cardGroup.pair, player, satyOut, role);
		}
		return satyOut;
	}

	/**
	 * 该方法用于人机跟牌时所需要进行的相关的计算 适用于单牌 对 单三 单四
	 * 
	 * @param cardGroup 人机手牌拆分后对应玩家出牌的牌组
	 * @param player    当前玩家出的牌
	 * @param list      人机准备回的牌
	 * @param role      当前出牌的人机角色
	 * @return void
	 */
	public static void autoChoice1(List<String> cardGroup, List<Card> player, List<String> list, int role,
			int landLordsFlag) {
		// 如果下一个出牌的是地主 则出最大的牌 否则从最小的能出的牌开始出
		int roleSize = 3;
		if ((role + 1) % roleSize == landLordsFlag) {
			int len = cardGroup.size();
			for (int i = 0; i < len; i++) {
				if (getValue(cardGroup.get(i)) > getValue(player.get(0))) {
					list.add(cardGroup.get(i));
					break;
				}
			}
		} else {
			int len = cardGroup.size();
			for (int i = len - 1; i >= 0; i--) {
				if (getValue(cardGroup.get(i)) > getValue(player.get(0))) {
					list.add(cardGroup.get(i));
					break;
				}
			}
		}
	}

	/**
	 * 该方法用于人机跟牌时所需要进行的相关的计算 适用于三带 四带
	 * 
	 * @param cardGroup1 人机手牌拆分后对应玩家出牌的主牌的牌组
	 * @param cardGroup2 人机手牌拆分后对应玩家出牌的带牌的牌组
	 * @param player     当前玩家出的牌
	 * @param list       人机准备回的牌
	 * @param role       当前出牌的人机角色
	 * @return void
	 */
	public static void autoChoice2(List<String> cardGroup1, List<String> cardGroup2, List<Card> player,
			List<String> list, int role) {
		int len1 = cardGroup1.size();
		int len2 = cardGroup2.size();
		int len = 10;
		if (len1 > 0 && cardGroup1.get(0).length() < len) {
			list.add(cardGroup1.get(0));
			return;
		}
		if (len1 < 1 || len2 < 1) {
			return;
		}
		for (int i = len1 - 1; i >= 0; i--) {
			if (getValue(cardGroup1.get(i)) > getValue(player.get(0))) {
				list.add(cardGroup1.get(i));
				break;
			}
		}
		list.add(cardGroup2.get(len2 - 1));
		len = 2;
		if (list.size() < len) {
			list.clear();
		}
	}

	/**
	 * 该方法用于人机跟牌时所需要进行的相关的计算 适用于顺子
	 * 
	 * @param cardGroup 人机手牌拆分后对应玩家出牌的牌组
	 * @param player    当前玩家出的牌
	 * @param list      人机准备回的牌
	 * @param role      当前出牌的人机角色
	 * @return void
	 */
	public static void autoChoice3(List<String> cardGroup, List<Card> player, List<String> list, int role) {
		int len = cardGroup.size();
		for (int i = 0; i < len; i++) {
			String[] s = cardGroup.get(i).split(",");
			int playerSize = player.size();
			if (s.length == playerSize && getValue(cardGroup.get(i)) > getValue(player.get(0))) {
				list.add(cardGroup.get(i));
				return;
			}
		}
	}

	/**
	 * 该方法用于人机跟牌时所需要进行的相关的计算 适用于飞机
	 * 
	 * @param cardGroup1 人机手牌拆分后对应玩家出牌的主牌的牌组
	 * @param cardGroup2 人机手牌拆分后对应玩家出牌的带牌的牌组
	 * @param player     当前玩家出的牌
	 * @param list       人机准备回的牌
	 * @param role       当前出牌的人机角色
	 * @return void
	 */
	public static void autoChoice4(List<String> cardGroup1, List<String> cardGroup2, List<Card> player,
			List<String> list, int role) {
		int len1 = cardGroup1.size();
		int len2 = cardGroup2.size();

		if (len1 < 1 || len2 < 1) {
			return;
		}

		for (int i = 0; i < len1; i++) {
			String[] s = cardGroup1.get(i).split(",");
			String[] s2 = cardGroup2.get(0).split(",");
			int sLen = s.length / 3;
			if ((sLen <= len2) && (s.length * (3 + s2.length) == player.size())
					&& getValue(cardGroup1.get(i)) > getValue(player.get(0))) {
				list.add(cardGroup1.get(i));
				for (int j = 1; j <= sLen; j++) {
					list.add(cardGroup2.get(len2 - j));
				}
			}
		}
	}

	/**
	 * 该方法用于人机跟牌时所需要进行的相关的计算 适用于4带 2单或者2双
	 * 
	 * @param cardGroup1 人机手牌拆分后对应玩家出牌的主牌的牌组
	 * @param cardGroup2 人机手牌拆分后对应玩家出牌的带牌的牌组
	 * @param player     当前玩家出的牌
	 * @param list       人机准备回的牌
	 * @param role       当前出牌的人机角色
	 * @return void
	 */
	public static void autoChoice5(List<String> cardGroup1, List<String> cardGroup2, List<Card> player,
			List<String> list, int role) {
		int len1 = cardGroup1.size();
		int len2 = cardGroup2.size();
		int beltSize = 2;
		if (len1 < 1 || len2 < beltSize) {
			return;
		}

		for (int i = 0; i < len1; i++) {
			if (getValue(cardGroup1.get(i)) > getValue(player.get(0))) {
				list.add(cardGroup1.get(i));
				for (int j = 1; j <= beltSize; j++) {
					list.add(cardGroup2.get(len2 - j));
				}
			}
		}
	}
}
