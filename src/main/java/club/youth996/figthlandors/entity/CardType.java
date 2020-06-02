package club.youth996.figthlandors.entity;

/**
 * 1、允许出牌的牌型常量以及不能出牌的常量
 * 
 * @author Lzx
 *
 */
public enum CardType { 
	//不能出牌
	c0,
	//单牌
	c1,
	//对
	c2,
	//3不带
	c3,
	//炸弹
	c4,
	//3带1
	c31,
	//3带2
	c32,
	//4带2个单，或者一对
	c411,
	//4带2对
	c422,
	//顺子
	c123,
	//连队
	c1122,
	//飞机
	c111222,
	//飞机带单牌
	c11122234,
	//飞机带对
	c1112223344
}
