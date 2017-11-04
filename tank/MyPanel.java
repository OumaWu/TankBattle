package tank;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

class MyPanel extends JPanel implements KeyListener, Runnable{
	
	private static final long serialVersionUID = 1L;
	//定义画布参数
	//定义画布大小
	public final static int width = 600;
	public final static int height = 500;
	
	//定义一个我的坦克
	private Hero hero;
	
	//定义敌人的坦克组
	//用Vector确保线程安全
	private Vector<EnemyTank> tks;
	
	//定义坦克数量
	private final int maxTankNum = 5;
	
	//定义爆炸效果图片
	private final Image images[] = new Image[32];
	//所有图片才能组成一颗炸弹
	private Vector<Explosion> explosion = new Vector<Explosion>();
	
	//构造函数
	public MyPanel(){
		
		//恢复记录
		Recorder.getRecord();
		
		tks = new Vector<EnemyTank>();
		hero = new Hero((MyPanel.width-40)/2, MyPanel.height-50);
		hero.setEts(tks);
		
		//初始化敌人坦克
		for (int i=0; i<maxTankNum; i++){
			//创建一辆敌人的坦克对象
			EnemyTank et = new EnemyTank((i+1)*70, 0);
			//将MyPanel的敌人坦克向量交给该敌人坦克向量
			et.setEts(tks);
			
			//将敌人坦克添加到向量
			tks.add(et);
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
		
		//播放开战声音
		new AudioPlayer("./src/tank/tb_newgame.wav").start();
		
	}
	
	/**
	 * @return the tks
	 */
	public Vector<EnemyTank> getTks() {
		return tks;
	}

	/**
	 * @param tks the tks to set
	 */
	public void setTks(Vector<EnemyTank> tks) {
		this.tks = tks;
		//设置向量
		for(EnemyTank et : this.tks)
			et.setEts(tks);
	}

	/**
	 * 画出提示信息
	 */
	public void showInfo(Graphics g){
		//画出提示信息坦克，(改坦克不参与战斗)
		this.drawTank(g, new EnemyTank((int)(MyPanel.width*0.1), MyPanel.height+10));
		g.setColor(Color.black);
		g.drawString(Recorder.getEnNum()+"", (int)(MyPanel.width*0.1)+50, MyPanel.height+40);
		this.drawTank(g, new Hero((int)(MyPanel.width*0.3), MyPanel.height+10));
		g.setColor(Color.black);
		g.drawString(Recorder.getMyLife()+"", (int)(MyPanel.width*0.3)+50, MyPanel.height+40);
		
		//画出玩家总成绩
		g.setColor(Color.black);
		g.setFont(new Font("宋体", Font.BOLD, 20));
		g.drawString("您的总成绩 : " + Recorder.getAllEnNum(), (int)(MyPanel.width*0.4)+50, MyPanel.height+40);
	}
	
	/**
	 * 渲染画布
	 */
	public void paint(Graphics g){
		
		super.paint(g);
		g.fillRect(0, 0, MyPanel.width, MyPanel.height);
		//画出提示信息
		this.showInfo(g);
		
		/**
		 * 画自己的坦克
		 */
		if(!hero.isDead()){	//如果玩家坦克没被击中，则画出
			this.drawTank(g, hero);
		}		
		
		//从bullets中取出子弹，并画出
		for(int i=0; i<hero.getBullets().size(); i++){
			
			Bullet bullet = hero.getBullets().get(i);
//			if(bullet!=null&&!bullet.isDead()){
//				this.drawBullet(g, bullet);
//			}	
			if(bullet.isDead()){
				//从bullets中删除该子弹
				hero.getBullets().remove(bullet);
				//将子弹实例从内存删除
				bullet = null;
			}
			else {
				this.drawBullet(g, bullet);
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
//					if(bullet!=null&&!bullet.isDead()){
//						this.drawBullet(g, bullet);
//					}
					if(bullet.isDead()){
						//从bullets中删除该子弹
						hero.getBullets().remove(bullet);
					}
					else {
						this.drawBullet(g, bullet);
					}
				}
			}
		}
			
	}

	/**
	 * 写一个函数专门判断子弹是否击中坦克
	 */
	public boolean hitTank(Bullet bullet, Tank tank){
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
					//返回true,说明被击中
					return true;
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
					//返回true,说明被击中
					return true;
				}
		}
		return false;
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
	 * 判断地方坦克是否全死亡
	 */
	public boolean AllDead(){
		boolean result = true;
		for (Tank tank : this.tks){
			if(!tank.isDead())
				return false;
		}
		return result;
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
						if(this.hitTank(bullet, tank)){
//							tks.remove(tank);
							tank = null;
							Recorder.reduceEnNum();
							Recorder.addAllEnNum();
						}
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
						if(this.hitTank(bullet, hero))
							hero.setDead(true);
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
				//保证玩家坦克不出边界
				if(hero.getY()<MyPanel.height-50&&!hero.isTouched()){
					hero.move(Direction.DOWN);
				}
				//如果移动会超出边界，就只改变方向，不改变坦克坐标
				else{
					hero.setDirection(Direction.DOWN);
				}
				break;
			case KeyEvent.VK_UP :
				//保证玩家坦克不出边界
				if(hero.getY()>0&&!hero.isTouched()){
					hero.move(Direction.UP);
				}
				//如果移动会超出边界，就只改变方向，不改变坦克坐标
				else{
					hero.setDirection(Direction.UP);
				}
				break;
			case KeyEvent.VK_LEFT :
				//保证玩家坦克不出边界
				if(hero.getX()>0&&!hero.isTouched()){
					hero.move(Direction.LEFT);
				}
				//如果移动会超出边界，就只改变方向，不改变坦克坐标
				else{
					hero.setDirection(Direction.LEFT);
				}
				break;
			case KeyEvent.VK_RIGHT :
				//保证玩家坦克不出边界
				if(hero.getX()<MyPanel.width-50&&!hero.isTouched()){
					hero.move(Direction.RIGHT);
				}
				//如果移动会超出边界，就只改变方向，不改变坦克坐标
				else{
					hero.setDirection(Direction.RIGHT);
				}
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
	public void keyReleased(KeyEvent e) {}

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
			if(!AllDead()){
				hitEnemy();
			}
			//判断是否击中我方坦克
			if(!hero.isDead()){
				hitMe();
			}
			this.repaint();
		}
	}

	public void setEnemyThread() {
		for (EnemyTank et : this.tks){
			Thread t = new Thread(et);
			t.start();
		}
	}
	
}

/**
 * 就是一个提示作用
 */
class MyStartPanel extends JPanel implements Runnable {
	
	private boolean times = true;
	
	private static final long serialVersionUID = 1L;
	public void paint(Graphics g){
		super.paint(g);
		g.fillRect(0, 0, MyPanel.width, MyPanel.height);
		
		g.setColor(Color.yellow);
		//开关信息的字体
		Font myFont = new Font("华文新魏", Font.BOLD, 30);
		g.setFont(myFont);
		
		if(times){
			g.drawString("stage 1", (MyPanel.width-105)/2, (MyPanel.height-30)/2);
		}
	}
	@Override
	public void run() {
		while(true){
			//休眠
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			times = !times;
			//重画
			this.repaint();
		}
	}
}




