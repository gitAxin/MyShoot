package top.hao456.shoot;
/**英雄机: 是飞行物*/
import java.awt.image.BufferedImage;
import java.util.Random;

public class Hero extends FlyingObject  {
	private int doubleFire; //火力值
	private int life;		//命
	private BufferedImage[] images;   //可切换的图片
	private int index; //协助图片切换
	
	public Hero(){
		image = ShootGame.hero3;
		width = image.getWidth();   //获取图片的宽度
		height = image.getHeight(); //获取图片的高度
		x = 150;//X;固定的值
		y = 400;//y;固定的值
		doubleFire = 0;   //默认为0(单位火力)
		life = 3;         //默认3条命
		images = new BufferedImage[]{ShootGame.hero3,ShootGame.hero4};
		index = 0;
	
	}
	
	/** 重写step()方法*/
	public void step(){   //走步  10毫秒走一次
		image = images[index++/10%images.length];
		
		/*
		index++;
		int a = index/10;
		int b = a%2;
		image = images[b];
		*/
	}
	
	public Bullet[] shoot(){
		
		int xStep = this.width /16; //xStep:1/16英雄机的宽
		int yStep = 20;		//固定的20
		
		if(doubleFire > 0){//双倍
			Bullet[] bs = new Bullet[3];
			bs[0] = new Bullet(this.x+5*xStep,this.y-yStep); //
			bs[1] = new Bullet(this.x+8*xStep,this.y-yStep);
			bs[2] = new Bullet(this.x+11*xStep,this.y-yStep);
			doubleFire-=3;  //发射一次双倍火力,则减2
			return bs;
		}else{  //单倍
			Bullet[] bs = new Bullet[2];
			bs[0] = new Bullet(this.x+5*xStep,this.y-yStep); //
			bs[1] = new Bullet(this.x+11*xStep,this.y-yStep); //
			return bs;
		}
	}
	
	/**英雄机随着鼠标动,x:鼠标的x y :鼠标的y*/
	public void moveTo(int x, int y){
		this.x = x-this.width/2;  //英雄机的x=鼠标的x/2;
		this.y = y-this.height/2;  //
	}
	
	
	/**重写outOfBounds检查是否载界*/
	public  boolean outOfBounds(){
		return false;   //永不越界
	}
	
	public void addLife(){
		life++;    //生命增1
	}
	
	public int getLife(){ //获取英雄机的命
		return life;    
	}
	
	public void addDoubleFire(){
		doubleFire+=40;    //火力值增40
	}
	
	public int getdoubleFire(){
		return doubleFire;
	}
	
	public void downLife(){
		life--;
	}
	
	public void clearDoubleFire(){
		doubleFire=0;
	}
	public boolean hit(FlyingObject fly){
		int x1 =fly.x+10-this.width;
		int x2 = fly.x-10+fly.width;
		int y1 = fly.x-fly.height;
		int y2 = fly.y+fly.height;
		int x = this.x;
		int y = this.y;
		return x>=x1 && x<=x2 && y>=y1 && y<=y2;
	}
}
