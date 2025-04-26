import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class Blacktest {
 public void readWordsToGraph(DGraph graph,String fileName){
  //graph = new DGraph();
  File file = new File(fileName);
  BufferedReader reader = null;
  try{
   reader = new BufferedReader(new FileReader(file));
   String tempString = null;
   String word1 = null;
   String word2 = null;
   //下面的循环意思就是，读到文件末尾，为每相邻的两个单词创建有向图的边
   while((tempString = reader.readLine())!=null){
    word2 = tempString;
    if(word1 != null){
     //是自己实现的addedge函数
     graph.addEdge(word1, word2);
    }
    word1 = tempString;
    //System.out.println(tempString);
   }
   reader.close();
  }catch(IOException e){
   e.printStackTrace();
  }finally{
   if(reader!=null){
    try{
     reader.close();
    }catch(IOException e1){
    }
   }
  }
 }
 @Test
 public void test0(){
  DGraph graph = new DGraph();
  readWordsToGraph(graph,"src/empty.txt");
  System.out.println(graph.calcShortestPath("word1","word2"));
 }
 @Test
 public void test1() {
  DGraph graph = new DGraph();
  readWordsToGraph(graph,"src/exampleResults.txt");
  System.out.println(graph.calcShortestPath("to","new"));

 }
 @Test
 public void test2() {
  DGraph graph = new DGraph();
  readWordsToGraph(graph,"src/exampleResults.txt");
  System.out.println(graph.calcShortestPath("123","456"));
 }
 @Test
 public void test3() {
  DGraph graph = new DGraph();
  readWordsToGraph(graph,"src/exampleResults.txt");
  System.out.println(graph.calcShortestPath("civilizations","worlds"));
 }
}
