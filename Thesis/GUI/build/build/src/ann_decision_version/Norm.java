package ann_decision_version;

public class Norm {
	
	public static Double get_norm(Double[] point_a, Double[] point_b){
		
		double sum = 0 ;
		
		for (int i = 0; i < point_a.length; i++) {
			
			sum += ( point_a[i] - point_b[i])* ( point_a[i] - point_b[i] ) ;
			
		}
		
		return Math.sqrt(sum);
		
	}
	
	public static double get_norm(double[] point_a,double[] point_b){
		
		double sum = 0 ;
		
		for (int i = 0; i < point_a.length; i++) {
			
			sum += ( point_a[i] - point_b[i] )* ( point_a[i] - point_b[i] ) ;
			
		}
		
		sum = sum/(point_a.length*(point_a.length - 1));
		
		return Math.sqrt(sum);
		
	}
	
	public static double get_norm(Integer[] point_a,Integer[] point_b){
		
		double sum = 0 ;
		
		for (int i = 0; i < point_a.length; i++) {
			
			sum += ( point_a[i] - point_b[i])* ( point_a[i] - point_b[i] ) ;
			
		}
		
		return Math.sqrt(sum);
		
	}
	
	public static double get_norm(int[] point_a,Double[] point_b){
		
		double sum = 0 ;
		
		for (int i = 0; i < 7; i++) {
			
			sum += ( point_a[i] - point_b[i])* ( point_a[i] - point_b[i] ) ;
			
		}
		
		return Math.sqrt(sum);
		
	}
	
	public static double get_norm(int[] point_a,int[] point_b){
		
		double sum = 0 ;
		
		for (int i = 0; i < 7; i++) {
			
			sum += ( point_a[i] - point_b[i])* ( point_a[i] - point_b[i] ) ;
			
		}
		
		return Math.sqrt(sum);
		
	}

}
