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
	//���廭������
	//���廭����С
	public final static int width = 600;
	public final static int height = 500;
	
	//����һ���ҵ�̹��
	private Hero hero;
	
	//������˵�̹����
	//��Vectorȷ���̰߳�ȫ
	private Vector<EnemyTank> tks;
	
	//����̹������
	private final int maxTankNum = 5;
	
	//���屬ըЧ��ͼƬ
	private final Image images[] = new Image[32];
	//����ͼƬ�������һ��ը��
	private Vector<Explosion> explosion = new Vector<Explosion>();
	
	//���캯��
	public MyPanel(){
		
		//�ָ���¼
		Recorder.getRecord();
		
		tks = new Vector<EnemyTank>();
		hero = new Hero((MyPanel.width-40)/2, MyPanel.height-50);
		hero.setEts(tks);
		
		//��ʼ������̹��
		for (int i=0; i<maxTankNum; i++){
			//����һ�����˵�̹�˶���
			EnemyTank et = new EnemyTank((i+1)*70, 0);
			//��MyPanel�ĵ���̹�����������õ���̹������
			et.setEts(tks);
			
			//������̹����ӵ�����
			tks.add(et);
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
		
		//���ſ�ս����
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
		//��������
		for(EnemyTank et : this.tks)
			et.setEts(tks);
	}

	/**
	 * ������ʾ��Ϣ
	 */
	public void showInfo(Graphics g){
		//������ʾ��Ϣ̹�ˣ�(��̹�˲�����ս��)
		this.drawTank(g, new EnemyTank((int)(MyPanel.width*0.1), MyPanel.height+10));
		g.setColor(Color.black);
		g.drawString(Recorder.getEnNum()+"", (int)(MyPanel.width*0.1)+50, MyPanel.height+40);
		this.drawTank(g, new Hero((int)(MyPanel.width*0.3), MyPanel.height+10));
		g.setColor(Color.black);
		g.drawString(Recorder.getMyLife()+"", (int)(MyPanel.width*0.3)+50, MyPanel.height+40);
		
		//��������ܳɼ�
		g.setColor(Color.black);
		g.setFont(new Font("����", Font.BOLD, 20));
		g.drawString("�����ܳɼ� : " + Recorder.getAllEnNum(), (int)(MyPanel.width*0.4)+50, MyPanel.height+40);
	}
	
	/**
	 * ��Ⱦ����
	 */
	public void paint(Graphics g){
		
		super.paint(g);
		g.fillRect(0, 0, MyPanel.width, MyPanel.height);
		//������ʾ��Ϣ
		this.showInfo(g);
		
		/**
		 * ���Լ���̹��
		 */
		if(!hero.isDead()){	//������̹��û�����У��򻭳�
			this.drawTank(g, hero);
		}		
		
		//��bullets��ȡ���ӵ���������
		for(int i=0; i<hero.getBullets().size(); i++){
			
			Bullet bullet = hero.getBullets().get(i);
//			if(bullet!=null&&!bullet.isDead()){
//				this.drawBullet(g, bullet);
//			}	
			if(bullet.isDead()){
				//��bullets��ɾ�����ӵ�
				hero.getBullets().remove(bullet);
				//���ӵ�ʵ�����ڴ�ɾ��
				bullet = null;
			}
			else {
				this.drawBullet(g, bullet);
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
//					if(bullet!=null&&!bullet.isDead()){
//						this.drawBullet(g, bullet);
//					}
					if(bullet.isDead()){
						//��bullets��ɾ�����ӵ�
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
	 * дһ������ר���ж��ӵ��Ƿ����̹��
	 */
	public boolean hitTank(Bullet bullet, Tank tank){
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
					//����true,˵��������
					return true;
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
					//����true,˵��������
					return true;
				}
		}
		return false;
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
	 * �жϵط�̹���Ƿ�ȫ����
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
						if(this.hitTank(bullet, hero))
							hero.setDead(true);
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
				//��֤���̹�˲����߽�
				if(hero.getY()<MyPanel.height-50&&!hero.isTouched()){
					hero.move(Direction.DOWN);
				}
				//����ƶ��ᳬ���߽磬��ֻ�ı䷽�򣬲��ı�̹������
				else{
					hero.setDirection(Direction.DOWN);
				}
				break;
			case KeyEvent.VK_UP :
				//��֤���̹�˲����߽�
				if(hero.getY()>0&&!hero.isTouched()){
					hero.move(Direction.UP);
				}
				//����ƶ��ᳬ���߽磬��ֻ�ı䷽�򣬲��ı�̹������
				else{
					hero.setDirection(Direction.UP);
				}
				break;
			case KeyEvent.VK_LEFT :
				//��֤���̹�˲����߽�
				if(hero.getX()>0&&!hero.isTouched()){
					hero.move(Direction.LEFT);
				}
				//����ƶ��ᳬ���߽磬��ֻ�ı䷽�򣬲��ı�̹������
				else{
					hero.setDirection(Direction.LEFT);
				}
				break;
			case KeyEvent.VK_RIGHT :
				//��֤���̹�˲����߽�
				if(hero.getX()<MyPanel.width-50&&!hero.isTouched()){
					hero.move(Direction.RIGHT);
				}
				//����ƶ��ᳬ���߽磬��ֻ�ı䷽�򣬲��ı�̹������
				else{
					hero.setDirection(Direction.RIGHT);
				}
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
	public void keyReleased(KeyEvent e) {}

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
			if(!AllDead()){
				hitEnemy();
			}
			//�ж��Ƿ�����ҷ�̹��
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
 * ����һ����ʾ����
 */
class MyStartPanel extends JPanel implements Runnable {
	
	private boolean times = true;
	
	private static final long serialVersionUID = 1L;
	public void paint(Graphics g){
		super.paint(g);
		g.fillRect(0, 0, MyPanel.width, MyPanel.height);
		
		g.setColor(Color.yellow);
		//������Ϣ������
		Font myFont = new Font("������κ", Font.BOLD, 30);
		g.setFont(myFont);
		
		if(times){
			g.drawString("stage 1", (MyPanel.width-105)/2, (MyPanel.height-30)/2);
		}
	}
	@Override
	public void run() {
		while(true){
			//����
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			times = !times;
			//�ػ�
			this.repaint();
		}
	}
}




