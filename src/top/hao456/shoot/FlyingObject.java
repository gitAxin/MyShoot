package top.hao456.shoot;
import java.awt.image.BufferedImage;



public abstract class FlyingObject {
	
	protected BufferedImage image;   //图片
	protected int width;			//宽
	protected int height; 			//高
	protected int x;				//x坐标
	protected int y;				//y坐标
	
	
	/**飞行物走一步*/
	public abstract void step();
	
	/**检查是否出界*/
	public abstract boolean outOfBounds();
	
	/**检测敌人是否被击中*/
	public boolean shootBy(Bullet bullet){
		int x1 = this.x;   //敌人的x
		int x2 = this.x+this.width;	//敌人的x+敌人的宽
		int y1 = this.y;			//
		int y2 = this.y+this.height;
		int x = bullet.x;
		int y = bullet.y;
		
		return x>=x1 && x<=x2 && y>=y1 && y<=y2;
			
	}
	
}
