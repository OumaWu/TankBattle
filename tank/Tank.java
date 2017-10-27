package tank;
import java.util.Vector;

class Tank implements Runnable{
	
	//��ʾ̹�˵�����
	private int x, y, speed;
	protected int type;
	private Direction direction;
	private boolean isDead = false;
	
	//����һ�����������Դ�ŵ��˵��ӵ�
	private Bullet bullet;
	private Vector<Bullet> bullets;
	
	//��������ӵ���Ӧ���ڸոմ���̹�˺͵��˵�̹���ӵ�������
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
	 * ̹���ƶ�
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
	
	//����
	public void fire(){
		bullet = new Bullet(this);
		this.bullets.add(bullet);
		
		//�����ӵ��߳�
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
					//˵��̹������������
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
					//����
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
					//����
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
					//����
					for(int i=0; i<30; i++){
						//��֤̹�˲����߽�
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
			
			//��̹���������
			try {
				this.fire();
				Thread.sleep(50+(int)(Math.random()*200));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//��̹���������һ���µķ���
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
			
			//�жϵ���̹���Ƿ�����
			if(this.isDead){
				//��̹���������˳��߳�
				break;
			}
		}
	}

}

//�ҵ�̹��
class Hero extends Tank {
	
	public Hero(int x, int y) {
		super(x, y);
		this.type = 1;
		this.setDirection(Direction.UP);
	}
	
}