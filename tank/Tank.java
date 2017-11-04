package tank;
import java.util.Vector;

abstract class Tank{
	
	//表示坦克的坐标
	private int x, y, speed;
	protected int type;
	private Direction direction;
	private boolean isDead = false;
	
	//定义一个向量，可以存放子弹
	private Bullet bullet;
	private Vector<Bullet> bullets;
	
	//定义一个向量，可以访问到MyPanel上所有敌人的坦克
	Vector<EnemyTank> ets;
	
	//敌人添加子弹，应当在刚刚创建坦克和敌人的坦克子弹死亡后
	public Tank(int x, int y){
		this.x = x;
		this.y = y;
		this.setSpeed(5);
		this.bullets = new Vector<Bullet>();
		ets = new Vector<EnemyTank>();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public Vector<Bullet> getBullets() {
		return bullets;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	 //得到MyPanel敌人坦克的向量
	public void setEts(Vector<EnemyTank> ets) {
		this.ets = ets;
	}
	
	/**
	 * 坦克移动
	 */
	public void move(Direction direction){
		this.setDirection(direction);	
		switch(direction){
			case UP :
				this.y -= this.speed;
			break;
			case DOWN :
				this.y += this.speed;
			break;
			case LEFT :
				this.x -= this.speed;
			break;
			case RIGHT :
				this.x += this.speed;
			break;
		}
	}
	
	//开火
	public void fire(){
		bullet = new Bullet(this);
		this.bullets.add(bullet);
		
		//启动子弹线程
		new Thread(bullet).start();
	}
	
	//判断是否碰到了别的坦克
	public boolean isTouched(){
	boolean isTouched = false;
	
	switch (this.getDirection()){
		case UP:
			//我的坦克向上
			//取出所有敌人坦克
			for(int i=0; i<ets.size(); i++){
				EnemyTank et = ets.get(i);
				//如果不是自己
				if(et!=this){
					//如果敌人的方向是向下或向上
					if(et.getDirection()==Direction.UP
							||et.getDirection()==Direction.DOWN){
						//左点
						if(getX()>=et.getX()&&getX()<=et.getX()+40
								&&getY()>=et.getY()
								&&getY()<=et.getY()+50)
							return true;
						//右点
						if(getX()+40>=et.getX()&&getX()+40<=et.getX()+40
								&&getY()>=et.getY()
								&&getY()<=et.getY()+50)
							return true;
					}
					if(et.getDirection()==Direction.LEFT
							||et.getDirection()==Direction.RIGHT){
						//左点
						if(getX()>=et.getX()&&getX()<=et.getX()+50
								&&getY()>=et.getY()
								&&getY()<=et.getY()+40)
							return true;
						//右点
						if(getX()+40>=et.getX()&&getX()+40<=et.getX()+50
								&&getY()>=et.getY()
								&&getY()<=et.getY()+40)
							return true;
					}
				}
			}
			break;
		case DOWN:
			//坦克向下
			//取出所有敌人坦克
			for(int i=0; i<ets.size(); i++){
				EnemyTank et = ets.get(i);
				//如果不是自己
				if(et!=this){
					//如果敌人的方向是向下或向上
					if(et.getDirection()==Direction.UP
							||et.getDirection()==Direction.DOWN){
						//我的左点
						if(getX()>=et.getX()&&getX()<=et.getX()+40
								&&getY()+50>=et.getY()
								&&getY()+50<=et.getY()+50)
							return true;
						//我的右点
						if(getX()+40>=et.getX()&&getX()+40<=et.getX()+40
								&&getY()+50>=et.getY()
								&&getY()+50<=et.getY()+50)
							return true;
					}
					if(et.getDirection()==Direction.LEFT
							||et.getDirection()==Direction.RIGHT){
						//左点
						if(getX()>=et.getX()&&getX()<=et.getX()+50
								&&getY()+50>=et.getY()
								&&getY()+50<=et.getY()+40)
							return true;
						//右点
						if(getX()+40>=et.getX()&&getX()+40<=et.getX()+50
								&&getY()+50>=et.getY()
								&&getY()+50<=et.getY()+40)
							return true;
					}
				}
			}
			break;
		case LEFT:
			//坦克向左
			//取出所有敌人坦克
			for(int i=0; i<ets.size(); i++){
				EnemyTank et = ets.get(i);
				//如果不是自己
				if(et!=this){
					//如果敌人的方向是向下或向上
					if(et.getDirection()==Direction.UP
							||et.getDirection()==Direction.DOWN){
						//我的上一点
						if(getX()>=et.getX()&&getX()<=et.getX()+40
								&&getY()>=et.getY()
								&&getY()<=et.getY()+50)
							return true;
						//我的下一点
						if(getX()>=et.getX()&&getX()<=et.getX()+40
								&&getY()+40>=et.getY()
								&&getY()+40<=et.getY()+50)
							return true;
					}
					if(et.getDirection()==Direction.LEFT
							||et.getDirection()==Direction.RIGHT){
						//上一点
						if(getX()>=et.getX()&&getX()<=et.getX()+50
								&&getY()>=et.getY()
								&&getY()+40<=et.getY()+40)
							return true;
						//下一点
						if(getX()>=et.getX()&&getX()<=et.getX()+50
								&&getY()+40>=et.getY()
								&&getY()+40<=et.getY()+40)
							return true;
					}
				}
			}
			break;
		case RIGHT:
			//坦克向右
			//取出所有敌人坦克
			for(int i=0; i<ets.size(); i++){
				EnemyTank et = ets.get(i);
				//如果不是自己
				if(et!=this){
					//如果敌人的方向是向下或向上
					if(et.getDirection()==Direction.UP
							||et.getDirection()==Direction.DOWN){
						//上点
						if(getX()+50>=et.getX()&&getX()+50<=et.getX()+40
								&&getY()>=et.getY()
								&&getY()<=et.getY()+50)
							return true;
						//下点
						if(getX()+50>=et.getX()&&getX()+50<=et.getX()+40
								&&getY()+40>=et.getY()
								&&getY()+40<=et.getY()+50)
							return true;
					}
					if(et.getDirection()==Direction.LEFT
							||et.getDirection()==Direction.RIGHT){
						if(getX()+50>=et.getX()&&getX()+50<=et.getX()+50
								&&getY()>=et.getY()
								&&getY()<=et.getY()+40)
							return true;
						if(getX()+50>=et.getX()&&getX()+50<=et.getX()+50
								&&getY()+40>=et.getY()
								&&getY()+40<=et.getY()+40)
							return true;
					}
				}
			}
			break;
		}	
	return isTouched;
	}
}

//敌人坦克
class EnemyTank extends Tank implements Runnable {
	
	public EnemyTank(int x, int y) {
		super(x, y);
		//设置坦克类型为敌人
		this.setType(0);
		//敌人坦克初始方向向下
		this.setDirection(Direction.DOWN);
	}
	
	public EnemyTank(int x, int y, Direction dir) {
		super(x, y);
		//设置坦克类型为敌人
		this.setType(0);
		//敌人坦克初始方向向下
		this.setDirection(dir);
	}
	

	@Override
	public final void run() {
		while(true){
			try {
				Thread.sleep(50);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			switch(this.getDirection()){
				case UP :
					//说明坦克正在向上走
					for(int i=0; i<30; i++){
						//保证敌人坦克不出边界
						if(this.getY()>0&&!this.isTouched()){
							this.move(this.getDirection());
						}
						else {
							this.setDirection(this.getDirection());
						}
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}	
					break;
				case DOWN :
					//向下
					for(int i=0; i<30; i++){
						//保证敌人坦克不出边界
						if(this.getY()<MyPanel.height-50&&!this.isTouched()){
							this.move(this.getDirection());
						}
						else {
							this.setDirection(this.getDirection());
						}
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}	
					break;
				case LEFT :
					//向左
						for(int i=0; i<30; i++){
						//保证敌人坦克不出边界
						if(this.getX()>0&&!this.isTouched()){
							this.move(this.getDirection());
						}
						else {
							this.setDirection(this.getDirection());
						}
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}	
					break;
				case RIGHT :
					//向右
					for(int i=0; i<30; i++){
						//保证敌人坦克不出边界
						if(this.getX()<MyPanel.width-50&&!this.isTouched()){
							this.move(this.getDirection());
						}
						else {
							this.setDirection(this.getDirection());
						}
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}	
					break;
			}
			
			//让坦克随机开火
			try {
				this.fire();
				Thread.sleep(50+(int)(Math.random()*200));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//让坦克随机产生一个新的方向
			switch((int)(Math.random()*4)){
			case 0 :
				this.setDirection(Direction.UP);
				break;
			case 1 :
				this.setDirection(Direction.RIGHT);
				break;
			case 2 :
				this.setDirection(Direction.DOWN);
				break;
			case 3 :
				this.setDirection(Direction.LEFT);
				break;
			}
			
			//判断敌人坦克是否死亡
			if(this.isDead()){
				//让坦克死亡后，退出线程
				break;
			}
		}
	}
}

//我的坦克
class Hero extends Tank {
	
	public Hero(int x, int y) {
		super(x, y);
		this.setType(1);;
		this.setDirection(Direction.UP);
	}
	
}