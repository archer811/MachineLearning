package ML;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Tree {
  /**
   * 父节点，左右子节点
   */
  Tree left=null;
  Tree right=null;
  Tree point=null;
  Tree parent=null;
  /**
   * 案例是两维，其实是有多少维，就应该有多少个值
   */
  double x1,x2;
  // 这个节点处于第几层
  int depth;
  // 属于原来 输入数据队列 的第几个元素
  int id=-1;
  public void setX1(double _x1) {
    x1= _x1;
  }
  public void setX2(double _x2) {
    x2= _x2;
  }
  public Tree(double _x1, double _x2) {
    x1 = _x1;
    x2 = _x2;
  }
  public void setParent(Tree p) {
    parent = p;
  }
  // 边界值 ,每一维有两个边界值，边界值不存在的时候就是无穷大/无穷小
  double[][] boundary_value = {{-2147483648,2147483647},{-2147483648,2147483647}};
  // 标记每一维 边界值是否确定
  boolean[][] mask = {{false,false},{false,false}};
  
}
/***
 * 方便pair数的排序
 * @author zhouxiaojie
 *
 * @param <K>
 * @param <V>
 * @param <W>
 */
class KeyValuePair<K extends Comparable<K>, V extends Comparable<V>, W extends Comparable<W>> implements Comparable<KeyValuePair<K, V, W>> {
  K key;
  V value;
  W id;
  
  public KeyValuePair(K key, V value, W id) {
    this.key = key;
    this.value = value;
    this.id = id;
  }
  // 此处可定义按照key或value的正序或逆序排序
  @Override
  public int compareTo(KeyValuePair<K, V, W> o) {
    return key.compareTo(o.getKey());
  }
  public K getKey() {
    return key;
  }
  public V getValue() {
    return value;
  }
  public W getW() {
    return id;
  }
}
public class KDTree {
  //static double[] x1 = {2,5,9,4,8,7};
  //static double[] x2 = {3,4,6,7,1,2};
  /***
   * 输入数据dataSet：x1,x2数组
   * used[] 是在构建 kdTree的时候标记是否已经走过
   * mk[] 是在搜索kdTree的时候标记是否已经搜索过
   * N 是输入数据量大小
   */
  static double[] x2 = {3,6,4,8,7,1,2};
  static double[] x1 = {4,2,6,3,5,1,7};
  static int[] used = {0,0,0,0,0,0,0,0};
  static int[] mk = {0,0,0,0,0,0,0,0};
  static int N = 7;
  
  /**
   * 判断 当前点是否 在边界范围内，如果在返回1，否则返回0
   * @return
   */
  static int judge(boolean x1Min,boolean x1Max,boolean x2Min,boolean x2Max,double _x1Min,double _x1Max,double _x2Min,double _x2Max,double x1,double x2) {
    if(x1Min == true && x1 < _x1Min) return 0;
    if(x1Max == true && x1 > _x1Max) return 0;
    if(x2Min == true && x2 < _x2Min) return 0;
    if(x2Max == true && x2 > _x2Max) return 0;
    return 1;
  }

  
  /***
   * 递归构建kdTree
   * @param point
   * @param depth
   * @return
   */
  public static Tree buildTree(Tree point, int depth) {
    //深度为j， l = (j mod k) 
    // 根据point 的边界， 取出满足边界的点，排序， 取中位数
    int l = depth%2;
    List<KeyValuePair<Double, Double, Integer>> ts = new ArrayList<KeyValuePair<Double, Double, Integer>>();  
    for(int i=0;i<N;i++) {
      if(used[i]==1)continue;
      if(judge(point.mask[0][0],point.mask[0][1],point.mask[1][0],point.mask[1][1],
          point.boundary_value[0][0],point.boundary_value[0][1],point.boundary_value[1][0],point.boundary_value[1][1],x1[i],x2[i]
          )==1) {
        if(l==0)ts.add(new KeyValuePair(x1[i],x2[i],i));
        else ts.add(new KeyValuePair(x2[i],x1[i],i));
      }
    }
    if(ts.size()==0) {
      return point;
    }
    //排序
    Collections.sort(ts);
    //取中位数
    int m = (ts.size())/2;
    point.depth = depth;
    if(l==0) {
      point.setX1(ts.get(m).getKey());
      point.setX2(ts.get(m).getValue());
    }else {
      point.setX1(ts.get(m).getValue());
      point.setX2(ts.get(m).getKey());
    }
    used[ts.get(m).getW()]=1;
    point.id=ts.get(m).getW();
    if(ts.size() <= 1)
    {
      return point;
    }
      
    Tree left = new Tree(-2147483648, -2147483648); 
    Tree right = new Tree(-2147483648, -2147483648);
    //左右两子树分别拷贝父节点的 边界值
    for(int i=0;i<2;i++)
    {
      for(int j=0;j<2;j++) {
        left.boundary_value[i][j]=point.boundary_value[i][j];
        right.boundary_value[i][j]=point.boundary_value[i][j];
        left.mask[i][j]=point.mask[i][j];
        right.mask[i][j]=point.mask[i][j];
      }
    }
    left.parent = point;
    right.parent = point;
    if(l==0) {
      //x1 维度
      //如果左子树， 设置右边界
      //如果右子树， 设置左边界
      left.boundary_value[0][1]=ts.get(m).getKey();
      left.mask[0][1]=true;
      right.boundary_value[0][0]=ts.get(m).getKey();
      right.mask[0][0]=true;
    }else {
      left.boundary_value[1][1]=ts.get(m).getValue();
      left.mask[1][1]=true;
      right.boundary_value[1][0]=ts.get(m).getValue();
      right.mask[1][0]=true;
    }
    
    //如果左子树/右子树的  边界值和 point相同，说明左子树或者右子树为空
    Tree _left = buildTree(left,depth+1);
    Tree _right = buildTree(right,depth+1);
    if(!(_left.x1==-2147483648&&_left.x2==-2147483648)) {
      point.left = _left;
    }
    if(!(_right.x1==-2147483648&&_right.x2==-2147483648)) {
      point.right =  _right;
    }
    return point;
  }
  
  
  public static double dis(Tree a, Tree b) {
    //根据维度计算距离
    return Math.sqrt((a.x1-b.x1)*(a.x1-b.x1)+(a.x2-b.x2)*(a.x2-b.x2));
  }
  /**
   * 这个递归完成的任务：
   * 1。此节点和最近节点对比。
   * 2。检查父节点的另外一个节点对应的区域是否有更近点。如果有，递归搜索另一个子区域。如果没有向上回退到父节点
   * @param point
   */
  public static Tree Search(Tree point,Tree nearest,Tree target) {
    if(point.id==-1)return nearest;
    if(mk[point.id]==1)return nearest;
    if(point.id>=0)
      mk[point.id]=1;
    if(dis(point,target)<dis(nearest,target)) {
      nearest=point;
    }
    //如果以目标点为圆心的园，如果和父节点另外一个字节点有交集， 搜索父节点的另外一个节点
    double dis = dis(nearest,target);
    Tree parent = point.parent;  
    if(parent == null || (parent.x1==-2147483648&&parent.x2==-2147483648))return nearest;
    Tree bro = null;
    if(parent.left.x1==point.x1&&parent.left.x2==point.x2)bro=parent.right;
    if(parent.right.x1==point.x1&&parent.right.x2==point.x2)bro=parent.left;
    if(bro==null)return nearest;
    int judge = 0;
    judge = judge | judge(bro.mask[0][0],bro.mask[0][1],bro.mask[1][0],bro.mask[1][1],
      bro.boundary_value[0][0],bro.boundary_value[0][1],bro.boundary_value[1][0],bro.boundary_value[1][1],
      target.x1+dis,target.x2);
    judge = judge | judge(bro.mask[0][0],bro.mask[0][1],bro.mask[1][0],bro.mask[1][1],
      bro.boundary_value[0][0],bro.boundary_value[0][1],bro.boundary_value[1][0],bro.boundary_value[1][1],
      target.x1-dis,target.x2);
    judge = judge | judge(bro.mask[0][0],bro.mask[0][1],bro.mask[1][0],bro.mask[1][1],
      bro.boundary_value[0][0],bro.boundary_value[0][1],bro.boundary_value[1][0],bro.boundary_value[1][1],
      target.x1,target.x2+dis);
    judge = judge | judge(bro.mask[0][0],bro.mask[0][1],bro.mask[1][0],bro.mask[1][1],
      bro.boundary_value[0][0],bro.boundary_value[0][1],bro.boundary_value[1][0],bro.boundary_value[1][1],
      target.x1,target.x2-dis);
    if(judge == 1) {
      Tree d = findNearestValue(bro,target);
      return Search(d,nearest,target);
    }
    else {
      return  Search(point.parent,nearest,target);
    }
  }
  
  /**
   * 从更节点出发，如果目标点x小于切分点的坐标，左移到左子树，否则右移到右子树
   * 一个主意点：要根据当前维来判断
   * @param point
   * @return
   */
  public static Tree findNearestValue(Tree point,Tree target) {
    int l = point.depth%2;
    //如果存在左子树
    if(point.left!=null) {
      //System.out.println(point.left.x1+"*"+point.left.x2);
      if(l==0 && target.x1 < point.x1) {
        return findNearestValue(point.left,target);
      }
      if(l==1 && target.x2 < point.x2) {
        return findNearestValue(point.left,target);
      }
    }
    
    //如果存在右子树
    if(point.right!=null) {
      if(l==0 && target.x1 >= point.x1) {
        return findNearestValue(point.right,target);
      }
      if(l==1 && target.x2 >= point.x2) {
        return findNearestValue(point.right,target);
      }
    }
    //System.out.println(point.x1+"fk"+point.x2);
    return point;
  }
  
  //打印
  public static void print(Tree g) {
    System.out.println(g.x1 + " * "+ g.x2 +" "+ g.id +" "+ g.boundary_value[0][0] + " " + g.boundary_value[0][1] + " "
      +g.boundary_value[1][0] + " " +g.boundary_value[1][1] );
    if(g.left!=null)
      print(g.left);
    if(g.right!=null)
      print(g.right);
  }
  
  public static void main(String[] args) {
    // dataSet 有两维
    Tree tree = new Tree(-2147483648, -2147483648);
    tree = buildTree(tree,0);
    //print(tree);
    
    Tree target = new Tree(3.5,7);
    Tree g = findNearestValue(tree,target);
    
    Tree ans = Search(g,g,target);
    System.out.println(ans.x1+" "+ans.x2 + " " + ans.depth);
    
  }
}
