package tank;
import java.util.Vector;

class Tank implements Runnable{
	
	//表示坦克的坐标
	private int x, y, speed;
	protected int type;
	private Direction direction;
	private boolean isDead = false;
	
	//定义一个向量，可以存放敌人的子弹
	private Bullet bullet;
	private Vector<Bullet> bullets;
	
	//敌人添加子弹，应当在刚刚创建坦克和敌人的坦克子弹死亡后
	public Tank(int x, int y){
		this.x = x;
		this.y = y;
		this.setSpeed(5);
		this.type = 0;
		this.bullets = new Vector<Bullet>();
		this.setDirection(Direction.DOWN);
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

	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(50);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			switch(direction){
				case UP :
					//说明坦克正在向上走
					for(int i=0; i<30; i++){
						if(this.y>0){
							this.move(direction);
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
						if(y<MyPanel.HEIGHT-50){
							this.move(direction);
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
						if(x>0){
							this.move(direction);
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
						//保证坦克不出边界
						if(x<MyPanel.WIDTH-50){
							this.move(direction);
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
				this.direction = Direction.UP;
				break;
			case 1 :
				this.direction = Direction.RIGHT;
				break;
			case 2 :
				this.direction = Direction.DOWN;
				break;
			case 3 :
				this.direction = Direction.LEFT;
				break;
			}
			
			//判断敌人坦克是否死亡
			if(this.isDead){
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
		this.type = 1;
		this.setDirection(Direction.UP);
	}
	
}