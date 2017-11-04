package tank;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
/**
 * ̹�˴�ս��Ϸ4.0
 * 1.����̹��
 * 2.˫��̹�˿������������ƶ�
 * 3.̹�˿��Է��������ӵ�(���5��)
 * 4.���ӵ����ез�/�ҷ�̹�ˣ�̹����ʧ
 * 5.�з�̹�˱����к��б�ըЧ��
 * 6.��ֹ����̹���ص�
 * 	6.1�������ж��Ƿ���ײ�ĺ���д��Tank��
 * 7.����̹�˵��ƶ���Χ(�ڻ�������)
 * 8.���Էֹ�*
 * 	8.1��һ����ʼ��Panel,����һ����Panel
 * 	8.2��˸Ч��
 * 9.������ͣ��Ϸ�ͼ���*
 * 	9.1���û������ͣʱ���ӵ����ٶȺ�̹�˵��ٶ�Ϊ0������̹�˵ķ���Ҫ�仯
 * 10.���Լ�¼��ҵĳɼ�
 * 	10.1���ļ���
 * 	10.2��дһ����¼�࣬��ɶ���Ҽ�¼
 * 	10.3����ɱ��湲�����˶���������̹�˵Ĺ���
 * 	10.4�����˳���Ϸ�����Լ�¼��ʱ�ĵ���̹�����꣬�����Իָ�
 * 11.java��β��������ļ�
 * 12.�����ս*(������)
 */
@SuppressWarnings("serial")
public class TankCombat3 extends JFrame implements ActionListener {
	
	//	����һ����ʼ���
	MyStartPanel msp = null;
	private MyPanel mp;
	private Thread t1;
	
	//����һ����ʼ���
	private JMenuBar jmb = null;
	//��ʼ��Ϸ
	private JMenu jm1 = null;
	private JMenuItem jmi1 = null;
	//�˳�ϵͳ
	private JMenuItem jmi2 = null;
	//����
	private JMenuItem jmi3 = null;
	//�ָ��Ͼ�
	private JMenuItem jmi4 = null;
	
	public TankCombat3(){
		
		//�����˵����˵�ѡ��
		msp = new MyStartPanel();
		jmb = new JMenuBar();
		jm1 = new JMenu("��Ϸ(G)");
		jmi1 = new JMenuItem("��ʼ����Ϸ(N)");
		jmi2 = new JMenuItem("�˳���Ϸ(E)");
		jmi3 = new JMenuItem("�����˳���Ϸ(C)");
		jmi4 = new JMenuItem("�����Ͼ���Ϸ(S)");

		//���ÿ�ݷ�ʽ
		jm1.setMnemonic('G');
		jmi1.setMnemonic('N');
		jmi2.setMnemonic('E');
		jmi3.setMnemonic('C');
		jmi4.setMnemonic('S');
		
		//ע�����
		jmi1.addActionListener(this);
		jmi2.addActionListener(this);
		jmi3.addActionListener(this);
		jmi4.addActionListener(this);

		//����ָ��	
		jmi1.setActionCommand("newgame");
		jmi2.setActionCommand("exit");
		jmi3.setActionCommand("saveExit");
		jmi4.setActionCommand("lastGame");
		
		//������
		jmb.add(jm1);
		jm1.add(jmi1);
		jm1.add(jmi4);
		jm1.add(jmi2);
		jm1.addSeparator();
		jm1.add(jmi3);

		//���ڳ�ʼ��
		t1 = new Thread(msp);
		this.setJMenuBar(jmb);
		this.add(msp);
		this.setTitle("̹�˴�ս");
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
		//���û���ͬ���������ͬ�Ĵ���
		if(e.getActionCommand().equals("newgame")){
			//��ɾ���ɵĿ�ʼ���
			if(msp!=null)
				this.remove(msp);
			if(mp!=null)
				this.remove(mp);
			
			//����ս�����
			mp = new MyPanel();
			this.add(mp);
			this.addKeyListener(mp);
			//��ʾ��ˢ��JFrame
			this.setVisible(true);
			//�����߳�
			Thread t2 = new Thread(mp);
			t2.start();
			//��������̹���߳�
			mp.setEnemyThread();
		}
		else if (e.getActionCommand().equals("exit")){
			
			//�û�������˳�ϵͳ�Ĳ˵�
			//������ٵ�������
			Recorder.keepRecord();
			
			System.exit(0);
		}//�Դ����˳�������
		else if (e.getActionCommand().equals("saveExit")) {
			
			//������
			//������ٵ��˵������͵��˵�����
			Recorder.setEts(mp.getTks());
			Recorder.keepRecAndEnemyTank();
			
			//�˳�
			System.exit(0);
		}//�����Ͼ���Ϸ
		else if (e.getActionCommand().equals("lastGame")) {
			//��ɾ���ɵĿ�ʼ���
			if(msp!=null)
				this.remove(msp);
			if(mp!=null)
				this.remove(mp);
			
			//����ս�����
			mp = new MyPanel();
			//�����Ͼֵ���̹�˵Ĳ���
			mp.setTks(Recorder.readLastGame());
			this.add(mp);
			this.addKeyListener(mp);
			//��ʾ��ˢ��JFrame
			this.setVisible(true);
			//�����߳�
			Thread t2 = new Thread(mp);
			t2.start();
			//��������̹���߳�
			mp.setEnemyThread();
		}
	}
}

		





