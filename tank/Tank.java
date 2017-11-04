package tank;
import java.util.Vector;

abstract class Tank{
	
	//��ʾ̹�˵�����
	private int x, y, speed;
	protected int type;
	private Direction direction;
	private boolean isDead = false;
	
	//����һ�����������Դ���ӵ�
	private Bullet bullet;
	private Vector<Bullet> bullets;
	
	//����һ�����������Է��ʵ�MyPanel�����е��˵�̹��
	Vector<EnemyTank> ets;
	
	//��������ӵ���Ӧ���ڸոմ���̹�˺͵��˵�̹���ӵ�������
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

	 //�õ�MyPanel����̹�˵�����
	public void setEts(Vector<EnemyTank> ets) {
		this.ets = ets;
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
	
	//�ж��Ƿ������˱��̹��
	public boolean isTouched(){
	boolean isTouched = false;
	
	switch (this.getDirection()){
		case UP:
			//�ҵ�̹������
			//ȡ�����е���̹��
			for(int i=0; i<ets.size(); i++){
				EnemyTank et = ets.get(i);
				//��������Լ�
				if(et!=this){
					//������˵ķ��������»�����
					if(et.getDirection()==Direction.UP
							||et.getDirection()==Direction.DOWN){
						//���
						if(getX()>=et.getX()&&getX()<=et.getX()+40
								&&getY()>=et.getY()
								&&getY()<=et.getY()+50)
							return true;
						//�ҵ�
						if(getX()+40>=et.getX()&&getX()+40<=et.getX()+40
								&&getY()>=et.getY()
								&&getY()<=et.getY()+50)
							return true;
					}
					if(et.getDirection()==Direction.LEFT
							||et.getDirection()==Direction.RIGHT){
						//���
						if(getX()>=et.getX()&&getX()<=et.getX()+50
								&&getY()>=et.getY()
								&&getY()<=et.getY()+40)
							return true;
						//�ҵ�
						if(getX()+40>=et.getX()&&getX()+40<=et.getX()+50
								&&getY()>=et.getY()
								&&getY()<=et.getY()+40)
							return true;
					}
				}
			}
			break;
		case DOWN:
			//̹������
			//ȡ�����е���̹��
			for(int i=0; i<ets.size(); i++){
				EnemyTank et = ets.get(i);
				//��������Լ�
				if(et!=this){
					//������˵ķ��������»�����
					if(et.getDirection()==Direction.UP
							||et.getDirection()==Direction.DOWN){
						//�ҵ����
						if(getX()>=et.getX()&&getX()<=et.getX()+40
								&&getY()+50>=et.getY()
								&&getY()+50<=et.getY()+50)
							return true;
						//�ҵ��ҵ�
						if(getX()+40>=et.getX()&&getX()+40<=et.getX()+40
								&&getY()+50>=et.getY()
								&&getY()+50<=et.getY()+50)
							return true;
					}
					if(et.getDirection()==Direction.LEFT
							||et.getDirection()==Direction.RIGHT){
						//���
						if(getX()>=et.getX()&&getX()<=et.getX()+50
								&&getY()+50>=et.getY()
								&&getY()+50<=et.getY()+40)
							return true;
						//�ҵ�
						if(getX()+40>=et.getX()&&getX()+40<=et.getX()+50
								&&getY()+50>=et.getY()
								&&getY()+50<=et.getY()+40)
							return true;
					}
				}
			}
			break;
		case LEFT:
			//̹������
			//ȡ�����е���̹��
			for(int i=0; i<ets.size(); i++){
				EnemyTank et = ets.get(i);
				//��������Լ�
				if(et!=this){
					//������˵ķ��������»�����
					if(et.getDirection()==Direction.UP
							||et.getDirection()==Direction.DOWN){
						//�ҵ���һ��
						if(getX()>=et.getX()&&getX()<=et.getX()+40
								&&getY()>=et.getY()
								&&getY()<=et.getY()+50)
							return true;
						//�ҵ���һ��
						if(getX()>=et.getX()&&getX()<=et.getX()+40
								&&getY()+40>=et.getY()
								&&getY()+40<=et.getY()+50)
							return true;
					}
					if(et.getDirection()==Direction.LEFT
							||et.getDirection()==Direction.RIGHT){
						//��һ��
						if(getX()>=et.getX()&&getX()<=et.getX()+50
								&&getY()>=et.getY()
								&&getY()+40<=et.getY()+40)
							return true;
						//��һ��
						if(getX()>=et.getX()&&getX()<=et.getX()+50
								&&getY()+40>=et.getY()
								&&getY()+40<=et.getY()+40)
							return true;
					}
				}
			}
			break;
		case RIGHT:
			//̹������
			//ȡ�����е���̹��
			for(int i=0; i<ets.size(); i++){
				EnemyTank et = ets.get(i);
				//��������Լ�
				if(et!=this){
					//������˵ķ��������»�����
					if(et.getDirection()==Direction.UP
							||et.getDirection()==Direction.DOWN){
						//�ϵ�
						if(getX()+50>=et.getX()&&getX()+50<=et.getX()+40
								&&getY()>=et.getY()
								&&getY()<=et.getY()+50)
							return true;
						//�µ�
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

//����̹��
class EnemyTank extends Tank implements Runnable {
	
	public EnemyTank(int x, int y) {
		super(x, y);
		//����̹������Ϊ����
		this.setType(0);
		//����̹�˳�ʼ��������
		this.setDirection(Direction.DOWN);
	}
	
	public EnemyTank(int x, int y, Direction dir) {
		super(x, y);
		//����̹������Ϊ����
		this.setType(0);
		//����̹�˳�ʼ��������
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
					//˵��̹������������
					for(int i=0; i<30; i++){
						//��֤����̹�˲����߽�
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
					//����
					for(int i=0; i<30; i++){
						//��֤����̹�˲����߽�
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
					//����
						for(int i=0; i<30; i++){
						//��֤����̹�˲����߽�
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
					//����
					for(int i=0; i<30; i++){
						//��֤����̹�˲����߽�
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
			
			//�жϵ���̹���Ƿ�����
			if(this.isDead()){
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
		this.setType(1);;
		this.setDirection(Direction.UP);
	}
	
}