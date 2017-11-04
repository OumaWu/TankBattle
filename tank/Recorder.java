package tank;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

/**
 * ��¼�࣬ͬʱҲ���Ա�����ҵ�����
 */
public class Recorder {

	//��¼ÿ���ж��ٵ���
	private static int enNum = 20;
	//�������ж�������
	private static int myLife = 3;
	//��¼�ܹ������˶��ٵ���
	private static int allEnNum = 0;
	//���ļ��лָ���¼��
	private static Vector<Node> nodes = new Vector<Node>();
	
	private static FileWriter fw = null;
	private static BufferedWriter bw = null;
	private static FileReader fr = null;
	private static BufferedReader br = null;
	
	private static Vector<EnemyTank> ets;
	
	//������ٵ��˵������͵���̹�����꣬����
	public static void keepRecAndEnemyTank() {
		try {
			//����
			fw = new FileWriter("d:/myRecord.txt");
			bw = new BufferedWriter(fw);
			
			bw.write(allEnNum+"\r\n");
			
			//���浱ǰ��ĵ���̹�˵�����ͷ���
			for (int i=0; i<ets.size(); i++) {
				//ȡ����һ��̹��
				EnemyTank et = ets.get(i);
				
				if(!et.isDead()) {
					//��ľͱ���
					String record = et.getX() + " "
							+ et.getY() + " " + et.getDirection();
					
					//д��
					bw.write(record + "\r\n");
				}
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		finally{
			
			//�ر���
			try {
				//���ȹر�
				bw.close();
				fw.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//����һ��ٵ���̹���������浽�ļ���
	public static void keepRecord(){
		try {
			//����
			fw = new FileWriter("d:/myRecord.txt");
			bw = new BufferedWriter(fw);
			
			bw.write(allEnNum+"\r\n");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		finally{
			
			//�ر���
			try {
				//���ȹر�
				bw.close();
				fw.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//���ļ��ж�ȡ��¼
	public static void getRecord(){
		try {
			fr = new FileReader("d:/myRecord.txt");
			br = new BufferedReader(fr);
			
			String n = br.readLine();
			allEnNum = Integer.parseInt(n);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			
			try {
				//����ȹر�
				br.close();
				fr.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//��ȡ�ڵ�
	public static Vector<EnemyTank> readLastGame(){
		Vector<EnemyTank> ets = new Vector<EnemyTank>();
		try {
			fr = new FileReader("d:/myRecord.txt");
			br = new BufferedReader(fr);
			//��Ҽ�¼
			String n = br.readLine();
			allEnNum = Integer.parseInt(n);

			//������̹�˲���(���꣬����)
			while((n = br.readLine())!=null){
				
				//split()��������һ���Դ����ִ�Ϊ�ָ������ִ�����
				String[] xyz = n.split(" ");
				
				Node node = new Node(Integer.parseInt(xyz[0]),
						Integer.parseInt(xyz[1]),
								xyz[2]);
				nodes.add(node);
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			
			try {
				//����ȹر�
				br.close();
				fr.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		for (Node node : nodes){
			ets.add(new EnemyTank(node.x, node.y, node.direction));
		}
		
		return ets;
	}
	
	public static int getEnNum() {
		return enNum;
	}
	
	public static void setEnNum(int enNum) {
		Recorder.enNum = enNum;
	}
	
	public static int getMyLife() {
		return myLife;
	}
	
	public static void setMyLife(int myLife) {
		Recorder.myLife = myLife;
	}
	
	public static int getAllEnNum() {
		return allEnNum;
	}

	public static void setAllEnNum(int allEnNum) {
		Recorder.allEnNum = allEnNum;
	}

	//���ٵ�������
	public static void reduceEnNum(){
		enNum--;
	}
	
	public static void addAllEnNum(){
		allEnNum++;
	}

	public Vector<EnemyTank> getEts() {
		return ets;
	}

	public static void setEts(Vector<EnemyTank> ets) {
		Recorder.ets = ets;
	}

	public static Vector<Node> getNodes() {
		return nodes;
	}

	public static void setNodes(Vector<Node> nodes) {
		Recorder.nodes = nodes;
	}
	
}

/* ��¼�� */
class Node {
	int x;
	int y;
	Direction direction;
	
	public Node(int x, int y, String direction) {
		this.x = x;
		this.y = y;
		this.direction = this.castDirection(direction);
	}
	
	public Direction castDirection(String dir){
		switch(dir.toUpperCase()){
			case "UP":
				return Direction.UP;
			case "DOWN":
				return Direction.DOWN;
			case "LEFT":
				return Direction.LEFT;
			case "RIGHT":
				return Direction.RIGHT;
		}
		return null;
	}

}