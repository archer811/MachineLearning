package ML;

public class PerceptionM {

  public static void main(String[] args) { 

    /**
     * 输入数据集 按照书本来的
     */
    int[] x0 = {3,4,1};
    int[] x1 = {3,3,1};
    int[] y = {1,1,-1};
    /**
     * w0,w1 表示输入空间的第1，2维模型参数
     */
    int w0 = 1, w1 =1, b = 1;
    for(int i = 0; ; i++) {
      int mask=0;
      for(int j =0;j < 3;j++) {
        //存在误分类点
        if(y[j]*(w0*x0[j]+w1*x1[j]+b) <=0 && mask ==0) {
          // 进行修正
          w0 = w0 + y[j]*x0[j];
          w1 = w1 + y[j]*x1[j];
          b = b + y[j];
          System.out.println(i+":"+w0+","+w1+","+b + "-------------" + j);
          mask=1;
        }
      }
      if(mask==0)break;
    }
  }
  
}
