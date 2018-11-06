package top.hao456.shoot;
//敌机
import java.util.Random;
public class Airplane extends FlyingObject implements Enemy{
	private int speed = 2; //移动的速度
	
	/**构造方法*/
	public Airplane(){
		image = ShootGame.airplane;
		width = image.getWidth();   //获取图片的宽度
		height = image.getHeight(); //获取图片的高度
		Random rand = new Random();
		x = rand.nextInt(ShootGame.WIDTH - this.width);//窗口宽减敌机宽
		y = -this.height;  // 负的敌机的高
	}
	
	
	public int getScore(){  
		return 5;     //打掉一个敌机得5分
	}
	/** 重写step()方法*/
	public void step(){ 
		y+=speed;   //y+(向下)
		
		
		
	}
	
	
	
	
	
	/**重写outOfBounds检查是否载界*/
	public  boolean outOfBounds(){
		return this.y>=ShootGame.HEIGHT;   //敌机的越界处理
	}

}
