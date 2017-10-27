package tank;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

class MyPanel extends JPanel implements KeyListener, Runnable{
	
	//定义画布参数
	//定义画布大小
	private final static int width = 1000;
	private final static int height = 800;
	
	//定义一个我的坦克
	private Hero hero;
	
	//定义敌人的坦克组
	//用Vector确保线程安全
	private Vector<Tank> tks = new Vector<Tank>();
	
	//定义坦克数量
	private final int maxTankNum = 10;
	
	//定义爆炸效果图片
	private final Image images[] = new Image[32];
	//所有图片才能组成一颗炸弹
	private Vector<Explosion> explosion = new Vector<Explosion>();
	
	//构造函数
	public MyPanel(){
		hero = new Hero(460, 750);
		
		//初始化敌人坦克
		for (int i=0; i<maxTankNum; i++){
			//创建一辆敌人的坦克对象
			tks.add(new Tank((i+2)*70, 0));
			
			//启动敌人的坦克
			Thread t = new Thread(tks.get(i));
			t.start();
			
		}
		
		//初始化图片
		for(int i=0; i<32; i++){
			try {
				images[i] = ImageIO.read(this.getClass().getResource((i>=10) ?
						"/expl_09_00"+Integer.toString(i)+".png" 
						: "/expl_09_000"+Integer.toString(i)+".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
//			images[i] = Toolkit.getDefaultToolkit()
//					.getImage(Panel.class.getResource
//							((i>=10) ?
//									"/expl_09_00"+Integer.toString(i)+".png" 
//									: "/expl_09_000"+Integer.toString(i)+".png"));
		}
		
	}
	
	/**
	 * 渲染画布
	 */
	public void paint(Graphics g){
		
		super.paint(g);
		g.fillRect(0, 0, MyPanel.width, MyPanel.height);
		
		/**
		 * 画自己的坦克
		 */
		if(!hero.isDead()){	//如果玩家坦克没被击中，则画出
			this.drawTank(g, hero);
		}		
		
		//从bullets中取出子弹，并画出
		for(int i=0; i<hero.getBullets().size(); i++){
			
			Bullet bullet = hero.getBullets().get(i);
			
			if(bullet!=null&&!bullet.isDead()){
				this.drawBullet(g, bullet);
			}
			
			if(bullet.isDead()){
				//从bullets中删除该子弹
				hero.getBullets().remove(bullet);
				//将子弹实例从内存删除
				bullet = null;
			}
		}
		
		//画出爆炸效果
		for(int i=0; i<explosion.size(); i++){
			//取出炸弹
			Explosion exp = explosion.get(i);
			g.drawImage(images[31-exp.getLife()], exp.getX()-85
					, exp.getY()-65, 200, 200, this);
			exp.lifeDown();
			
			//如果炸弹生命值为0，就把向量去掉
			if(!exp.isLive()){
				explosion.remove(exp);
				//将炸弹实例从内存清除
				exp = null;
			}
		}
		
		//画敌人的坦克
		for(Tank tank : tks){
			if(!tank.isDead()){
				this.drawTank(g, tank);
				//再画敌人的子弹
				for(int i=0; i<tank.getBullets().size(); i++){
					
					Bullet bullet = tank.getBullets().get(i);
					
					if(bullet!=null&&!bullet.isDead()){
						this.drawBullet(g, bullet);
					}
					
					if(bullet.isDead()){
						//从bullets中删除该子弹
						hero.getBullets().remove(bullet);
					}
				}
			}
		}
			
	}

	/**
	 * 写一个函数专门判断子弹是否击中坦克
	 */
	public void hitTank(Bullet bullet, Tank tank){
		//判断坦克的方向
		switch(tank.getDirection()){
			case UP :
			case DOWN :
				if(bullet.getX()>=tank.getX()&&
						bullet.getX()<=tank.getX()+40&&
						bullet.getY()>=tank.getY()&&
						bullet.getY()<=tank.getY()+50){
					//击中
					//子弹死亡
					bullet.setIsDead(true);
					//坦克死亡
					tank.setDead(true);
					//创建一颗炸弹，放入Vector
					this.explosion.add(new Explosion(tank.getX(), tank.getY()));
					//将坦克实例从内存删除
					tank = null;
				}
				break;
			case LEFT :
			case RIGHT :
				if(bullet.getX()>=tank.getX()&&
						bullet.getX()<=tank.getX()+50&&
						bullet.getY()>=tank.getY()&&
						bullet.getY()<=tank.getY()+40){
					//击中
					//子弹死亡
					bullet.setIsDead(true);
					//坦克死亡
					tank.setDead(true);
					//创建一颗炸弹，放入Vector
					this.explosion.add(new Explosion(tank.getX(), tank.getY()));
					//将坦克实例从内存删除
					tank = null;
				}
		}
	}
	
	/**
	 * 画坦克方法
	 */
	public void drawTank(Graphics g, Tank tank){
		
		//判断是什么类型的坦克
		switch(tank.getType()){
			case 0:
				g.setColor(Color.YELLOW);
				break;
			case 1:
				g.setColor(Color.CYAN);
				break;
		}
		
		/**
		 * 画出我的坦克
		 */
		//判断坦克方向
		switch(tank.getDirection()){
			case UP :
				//画炮筒
				g.drawLine(tank.getX()+20, tank.getY()+25, tank.getX()+20, tank.getY()-5);
				drawVerticalTank(g, tank);
				break;
			case DOWN :
				g.drawLine(tank.getX()+20, tank.getY()+25, tank.getX()+20, tank.getY()+55);
				drawVerticalTank(g, tank);
				break;
			case LEFT :
				g.drawLine(tank.getX()+25, tank.getY()+20, tank.getX()-5, tank.getY()+20);
				drawHorizontalTank(g, tank);
				break;
			case RIGHT :
				g.drawLine(tank.getX()+25, tank.getY()+20, tank.getX()+55, tank.getY()+20);
				drawHorizontalTank(g, tank);
				break;
			default: 
				break;
		}
		
		
	}
	
	/**
	 * 画竖着的坦克车身
	 */
	public void drawVerticalTank(Graphics g, Tank tank){
		//画左边履带
		g.fill3DRect(tank.getX(), tank.getY(), 10, 50, false);
		//画右边履带
		g.fill3DRect(tank.getX()+30, tank.getY(), 10, 50, false);
		//画中间车身
		g.fill3DRect(tank.getX()+10, tank.getY()+10, 20, 30, false);
		//画炮架
		g.fillOval(tank.getX()+10, tank.getY()+15, 20, 20);
	}
	
	/**
	 * 画横着的坦克车身
	 */
	public void drawHorizontalTank(Graphics g, Tank tank){
		//画上边履带
		g.fill3DRect(tank.getX(), tank.getY(), 50, 10, false);
		//画下边履带
		g.fill3DRect(tank.getX(), tank.getY()+30, 50, 10, false);
		//画中间车身
		g.fill3DRect(tank.getX()+10, tank.getY()+10, 30, 20, false);
		//画炮架
		g.fillOval(tank.getX()+15, tank.getY()+10, 20, 20);
	}
	
	/**
	 * 画子弹方法
	 */
	public void drawBullet(Graphics g, Bullet bullet){
		g.draw3DRect(bullet.getX(),
				bullet.getY(),
				2, 2, false);
	}
	
	/**
	 * 判断是否击中敌方坦克
	 */
	public void hitEnemy(){
		//取出子弹			
		for(Bullet bullet : hero.getBullets()){
			//判断子弹是否有效
			if(!bullet.isDead()){
				//取出每个坦克，与它判断
				for(Tank tank : tks){
					//取出坦克
					if(!tank.isDead())
						this.hitTank(bullet, tank);
				}
			}
		}
	}
	
	/**
	 * 判断是否击中我方坦克
	 */
	public void hitMe(){
		//取出每个坦克
		for(Tank tank : tks){
			//取出每个敌方坦克的每颗子弹
			for(Bullet bullet : tank.getBullets()){
				//判断子弹是否有效
				if(!bullet.isDead()){
					//与玩家坦克判断
					if(!hero.isDead()){
						this.hitTank(bullet, hero);
					}
				}
			}
		}
	}
	
	/**
	 * 控制坦克
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		//首先判断玩家坦克是否死亡
		if(!hero.isDead()){
			switch(e.getKeyCode()){
			case KeyEvent.VK_DOWN :
				hero.move(Direction.DOWN);
				break;
			case KeyEvent.VK_UP :
				hero.move(Direction.UP);
				break;
			case KeyEvent.VK_LEFT :
				hero.move(Direction.LEFT);
				break;
			case KeyEvent.VK_RIGHT :
				hero.move(Direction.RIGHT);
				break;
			case KeyEvent.VK_A :
				//开火
				if(this.hero.getBullets().size()<5)
					hero.fire();
				break;
			}
			this.repaint();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void run() {
		//每隔100毫秒重新渲染
		while(true){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//判断是否击中敌方坦克
			hitEnemy();
			//判断是否击中我方坦克
			hitMe();
			this.repaint();
		}
	}
	
}