package tank;
import javax.swing.*;
/**
 * ̹�˴�ս��Ϸ4.0
 * 1.����̹��
 * 2.̹�˿������������ƶ�
 * 3.̹�˿��Է����ƶ����ӵ�
 * 4.�ӵ���������(���5��)
 * 5.�з�̹�˱����к��б�ըЧ��
 * 6.�з�̹������������˶�
 * 7.����̹�˵��ƶ���Χ(�ڻ�������)
 */
public class TankCombat3 extends JFrame {

	private MyPanel mp;
	private Thread t;
	
	public TankCombat3(){
		
		mp = new MyPanel();
		t = new Thread(mp);
		
		this.addKeyListener(mp);
		this.add(mp);
		this.setTitle("̹�˴�ս");
		this.setSize(1000,900);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void startGame(){
		this.t.start();
	}
	
	public static void main(String[] args) {
		new TankCombat3().startGame();
	}
}
		





