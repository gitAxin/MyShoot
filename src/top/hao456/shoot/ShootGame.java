package top.hao456.shoot;
//主窗口类

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;  //生成随机数的类
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO; //图片的输入输出
import javax.swing.JFrame;//相框
import javax.swing.JPanel;//面板

public class ShootGame extends JPanel {

	public static final int WIDTH = 400;// 窗口宽度
	public static final int HEIGHT = 654;// 窗口高度
	public static BufferedImage background;
	public static BufferedImage start;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage hero3;
	public static BufferedImage hero4;
	public static BufferedImage bao1;
	public static BufferedImage bao2;

	static {// 初始化静态图片
		try {
			background = ImageIO.read(ShootGame.class.getResource("background.png")); // 同包中读取图片的方式
			start = ImageIO.read(ShootGame.class.getResource("start.png"));
			pause = ImageIO.read(ShootGame.class.getResource("pause.png"));
			gameover = ImageIO.read(ShootGame.class.getResource("gameover.png"));
			airplane = ImageIO.read(ShootGame.class.getResource("airplane.png"));
			bee = ImageIO.read(ShootGame.class.getResource("bee.png"));
			bullet = ImageIO.read(ShootGame.class.getResource("bullet.png"));
			hero3 = ImageIO.read(ShootGame.class.getResource("hero3.png"));
			hero4 = ImageIO.read(ShootGame.class.getResource("hero4.png"));
			bao1 = ImageIO.read(ShootGame.class.getResource("bao1.png"));
//			bao2 = ImageIO.read(ShootGame.class.getResource("bao2.png"));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("静态图片初始化失败");
		}

	}

	private Hero hero = new Hero(); // 一个英雄机
	private FlyingObject[] flyings = {}; // 一堆敌人
	private Bullet[] bullets = {};// 一堆子弹

	public FlyingObject nextOne(){
		Random rand = new Random();
		int type = rand.nextInt(20);//生成0-19的随机数
		if(type<4){					//0-3时生成小蜜蜂对象
			return new Bee();
		}else{                 //4到19时生成敌机对象
			return new Airplane();  
		}
	}
	
	
	int flyIndex = 0;
	/** 敌人(敌机+小蜜蜂)入场*/  
	public void enterAction(){  //10秒走一次
		
		flyIndex++;
		if(flyIndex%40==0){
			FlyingObject obj = nextOne();
			flyings = Arrays.copyOf(flyings, flyings.length+1);
			flyings[flyings.length-1]=obj;   //
		}
		
		
	}
	public void stepAction(){//每10毫秒走一次
		hero.step();
		for(int i = 0; i < flyings.length; i++){		//遍历所有敌人
			flyings[i].step();	//敌人走一步
		}
		for(int i = 0; i < bullets.length; i++){     //遍历所有子弹 
			bullets[i].step();		//子弹走一步
		}
	}
	
	
	int shootIndex = 0;
	public void shootAction(){
		shootIndex++;
		if(shootIndex%30==0){
			Bullet[] bs = hero.shoot();
			bullets = Arrays.copyOf(bullets, bullets.length+bs.length);
			System.arraycopy(bs,0,bullets,bullets.length-bs.length,bs.length);
		}
	}
	
	//越界处理
	public void outOfBoundsAction(){
		int index = 0;  //1)不越界敌人数组下标  2)不越界敌人的计数器
		FlyingObject[] flyingLives = new FlyingObject[flyings.length];
		for(int i = 0; i < flyings.length; i ++){
			FlyingObject f = flyings[i];
			if(!f.outOfBounds()){
				flyingLives[index] = f;  //不越界的越到FlyingLives
				index++;
			}
		}
		flyings = Arrays.copyOf(flyingLives, index);
		
		index = 0;
		Bullet[] bulletLives = new Bullet[bullets.length];
		for(int i = 0; i < bullets.length; i++){
			Bullet b = bullets[i];
			if(!b.outOfBounds()){
				bulletLives[index] = b;
				index++;
			}
		}
		bullets = Arrays.copyOf(bulletLives, index);
	}
	
	/** 所有的子弹与所有敌人(敌机+小蜜蜂)的碰撞*/
		public void bangAction(){
			for(int i = 0; i<bullets.length; i++){
				Bullet b = bullets[i];
				bang(b);
			}
		}
		
	
		
		
		
		
		
		int score = 0;  //玩家的得分
		/**一个子弹与所有敌人(敌机+小蜜蜂)的碰撞 */	
		public void bang(Bullet b){
			int index = -1;  //被撞敌人的下标
			for(int i = 0; i < flyings.length; i++){
				FlyingObject f = flyings[i];
				if(f.shootBy(b)){
					index = i;
					break; //其余敌人不再参与比较了
				}
			}
			if(index !=-1){
				FlyingObject one = flyings[index];  //被撞的敌人
				if(one instanceof Enemy){   //若是敌人 强转为敌人
					Enemy e = (Enemy)one;	//
					score += e.getScore();
					System.out.println("分数:"+score); 
				}
				
				if(one instanceof Award){
					Award a = (Award)one;
					int type = a.getType();
					switch(type){
					case Award.DOUBLE_FIRE:
						hero.addDoubleFire();
						break;
					case Award.LIFE:
						hero.addLife();
						System.out.println("命数:"+hero.getLife());
						break;
					}
				}
				//交换被撞的敌人与数组中的最后一个元素
				FlyingObject t = flyings[index];
				flyings[index] = flyings[flyings.length-1];
				flyings[flyings.length-1] = t;
				//缩容 (去掉最后一个元素,即被撞的敌人对象)
				flyings = Arrays.copyOf(flyings, flyings.length-1);
				
			}
			
		}
		public void hitAction(){
			for(int i = 0; i < flyings.length; i++){
				FlyingObject f = flyings[i];
				if(hero.hit(f)){
					hero.addLife();
					hero.downLife();
					FlyingObject t = flyings[i];
					flyings[i] = flyings[flyings.length-1];
					flyings[flyings.length-1]=t;
					flyings = Arrays.copyOf(flyings, flyings.length-1);
				}
			}
		}
		
		
		
		
		
		
		
	/**启动程序的执行*/
	public void action(){
		//创建鼠标侦听器
		MouseAdapter l = new MouseAdapter(){
			public void mouseMoved(MouseEvent e){
				int x = e.getX();
				int y = e.getY();
//				int x = hero.x;
//				int y = hero.y;
				hero.moveTo(x, y);
			}
		};
		this.addMouseListener(l);   //处理鼠标操作事件
		this.addMouseMotionListener(l);    //
		
		Timer timer = new Timer();//
		int intervel = 10;  //时间间隔(以毫秒为单位)
		timer.schedule(new TimerTask(){
			public void run(){
				enterAction();//敌人(敌机+小蜜蜂);
				stepAction();  //敌人 移动
				shootAction();  //子弹入场
				outOfBoundsAction();  //删除越界的飞行物
				bangAction();//子弹击中敌机
				hitAction();
				repaint();//重画 (调用paint()方法)
				
			}
		},intervel,intervel);               //第一个10:程序启动到第一次触发的时间间隔
	}
	
	
	/*在实现时,Timer类可以调度任务,TimerTask则是通过在run()方法里实现具体任务.Timer实例可以调度多任务
	 * 它是线程安全的.
	 * Timer 的构造器被调用时,它创建了一个线程,这个线程可以用来调度任务 .
	
	public void action(){
		TimerTask task = new TimerTask(){  //实现任务
			public void run(){
				enterAction();
				repaint();
			}
		};
		int interel = 10;
		Timer timer = new Timer();
		timer.schedule(task, interel,interel);
		
		
	}
	
	*/
	
	/**重写paint()*/
	public void paint(Graphics g){
		g.drawImage(background,0,0,null);  //图片名称,x坐标,y坐标,---
		paintHero(g);//画英雄机对象
		paintFlyingObject(g);//画敌人(敌机+小蜜蜂)
		paintBullets(g);//画子弹对象
		paintScoreAndFire(g);  //
		
	}
	public void paintHero(Graphics g){   //画英雄机
		
		g.drawImage(hero.image,hero.x,hero.y,null);
		
	}
	public void paintFlyingObject(Graphics g){ //画敌人
		for(int i = 0; i < flyings.length; i++){//遍历所有敌人,
				FlyingObject f = flyings[i];    //
				g.drawImage(f.image,f.x,f.y,null);
		}
		
	}
	public void paintBullets(Graphics g){//画子弹对象
		for(int i = 0; i < bullets.length; i ++){
			Bullet b = bullets[i];
			g.drawImage(b.image,b.x,b.y,null);
		}
		
	}

	public void paintScoreAndFire(Graphics g){
		g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,25));
		g.setColor(new Color(255,0,0));
		g.drawString("SCORE:"+score,10,25);
		g.drawString("LIFE:"+hero.getLife(), 10,75);
		g.drawString("FIRE:"+hero.getdoubleFire(), 10,50);
	}
	public static void main(String[] args) {//*************************************

		JFrame frame = new JFrame("Fly"); // 创建相框
		ShootGame game = new ShootGame();
		frame.add(game);
		frame.setSize(WIDTH, HEIGHT); // 设置窗口尺寸
		frame.setAlwaysOnTop(true); // 设置总是在最上面
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 设置默认关闭操作,当窗口关闭的时候退出
		frame.setLocationRelativeTo(null); // 设置居中显示 设置相对位置 为null
		frame.setVisible(true);// 1)设置窗口可见 2)尽快调用paint()方法
		
		game.action(); //启动程序的执行
		
		
		
		

	}

	
	
	
}
