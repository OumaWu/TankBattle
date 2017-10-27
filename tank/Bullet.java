package tank;

class Bullet implements Runnable{
	
	private int speed, x, y, range;//子弹范围
	private boolean isDead;

	private Direction direction;
	
	public Bullet(Tank tank){
		this.speed = tank.getSpeed()*2;
		this.direction = tank.getDirection();
		this.setIsDead(false);
		this.range = 500;
	    //确定子弹坐标
		switch(direction){
			case UP :
				this.x = tank.getX()+19;
				this.y = tank.getY()-9;
				break;
			case DOWN :
				this.x = tank.getX()+19;
				this.y = tank.getY()+57;
				break;
			case LEFT :
				this.x = tank.getX()-9;
				this.y = tank.getY()+19;
				break;
			case RIGHT :
				this.x = tank.getX()+57;
				this.y = tank.getY()+19;
				break;
		}
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

	public boolean isDead() {
		return isDead;
	}

	public void setIsDead(boolean isDead) {
		this.isDead = isDead;
	}

	@Override
	public void run() {
		while(this.range>0){
			//控制子弹速度
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			switch(direction){
				case UP :
					//画炮筒
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
//			System.out.println("子弹坐标x="+x+"y="+y);
			this.range -= this.speed;
			
			//子弹何时死亡
			//判断子弹是否碰到边缘
			if(x<0||x>1000||y<0||y>800||range<=0){
				this.setIsDead(true);
				break;
			}
		}
	}
	
}
