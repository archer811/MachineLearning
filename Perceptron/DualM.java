package ML;

public class DualM {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    /**
     * 输入数据集 按照书本来的
     */
    int[] x0 = {3,4,1};
    int[] x1 = {3,3,1};
    int[] y = {1,1,-1};
    int b = 0,n = 1;
    /**
     * 对偶形式 的a值
     */
    int[] a = {0,0,0};
    for(int i = 0 ;; i++) {
      int mask =0 ;
      for(int j = 0; j < 3; j++) {
        int w1 =0,w2 = 0;
        w1 = a[0]*x0[0]*y[0]+a[1]*x0[1]*y[1]+a[2]*x0[2]*y[2];
        w2 = a[0]*x1[0]*y[0]+a[1]*x1[1]*y[1]+a[2]*x1[2]*y[2];
       if(y[j]*(w1*x0[j]+b)+y[j]*(w2*x1[j]+b)<=0 && mask==0) {
         //修改第j维的参数
         a[j] = a[j] + n;
         b = b + y[j];
         System.out.println(i+":"+a[0]+","+a[1]+","+a[2]+","+b);
         mask=1;
       }
      }
      if(mask==0)break;
    }
  }

}
