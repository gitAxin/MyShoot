package top.hao456.shoot;

import java.util.Random;

/**小蜜蜂 既是飞行物*/

public class Bee extends FlyingObject implements Award {
	private int xSpeed = 1;  //x坐标的移动速度
	private int ySpeed = 2;	//y坐标的移动速度
	private int awardType;  // //奖励的类型 (0或1);
	
	/**构造方法*/
	public Bee(){
		image = ShootGame.bee;
		width = image.getWidth();   //获取图片的宽度
		height = image.getHeight(); //获取图片的高度
		Random rand = new Random();
		x = rand.nextInt(ShootGame.WIDTH - this.width);//窗口宽减敌机宽
		y = this.height;  // 负的敌机的高
		awardType = rand.nextInt(2);
		
	}
	
	
	public int getType(){
		return awardType; 
	}
	
	/** 重写step()方法*/
	public void step(){  //每10秒运行一次

		Random rand = new Random();
	
		/*
		int index = rand.nextInt(100);
		if(index%2==0){
			x+=xSpeed;   //x+向左或向右
		}else{
			x-=xSpeed;
		}
		*/
		
		x+=xSpeed;
		y+=ySpeed;	//y+向下
		if(x>=ShootGame.WIDTH-this.width){
			xSpeed=-1;
		}
		if(x<=0){
			xSpeed=1;
		}
		
		
		}
	
	
	
	
	/**重写outOfBounds检查是否载界*/
	public  boolean outOfBounds(){
		return this.y>=ShootGame.HEIGHT;   //蜜蜂的越界处理
		
	}

}
