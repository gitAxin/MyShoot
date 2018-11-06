package top.hao456.shoot;

import java.util.Random;

/**子弹  是飞行物*/
public class Bullet extends FlyingObject{
	private int speed = 3;
	
	public Bullet(int x, int y){
		image = ShootGame.bullet;
		width = image.getWidth();   //获取图片的宽度
		height = image.getHeight(); //获取图片的高度
		this.x = x;
		this.y = y;
		
	}
	
	/** 重写step()方法*/
	public void step(){
		y-=speed;
		
	}
	

	/**重写outOfBounds检查是否载界*/
	public  boolean outOfBounds(){
		return this.y<=-this.height;   //子弹的越界处理
	}
}
