package club.youth996.figthlandors.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 1、符合规则的牌型组
 * 2、用于对人机牌的拆分存储
 * 
 * @author Lzx
 *
 */
public class CardGroup {
	/**
	 * 单牌 
	 */
	public List<String> single = new ArrayList<String>();
	/**
	 * 对子
	 */
	public List<String> pair = new ArrayList<String>(); 
	/**
	 * 3带
	 */
	public List<String> threeZones = new ArrayList<String>(); 
	/**
	 * 顺子
	 */
	public List<String> straight = new ArrayList<String>(); 
	/**
	 * 连对
	 */
	public List<String> evenPair = new ArrayList<String>(); 
	/**
	 * 飞机
	 */
	public List<String> plane = new ArrayList<String>(); 
	/**
	 * 炸弹
	 */
	public List<String> bomb = new ArrayList<String>(); 
}
