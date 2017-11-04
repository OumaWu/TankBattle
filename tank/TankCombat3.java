package tank;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
/**
 * 坦克大战游戏4.0
 * 1.画出坦克
 * 2.双方坦克可以上下左右移动
 * 3.坦克可以发射连发子弹(最多5课)
 * 4.当子弹击中敌方/我方坦克，坦克消失
 * 5.敌方坦克被击中后有爆炸效果
 * 6.防止敌人坦克重叠
 * 	6.1决定把判断是否碰撞的函数写到Tank类
 * 7.控制坦克的移动范围(在画布以内)
 * 8.可以分关*
 * 	8.1做一个开始的Panel,它是一个空Panel
 * 	8.2闪烁效果
 * 9.可以暂停游戏和继续*
 * 	9.1当用户点击暂停时，子弹的速度和坦克的速度为0，并让坦克的方向不要变化
 * 10.可以记录玩家的成绩
 * 	10.1用文件流
 * 	10.2单写一个记录类，完成对玩家记录
 * 	10.3先完成保存共击毁了多少辆敌人坦克的功能
 * 	10.4存盘退出游戏，可以记录当时的敌人坦克坐标，并可以恢复
 * 11.java如何操作声音文件
 * 12.网络大战*(待讲解)
 */
@SuppressWarnings("serial")
public class TankCombat3 extends JFrame implements ActionListener {
	
	//	定义一个开始面板
	MyStartPanel msp = null;
	private MyPanel mp;
	private Thread t1;
	
	//定义一个开始面板
	private JMenuBar jmb = null;
	//开始游戏
	private JMenu jm1 = null;
	private JMenuItem jmi1 = null;
	//退出系统
	private JMenuItem jmi2 = null;
	//存盘
	private JMenuItem jmi3 = null;
	//恢复上局
	private JMenuItem jmi4 = null;
	
	public TankCombat3(){
		
		//创建菜单及菜单选项
		msp = new MyStartPanel();
		jmb = new JMenuBar();
		jm1 = new JMenu("游戏(G)");
		jmi1 = new JMenuItem("开始新游戏(N)");
		jmi2 = new JMenuItem("退出游戏(E)");
		jmi3 = new JMenuItem("存盘退出游戏(C)");
		jmi4 = new JMenuItem("继续上局游戏(S)");

		//设置快捷方式
		jm1.setMnemonic('G');
		jmi1.setMnemonic('N');
		jmi2.setMnemonic('E');
		jmi3.setMnemonic('C');
		jmi4.setMnemonic('S');
		
		//注册监听
		jmi1.addActionListener(this);
		jmi2.addActionListener(this);
		jmi3.addActionListener(this);
		jmi4.addActionListener(this);

		//设置指令	
		jmi1.setActionCommand("newgame");
		jmi2.setActionCommand("exit");
		jmi3.setActionCommand("saveExit");
		jmi4.setActionCommand("lastGame");
		
		//添加组件
		jmb.add(jm1);
		jm1.add(jmi1);
		jm1.add(jmi4);
		jm1.add(jmi2);
		jm1.addSeparator();
		jm1.add(jmi3);

		//窗口初始化
		t1 = new Thread(msp);
		this.setJMenuBar(jmb);
		this.add(msp);
		this.setTitle("坦克大战");
		this.setSize(MyPanel.width+30,MyPanel.height+150);
		this.setLocation(300, 100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void startGame(){
		this.t1.start();
	}
	
	public static void main(String[] args) {
		new TankCombat3().startGame();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//对用户不同点击做出不同的处理
		if(e.getActionCommand().equals("newgame")){
			//先删除旧的开始面板
			if(msp!=null)
				this.remove(msp);
			if(mp!=null)
				this.remove(mp);
			
			//创建战场面板
			mp = new MyPanel();
			this.add(mp);
			this.addKeyListener(mp);
			//显示，刷新JFrame
			this.setVisible(true);
			//启动线程
			Thread t2 = new Thread(mp);
			t2.start();
			//启动敌人坦克线程
			mp.setEnemyThread();
		}
		else if (e.getActionCommand().equals("exit")){
			
			//用户点击了退出系统的菜单
			//保存击毁敌人数量
			Recorder.keepRecord();
			
			System.exit(0);
		}//对存盘退出做处理
		else if (e.getActionCommand().equals("saveExit")) {
			
			//做工作
			//保存击毁敌人的数量和敌人的坐标
			Recorder.setEts(mp.getTks());
			Recorder.keepRecAndEnemyTank();
			
			//退出
			System.exit(0);
		}//继续上局游戏
		else if (e.getActionCommand().equals("lastGame")) {
			//先删除旧的开始面板
			if(msp!=null)
				this.remove(msp);
			if(mp!=null)
				this.remove(mp);
			
			//创建战场面板
			mp = new MyPanel();
			//设置上局敌人坦克的参数
			mp.setTks(Recorder.readLastGame());
			this.add(mp);
			this.addKeyListener(mp);
			//显示，刷新JFrame
			this.setVisible(true);
			//启动线程
			Thread t2 = new Thread(mp);
			t2.start();
			//启动敌人坦克线程
			mp.setEnemyThread();
		}
	}
}

		





