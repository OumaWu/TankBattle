package tank;

public class Explosion {
	
	//���屬ըЧ��������
	private int x, y;
	//ը��������
	private int life = 31;
	private boolean isLive = true;
	
	public Explosion(int x, int y){
		this.setX(x);
		this.setY(y);
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

	public boolean isLive() {
		return isLive;
	}

	public int getLife() {
		return life;
	}
	
	//��������ֵ
	public void lifeDown(){
		if(life>0){
			life--;
		}
		else{
			this.isLive = false;
		}
	}
	
}












