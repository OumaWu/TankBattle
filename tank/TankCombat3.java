package tank;
import javax.swing.*;
/**
 * 坦克大战游戏4.0
 * 1.画出坦克
 * 2.坦克可以上下左右移动
 * 3.坦克可以发射移动的子弹
 * 4.子弹可以连发(最多5课)
 * 5.敌方坦克被击中后有爆炸效果
 * 6.敌方坦克能自由随机运动
 * 7.控制坦克的移动范围(在画布以内)
 */
public class TankCombat3 extends JFrame {

	private MyPanel mp;
	private Thread t;
	
	public TankCombat3(){
		
		mp = new MyPanel();
		t = new Thread(mp);
		
		this.addKeyListener(mp);
		this.add(mp);
		this.setTitle("坦克大战");
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
		





