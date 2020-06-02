package club.youth996.figthlandors.util;

import club.youth996.figthlandors.entity.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * 1、用于与前端数据交互时Json格式的转换
 * 
 * @author Lzx
 *
 */
public class JsonChange {
	
	public static List<Card> toList(String[] strs){
		if(strs==null) {
			return null;
		}
		List<Card> list =  new ArrayList<Card>();
		for(int i=0;i<strs.length;i++) {
			String name = strs[i];
			Card temp =new Card(name);
			list.add(temp);
		}
		return list;
	}
	
	public static List<Card> toCard(List<String> cardType){
		System.out.println("x: " + cardType);
		int len1  = cardType.size();
		List<Card> card = new ArrayList<Card>();
		if( len1 != 0 ) {
			System.out.println(cardType.toString()+"   "+len1);
			for( int i = 0 ; i < len1 ; i++ ) {
				String [] na = cardType.get(i).split(",");
				int len2 = na.length;
				System.out.println(len2);
				for( int j = 0 ; j < len2 ; j++ ) {
					Card temp = new Card(null);
					temp.setName(na[j]);
					card.add(temp);
				}
			}
		}
		
		return card;
	}
	
}