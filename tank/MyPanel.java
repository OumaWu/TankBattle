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
	
	//���廭������
	//���廭����С
	private final static int width = 1000;
	private final static int height = 800;
	
	//����һ���ҵ�̹��
	private Hero hero;
	
	//������˵�̹����
	//��Vectorȷ���̰߳�ȫ
	private Vector<Tank> tks = new Vector<Tank>();
	
	//����̹������
	private final int maxTankNum = 10;
	
	//���屬ըЧ��ͼƬ
	private final Image images[] = new Image[32];
	//����ͼƬ�������һ��ը��
	private Vector<Explosion> explosion = new Vector<Explosion>();
	
	//���캯��
	public MyPanel(){
		hero = new Hero(460, 750);
		
		//��ʼ������̹��
		for (int i=0; i<maxTankNum; i++){
			//����һ�����˵�̹�˶���
			tks.add(new Tank((i+2)*70, 0));
			
			//�������˵�̹��
			Thread t = new Thread(tks.get(i));
			t.start();
			
		}
		
		//��ʼ��ͼƬ
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
	 * ��Ⱦ����
	 */
	public void paint(Graphics g){
		
		super.paint(g);
		g.fillRect(0, 0, MyPanel.width, MyPanel.height);
		
		/**
		 * ���Լ���̹��
		 */
		if(!hero.isDead()){	//������̹��û�����У��򻭳�
			this.drawTank(g, hero);
		}		
		
		//��bullets��ȡ���ӵ���������
		for(int i=0; i<hero.getBullets().size(); i++){
			
			Bullet bullet = hero.getBullets().get(i);
			
			if(bullet!=null&&!bullet.isDead()){
				this.drawBullet(g, bullet);
			}
			
			if(bullet.isDead()){
				//��bullets��ɾ�����ӵ�
				hero.getBullets().remove(bullet);
				//���ӵ�ʵ�����ڴ�ɾ��
				bullet = null;
			}
		}
		
		//������ըЧ��
		for(int i=0; i<explosion.size(); i++){
			//ȡ��ը��
			Explosion exp = explosion.get(i);
			g.drawImage(images[31-exp.getLife()], exp.getX()-85
					, exp.getY()-65, 200, 200, this);
			exp.lifeDown();
			
			//���ը������ֵΪ0���Ͱ�����ȥ��
			if(!exp.isLive()){
				explosion.remove(exp);
				//��ը��ʵ�����ڴ����
				exp = null;
			}
		}
		
		//�����˵�̹��
		for(Tank tank : tks){
			if(!tank.isDead()){
				this.drawTank(g, tank);
				//�ٻ����˵��ӵ�
				for(int i=0; i<tank.getBullets().size(); i++){
					
					Bullet bullet = tank.getBullets().get(i);
					
					if(bullet!=null&&!bullet.isDead()){
						this.drawBullet(g, bullet);
					}
					
					if(bullet.isDead()){
						//��bullets��ɾ�����ӵ�
						hero.getBullets().remove(bullet);
					}
				}
			}
		}
			
	}

	/**
	 * дһ������ר���ж��ӵ��Ƿ����̹��
	 */
	public void hitTank(Bullet bullet, Tank tank){
		//�ж�̹�˵ķ���
		switch(tank.getDirection()){
			case UP :
			case DOWN :
				if(bullet.getX()>=tank.getX()&&
						bullet.getX()<=tank.getX()+40&&
						bullet.getY()>=tank.getY()&&
						bullet.getY()<=tank.getY()+50){
					//����
					//�ӵ�����
					bullet.setIsDead(true);
					//̹������
					tank.setDead(true);
					//����һ��ը��������Vector
					this.explosion.add(new Explosion(tank.getX(), tank.getY()));
					//��̹��ʵ�����ڴ�ɾ��
					tank = null;
				}
				break;
			case LEFT :
			case RIGHT :
				if(bullet.getX()>=tank.getX()&&
						bullet.getX()<=tank.getX()+50&&
						bullet.getY()>=tank.getY()&&
						bullet.getY()<=tank.getY()+40){
					//����
					//�ӵ�����
					bullet.setIsDead(true);
					//̹������
					tank.setDead(true);
					//����һ��ը��������Vector
					this.explosion.add(new Explosion(tank.getX(), tank.getY()));
					//��̹��ʵ�����ڴ�ɾ��
					tank = null;
				}
		}
	}
	
	/**
	 * ��̹�˷���
	 */
	public void drawTank(Graphics g, Tank tank){
		
		//�ж���ʲô���͵�̹��
		switch(tank.getType()){
			case 0:
				g.setColor(Color.YELLOW);
				break;
			case 1:
				g.setColor(Color.CYAN);
				break;
		}
		
		/**
		 * �����ҵ�̹��
		 */
		//�ж�̹�˷���
		switch(tank.getDirection()){
			case UP :
				//����Ͳ
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
	 * �����ŵ�̹�˳���
	 */
	public void drawVerticalTank(Graphics g, Tank tank){
		//������Ĵ�
		g.fill3DRect(tank.getX(), tank.getY(), 10, 50, false);
		//���ұ��Ĵ�
		g.fill3DRect(tank.getX()+30, tank.getY(), 10, 50, false);
		//���м䳵��
		g.fill3DRect(tank.getX()+10, tank.getY()+10, 20, 30, false);
		//���ڼ�
		g.fillOval(tank.getX()+10, tank.getY()+15, 20, 20);
	}
	
	/**
	 * �����ŵ�̹�˳���
	 */
	public void drawHorizontalTank(Graphics g, Tank tank){
		//���ϱ��Ĵ�
		g.fill3DRect(tank.getX(), tank.getY(), 50, 10, false);
		//���±��Ĵ�
		g.fill3DRect(tank.getX(), tank.getY()+30, 50, 10, false);
		//���м䳵��
		g.fill3DRect(tank.getX()+10, tank.getY()+10, 30, 20, false);
		//���ڼ�
		g.fillOval(tank.getX()+15, tank.getY()+10, 20, 20);
	}
	
	/**
	 * ���ӵ�����
	 */
	public void drawBullet(Graphics g, Bullet bullet){
		g.draw3DRect(bullet.getX(),
				bullet.getY(),
				2, 2, false);
	}
	
	/**
	 * �ж��Ƿ���ез�̹��
	 */
	public void hitEnemy(){
		//ȡ���ӵ�			
		for(Bullet bullet : hero.getBullets()){
			//�ж��ӵ��Ƿ���Ч
			if(!bullet.isDead()){
				//ȡ��ÿ��̹�ˣ������ж�
				for(Tank tank : tks){
					//ȡ��̹��
					if(!tank.isDead())
						this.hitTank(bullet, tank);
				}
			}
		}
	}
	
	/**
	 * �ж��Ƿ�����ҷ�̹��
	 */
	public void hitMe(){
		//ȡ��ÿ��̹��
		for(Tank tank : tks){
			//ȡ��ÿ���з�̹�˵�ÿ���ӵ�
			for(Bullet bullet : tank.getBullets()){
				//�ж��ӵ��Ƿ���Ч
				if(!bullet.isDead()){
					//�����̹���ж�
					if(!hero.isDead()){
						this.hitTank(bullet, hero);
					}
				}
			}
		}
	}
	
	/**
	 * ����̹��
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		//�����ж����̹���Ƿ�����
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
				//����
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
		//ÿ��100����������Ⱦ
		while(true){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//�ж��Ƿ���ез�̹��
			hitEnemy();
			//�ж��Ƿ�����ҷ�̹��
			hitMe();
			this.repaint();
		}
	}
	
}