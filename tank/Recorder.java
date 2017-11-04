package tank;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

/**
 * 记录类，同时也可以保存玩家的设置
 */
public class Recorder {

	//记录每关有多少敌人
	private static int enNum = 20;
	//设置我有多少条命
	private static int myLife = 3;
	//记录总共消灭了多少敌人
	private static int allEnNum = 0;
	//从文件中恢复记录点
	private static Vector<Node> nodes = new Vector<Node>();
	
	private static FileWriter fw = null;
	private static BufferedWriter bw = null;
	private static FileReader fr = null;
	private static BufferedReader br = null;
	
	private static Vector<EnemyTank> ets;
	
	//保存击毁敌人的数量和敌人坦克坐标，方向
	public static void keepRecAndEnemyTank() {
		try {
			//创建
			fw = new FileWriter("d:/myRecord.txt");
			bw = new BufferedWriter(fw);
			
			bw.write(allEnNum+"\r\n");
			
			//保存当前活的敌人坦克的坐标和方向
			for (int i=0; i<ets.size(); i++) {
				//取出第一个坦克
				EnemyTank et = ets.get(i);
				
				if(!et.isDead()) {
					//活的就保存
					String record = et.getX() + " "
							+ et.getY() + " " + et.getDirection();
					
					//写入
					bw.write(record + "\r\n");
				}
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		finally{
			
			//关闭流
			try {
				//后开先关闭
				bw.close();
				fw.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//把玩家击毁敌人坦克数量保存到文件中
	public static void keepRecord(){
		try {
			//创建
			fw = new FileWriter("d:/myRecord.txt");
			bw = new BufferedWriter(fw);
			
			bw.write(allEnNum+"\r\n");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		finally{
			
			//关闭流
			try {
				//后开先关闭
				bw.close();
				fw.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//从文件中读取记录
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
				//后打开先关闭
				br.close();
				fr.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//读取节点
	public static Vector<EnemyTank> readLastGame(){
		Vector<EnemyTank> ets = new Vector<EnemyTank>();
		try {
			fr = new FileReader("d:/myRecord.txt");
			br = new BufferedReader(fr);
			//玩家记录
			String n = br.readLine();
			allEnNum = Integer.parseInt(n);

			//读敌人坦克参数(坐标，方向)
			while((n = br.readLine())!=null){
				
				//split()方法返回一个以传入字串为分隔符的字串数组
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
				//后打开先关闭
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

	//减少敌人数量
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

/* 记录点 */
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