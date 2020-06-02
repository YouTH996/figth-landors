package club.youth996.figthlandors.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 1、牌局对象
 * 2、包括一个牌局中所需要的牌局ID，初始化的54张牌，地址标志，是否出牌的标志
 * 	  用户的手牌以及地主牌
 * 
 * @author Lzx
 *
 */
public class CardGame {
	/**
	 * 牌局ID
	 */
	private int id;
	/**
	 * 54张牌
	 */
	private Card[] card = new Card[56]; 
	/**
	 * 地主标识
	 */
	private int landLordsFlag ;
	/**
	 * 是否出牌标识
	 */
	private boolean [] flag = {false,false,false};
	/**
	 * 当前已经出的牌
	 */
	private List<Card>[] currentList; 
	/**
	 * 3个玩家所分到的牌
	 */
	private List<Card>[] playerList; 
	/**
	 * 地主牌
	 */
	private List<Card> lordList;
	
	@SuppressWarnings("unchecked")
	public CardGame() {
		currentList = new ArrayList[3];
		playerList = new ArrayList[3];
		lordList = new ArrayList<Card>();
		flag[0] = false;
		flag[1] = false;
		flag[2] = false;
	}
	
	public Card[] getCard() {
		return card;
	}
	
	public List<Card>[] getCurrentList() {
		return currentList;
	}

	public void setCurrentList(List<Card>[] currentList) {
		this.currentList = currentList;
	}

	public void setCard(Card[] card) {
		this.card = card;
	}
	public List<Card>[] getPlayerList() {
		return playerList;
	}
	public void setPlayerList(List<Card>[] playerList) {
		this.playerList = playerList;
	}
	public List<Card> getLordList() {
		return lordList;
	}
	public void setLordList(List<Card> lordList) {
		this.lordList = lordList;
	}
	
	
	public int getLandLordsFlag() {
		return landLordsFlag;
	}

	public void setLandLordsFlag(int landLordsFlag) {
		this.landLordsFlag = landLordsFlag;
	}

	public boolean[] getFlag() {
		return flag;
	}

	public void setFlag(boolean[] flag) {
		this.flag = flag;
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "CardGame [id=" + id + ", card=" + Arrays.toString(card) + ", landLordsFlag=" + landLordsFlag + ", flag="
				+ Arrays.toString(flag) + ", currentList=" + Arrays.toString(currentList) + ", playerList="
				+ Arrays.toString(playerList) + ", lordList=" + lordList + "]";
	}

	
	
	
}
